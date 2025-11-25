package com.expresso.finance.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity
@Table(name = "meta")
public class Meta {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 36)
    private String id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private BigDecimal valorAlvo; // O valor que o utilizador quer atingir

    // --- CAMPO 'valorAtual' REMOVIDO ---
    // (Este valor será agora o 'saldoAtual' da 'contaAssociada')

    @Column(nullable = false)
    private LocalDate dataAlvo; // A data limite para a meta

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    // Relacionamento: Muitas metas pertencem a UM cliente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    // NOVO RELACIONAMENTO: Uma Meta tem UMA Conta "Cofrinho"
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "conta_associada_id", referencedColumnName = "id", nullable = false)
    private Conta contaAssociada;

    // --- Getters e Setters Manuais ---

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public BigDecimal getValorAlvo() { return valorAlvo; }
    public void setValorAlvo(BigDecimal valorAlvo) { this.valorAlvo = valorAlvo; }

    // --- GETTER/SETTER de 'valorAtual' REMOVIDO ---

    public LocalDate getDataAlvo() { return dataAlvo; }
    public void setDataAlvo(LocalDate dataAlvo) { this.dataAlvo = dataAlvo; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Conta getContaAssociada() { return contaAssociada; } // Novo Getter
    public void setContaAssociada(Conta contaAssociada) { this.contaAssociada = contaAssociada; } // Novo Setter

    // --- Ciclo de Vida do JPA ---
    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDateTime.now();

        // --- REGRA de 'valorAtual = 0' REMOVIDA DAQUI ---
        // (Isto será tratado na criação da Conta associada)
    }
}