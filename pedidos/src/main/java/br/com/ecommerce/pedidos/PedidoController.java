package br.com.ecommerce.pedidos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    private static final String PRODUTOS_SERVICE_URL = "http://localhost:8081";

    @PostMapping
    public ResponseEntity<Map<String, Object>> createPedido(@RequestBody Map<String, Integer> request) {
        Long produtoId = request.get("produtoId");
        Integer quantidade = request.get("quantidade");

        // 1. Consultar produto no serviço de produtos via REST (síncrono)
        Produto produto = restTemplate.getForObject(
                PRODUTOS_SERVICE_URL + "/produtos/" + produtoId, Produto.class);

        if (produto == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Produto not found with id: " + produtoId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        // 2. Verificar estoque suficiente
        if (produto.getEstoque() < quantidade) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Estoque insuficiente. Disponível: " + produto.getEstoque() + ", Solicitado: " + quantidade);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        // 3. Criar pedido
        Pedido pedido = new Pedido();
        pedido.setStatus("CONFIRMADO");
        pedido.setValorTotal(produto.getPreco().multiply(new BigDecimal(quantidade)));
        pedido.setProduto(produto);

        // 4. Abater estoque (síncrono)
        produto.setEstoque(produto.getEstoque() - quantidade);
        produtoRepository.save(produto);
        pedidoRepository.save(pedido);

        // 5. Publicar evento Kafka assíncrono
        kafkaProducerService.publishPedidoCriado(
                String.valueOf(produtoId), 
                quantidade, 
                pedido.getValorTotal().doubleValue()
        );

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Pedido criado com sucesso");
        response.put("pedidoId", pedido.getId());
        response.put("produtoNome", produto.getNome());
        response.put("quantidade", quantidade);
        response.put("valorTotal", pedido.getValorTotal());
        response.put("estoqueRestante", produto.getEstoque());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public Iterable<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }
}