package com.expresso.finance.dto.response;

import com.expresso.finance.entity.Conta;
import java.math.BigDecimal;

// Este é o DTO que vamos retornar em GET /contas/...
public class ContaResponse {

    private String id;
    private String nome;
    private BigDecimal saldoAbertura;
    private BigDecimal saldoAtual;
    private String clienteId;

    // Construtor que facilita a conversão da Entidade para o DTO
    public ContaResponse(Conta conta) {
        this.id = conta.getId();
        this.nome = conta.getNome();
        this.saldoAbertura = conta.getSaldoAbertura();
        this.saldoAtual = conta.getSaldoAtual();
        this.clienteId = conta.getCliente().getId();
    }

    // --- Getters e Setters Manuais ---

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public BigDecimal getSaldoAtual() {
        return saldoAtual;
    }

    public void setSaldoAtual(BigDecimal saldoAtual) {
        this.saldoAtual = saldoAtual;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }
}