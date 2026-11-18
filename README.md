# Microserviços Spring Boot 4.x - PoC E-commerce

## Versão: Spring Boot 1.5.28.RELEASE | Java 8

### Serviços Incluídos
- `produtos/` - Serviço de Catálogo (porta 8081)
- `pedidos/` - Serviço de Pedidos (porta 8082)

### Como Rodar

#### Produtos (porta 8081)
```bash
cd produtos
mvn spring-boot:run
```

#### Pedidos (porta 8082)
```bash
cd pedidos
mvn spring-boot:run
```

### Testes

**Verificar produto:**
```bash
curl http://localhost:8081/produtos/1
```

**Criar pedido:**
```bash
curl -X POST http://localhost:8082/pedidos \
  -H "Content-Type: application/json" \
  -d '{"produtoId": 1, "quantidade": 2}'
```

### Observações
- Spring Boot 1.5.28.RELEASE (última da série 1.x)
- Java 8 necessário
- H2 Database em memória separado por serviço
- Comunicação REST via RestTemplate