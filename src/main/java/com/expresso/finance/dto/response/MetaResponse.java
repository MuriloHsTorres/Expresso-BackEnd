package com.expresso.finance.dto.response;

import com.expresso.finance.entity.Meta;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

// DTO para GET /api/metas
public class MetaResponse {

    private String id;
    private String nome;
    private BigDecimal valorAlvo;
    private BigDecimal valorAtual; // Este campo permanece...
    private LocalDate dataAlvo;
    private LocalDateTime dataCriacao;
    private String clienteId;
    private String contaAssociadaId; // Útil para o frontend saber o ID da conta-meta

    // Construtor que facilita a conversão da Entidade para o DTO
    public MetaResponse(Meta meta) {
        this.id = meta.getId();
        this.nome = meta.getNome();
        this.valorAlvo = meta.getValorAlvo();

        // *** MUDANÇA IMPORTANTE AQUI ***
        // O 'valorAtual' da Meta é o 'saldoAtual' da sua Conta associada
        this.valorAtual = meta.getContaAssociada().getSaldoAtual();

        this.dataAlvo = meta.getDataAlvo();
        this.dataCriacao = meta.getDataCriacao();
        this.clienteId = meta.getCliente().getId();
        this.contaAssociadaId = meta.getContaAssociada().getId();
    }

    // --- Getters e Setters Manuais ---

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public BigDecimal getValorAlvo() { return valorAlvo; }
    public void setValorAlvo(BigDecimal valorAlvo) { this.valorAlvo = valorAlvo; }
    public BigDecimal getValorAtual() { return valorAtual; }
    public void setValorAtual(BigDecimal valorAtual) { this.valorAtual = valorAtual; }
    public LocalDate getDataAlvo() { return dataAlvo; }
    public void setDataAlvo(LocalDate dataAlvo) { this.dataAlvo = dataAlvo; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }
    public String getContaAssociadaId() { return contaAssociadaId; } // Novo Getter
    public void setContaAssociadaId(String contaAssociadaId) { this.contaAssociadaId = contaAssociadaId; } // Novo Setter
}