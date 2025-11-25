package com.expresso.finance.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

// DTO para POST e PUT /api/metas
public class MetaRequest {

    private String nome;
    private BigDecimal valorAlvo;
    private LocalDate dataAlvo;
    private String clienteId;

    // Opcional: para o PUT de adicionar/remover valor
    private BigDecimal valorAtualizacao;

    // --- Getters e Setters Manuais ---

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getValorAlvo() {
        return valorAlvo;
    }

    public void setValorAlvo(BigDecimal valorAlvo) {
        this.valorAlvo = valorAlvo;
    }

    public LocalDate getDataAlvo() {
        return dataAlvo;
    }

    public void setDataAlvo(LocalDate dataAlvo) {
        this.dataAlvo = dataAlvo;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public BigDecimal getValorAtualizacao() {
        return valorAtualizacao;
    }

    public void setValorAtualizacao(BigDecimal valorAtualizacao) {
        this.valorAtualizacao = valorAtualizacao;
    }
}