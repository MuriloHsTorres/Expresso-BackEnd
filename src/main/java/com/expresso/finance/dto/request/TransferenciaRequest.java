package com.expresso.finance.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// DTO para POST /api/transferencias
public class TransferenciaRequest {

    private BigDecimal valor; // Valor (sempre positivo)
    private LocalDateTime dataOperacao;
    private String clienteId;
    private String contaOrigemId;
    private String contaDestinoId;

    // --- Getters e Setters Manuais ---

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
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

    public String getContaOrigemId() {
        return contaOrigemId;
    }

    public void setContaOrigemId(String contaOrigemId) {
        this.contaOrigemId = contaOrigemId;
    }

    public String getContaDestinoId() {
        return contaDestinoId;
    }

    public void setContaDestinoId(String contaDestinoId) {
        this.contaDestinoId = contaDestinoId;
    }
}