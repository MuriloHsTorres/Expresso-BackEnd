package com.expresso.finance.dto.request;

import java.math.BigDecimal;

// Este é o DTO para POST /contas
public class ContaRequest {

    private String nome;
    private BigDecimal saldoAbertura;
    private String clienteId; // O ID do cliente dono da conta

    // --- Getters e Setters Manuais ---

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getSaldoAbertura() {
        return saldoAbertura;
    }

    public void setSaldoAbertura(BigDecimal saldoAbertura) {
        this.saldoAbertura = saldoAbertura;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }
}