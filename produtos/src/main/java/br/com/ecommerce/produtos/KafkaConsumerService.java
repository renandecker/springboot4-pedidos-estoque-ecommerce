package br.com.ecommerce.produtos;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "pedido-criado", groupId = "grupo-produtos")
    public void consumirPedidoCriado(String message) {
        System.out.println("Evento Kafka recebido: " + message);
        // Aqui poderia-se lógica adicional como:
        // - Atualizar logs de auditoria
        // - Enviar notificações
        // - Atualizar métricas
        // - Reconciliar estoque em caso de falha síncrona
        System.out.println("Processando evento de pedido criado para sincronização de estoque...");
    }
}