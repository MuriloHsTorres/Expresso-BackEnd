package com.expresso.finance.entity;

// REMOVEMOS a importação do Enum
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacao")
public class Transacao {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 36)
    private String id;

    // Valor da transação (pode ser POSITIVO para receita, NEGATIVO para despesa)
    @Column(nullable = false)
    private BigDecimal valor;

    @Column(length = 150)
    private String descricao;

    // ----- CAMPO 'TIPO' REMOVIDO -----

    // Data em que a transação ocorreu (ex: paguei o almoço ontem)
    @Column(nullable = false)
    private LocalDateTime dataOperacao;

    // Data em que a transação foi registada no sistema
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    // --- Relacionamentos ---

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_id", nullable = false)
    private Conta conta; // A conta que foi afetada

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria; // A categoria da transação

    // --- Getters e Setters Manuais ---

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    // ----- GETTER/SETTER DE 'TIPO' REMOVIDOS -----

    public LocalDateTime getDataOperacao() { return dataOperacao; }
    public void setDataOperacao(LocalDateTime dataOperacao) { this.dataOperacao = dataOperacao; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Conta getConta() { return conta; }
    public void setConta(Conta conta) { this.conta = conta; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    // --- Ciclo de Vida do JPA ---
    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDateTime.now();
    }
}