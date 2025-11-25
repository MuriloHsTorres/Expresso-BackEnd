package com.expresso.finance.dto.response;

import com.expresso.finance.entity.Transferencia;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransferenciaResponse {

    private String id;
    private BigDecimal valor;
    private LocalDateTime dataOperacao;
    private LocalDateTime dataCriacao;
    private String clienteId;
    private String contaOrigemId;
    private String contaDestinoId;
    private String nomeContaOrigem;
    private String nomeContaDestino;
    private String transacaoDebitoId;
    private String transacaoCreditoId;

    // Construtor que facilita a conversão
    public TransferenciaResponse(Transferencia transferencia) {
        this.id = transferencia.getId();
        this.valor = transferencia.getValor();
        this.dataOperacao = transferencia.getDataOperacao();
        this.dataCriacao = transferencia.getDataCriacao();
        this.clienteId = transferencia.getCliente().getId();
        this.contaOrigemId = transferencia.getContaOrigem().getId();
        this.contaDestinoId = transferencia.getContaDestino().getId();
        this.nomeContaOrigem = transferencia.getContaOrigem().getNome();
        this.nomeContaDestino = transferencia.getContaDestino().getNome();
        this.transacaoDebitoId = transferencia.getTransacaoDebito().getId();
        this.transacaoCreditoId = transferencia.getTransacaoCredito().getId();
    }

    // --- GETTERS E SETTERS MANUAIS (NECESSÁRIOS PARA O JACKSON) ---

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public LocalDateTime getDataOperacao() { return dataOperacao; }
    public void setDataOperacao(LocalDateTime dataOperacao) { this.dataOperacao = dataOperacao; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }
    public String getContaOrigemId() { return contaOrigemId; }
    public void setContaOrigemId(String contaOrigemId) { this.contaOrigemId = contaOrigemId; }
    public String getContaDestinoId() { return contaDestinoId; }
    public void setContaDestinoId(String contaDestinoId) { this.contaDestinoId = contaDestinoId; }
    public String getNomeContaOrigem() { return nomeContaOrigem; }
    public void setNomeContaOrigem(String nomeContaOrigem) { this.nomeContaOrigem = nomeContaOrigem; }
    public String getNomeContaDestino() { return nomeContaDestino; }
    public void setNomeContaDestino(String nomeContaDestino) { this.nomeContaDestino = nomeContaDestino; }
    public String getTransacaoDebitoId() { return transacaoDebitoId; }
    public void setTransacaoDebitoId(String transacaoDebitoId) { this.transacaoDebitoId = transacaoDebitoId; }
    public String getTransacaoCreditoId() { return transacaoCreditoId; }
    public void setTransacaoCreditoId(String transacaoCreditoId) { this.transacaoCreditoId = transacaoCreditoId; }
}