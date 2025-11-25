package com.expresso.finance.entity;

import com.expresso.finance.entity.enums.TipoConta; // Importe o Enum
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "conta")
public class Conta {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 36)
    private String id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private BigDecimal saldoAbertura;

    @Column(nullable = false)
    private BigDecimal saldoAtual;

    // NOVO CAMPO: Para diferenciar Contas normais de Contas-Meta
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoConta tipoConta;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    // NOVO RELACIONAMENTO: Se esta for uma CONTA_META,
    // ela estará ligada à sua Meta original.
    @OneToOne(mappedBy = "contaAssociada", fetch = FetchType.LAZY)
    private Meta meta; // 'meta' é o nome do campo na classe Meta

    // --- Getters e Setters Manuais ---

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public BigDecimal getSaldoAbertura() { return saldoAbertura; }
    public void setSaldoAbertura(BigDecimal saldoAbertura) { this.saldoAbertura = saldoAbertura; }
    public BigDecimal getSaldoAtual() { return saldoAtual; }
    public void setSaldoAtual(BigDecimal saldoAtual) { this.saldoAtual = saldoAtual; }

    public TipoConta getTipoConta() { return tipoConta; } // Novo Getter
    public void setTipoConta(TipoConta tipoConta) { this.tipoConta = tipoConta; } // Novo Setter

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Meta getMeta() { return meta; } // Novo Getter
    public void setMeta(Meta meta) { this.meta = meta; } // Novo Setter

    // --- Ciclo de Vida do JPA ---
    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDateTime.now();

        // Regra de Negócio: Se o tipo não for definido, é CONTA_CORRENTE
        if (this.tipoConta == null) {
            this.tipoConta = TipoConta.CONTA_CORRENTE;
        }
    }
}