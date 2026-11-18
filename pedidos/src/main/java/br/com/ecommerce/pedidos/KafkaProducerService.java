package br.com.ecommerce.pedidos;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "pedido-criado";

    public void publishPedidoCriado(String produtoId, Integer quantidade, Double precoTotal) {
        String message = "produtoId:" + produtoId + "|quantidade:" + quantidade + "|precoTotal:" + precoTotal;
        kafkaTemplate.send(TOPIC, message);
        System.out.println("Mensagem Kafka enviada: " + message);
    }
}