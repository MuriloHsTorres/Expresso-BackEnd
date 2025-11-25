package com.expresso.finance.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transferencia")
public class Transferencia {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 36)
    private String id;

    // O valor transferido (sempre positivo)
    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private LocalDateTime dataOperacao;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    // --- Relacionamentos ---

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_origem_id", nullable = false)
    private Conta contaOrigem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_destino_id", nullable = false)
    private Conta contaDestino;

    // Ligação com a transação de SAÍDA (débito)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transacao_debito_id", nullable = false, unique = true)
    private Transacao transacaoDebito;

    // Ligação com a transação de ENTRADA (crédito)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transacao_credito_id", nullable = false, unique = true)
    private Transacao transacaoCredito;

    // --- Getters e Setters Manuais ---

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public LocalDateTime getDataOperacao() { return dataOperacao; }
    public void setDataOperacao(LocalDateTime dataOperacao) { this.dataOperacao = dataOperacao; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Conta getContaOrigem() { return contaOrigem; }
    public void setContaOrigem(Conta contaOrigem) { this.contaOrigem = contaOrigem; }
    public Conta getContaDestino() { return contaDestino; }
    public void setContaDestino(Conta contaDestino) { this.contaDestino = contaDestino; }
    public Transacao getTransacaoDebito() { return transacaoDebito; }
    public void setTransacaoDebito(Transacao transacaoDebito) { this.transacaoDebito = transacaoDebito; }
    public Transacao getTransacaoCredito() { return transacaoCredito; }
    public void setTransacaoCredito(Transacao transacaoCredito) { this.transacaoCredito = transacaoCredito; }

    // --- Ciclo de Vida do JPA ---
    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDateTime.now();
    }
}