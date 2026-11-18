package br.com.ecommerce.pedidos;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;
    private BigDecimal valorTotal;
    private LocalDateTime dataCriacao;

    @ManyToOne
    private Produto produto; // Note: This would typically be a reference, but for simplicity in PoC

    public Pedido() {
        this.dataCriacao = LocalDateTime.now();
        this.status = "PENDENTE";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }

    @Override
    public String toString() {
        return "Pedido{id=" + id + ", status='" + status + "', valorTotal=" + valorTotal + ", dataCriacao=" + dataCriacao + "}";
    }
}