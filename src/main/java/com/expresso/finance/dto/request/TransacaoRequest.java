package com.expresso.finance.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// DTO para POST /api/transacoes
public class TransacaoRequest {

    // Valor: POSITIVO para Receita, NEGATIVO para Despesa
    private BigDecimal valor;

    private String descricao;

    // Data que a operação realmente aconteceu
    private LocalDateTime dataOperacao;

    // IDs para os relacionamentos
    private String clienteId;
    private String contaId;
    private String categoriaId;

    // --- Getters e Setters Manuais ---

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataOperacao() {
        return dataOperacao;
    }

    public void setDataOperacao(LocalDateTime dataOperacao) {
        this.dataOperacao = dataOperacao;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public String getContaId() {
        return contaId;
    }

    public void setContaId(String contaId) {
        this.contaId = contaId;
    }

    public String getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(String categoriaId) {
        this.categoriaId = categoriaId;
    }
}