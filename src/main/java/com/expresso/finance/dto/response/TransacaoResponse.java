package com.expresso.finance.dto.response;

import com.expresso.finance.entity.Transacao;
import java.math.BigDecimal;
import java.time.LocalDateTime;

// DTO para GET /api/transacoes
public class TransacaoResponse {

    private String id;
    private BigDecimal valor;
    private String descricao;
    private LocalDateTime dataOperacao;
    private LocalDateTime dataCriacao; // Data do registo no sistema

    // IDs para o frontend saber onde a transação pertence
    private String clienteId;
    private String contaId;
    private String categoriaId;

    // (Opcional, mas útil): Nomes das entidades relacionadas
    private String nomeConta;
    private String nomeCategoria;


    // Construtor que facilita a conversão da Entidade para o DTO
    public TransacaoResponse(Transacao transacao) {
        this.id = transacao.getId();
        this.valor = transacao.getValor();
        this.descricao = transacao.getDescricao();
        this.dataOperacao = transacao.getDataOperacao();
        this.dataCriacao = transacao.getDataCriacao();
        this.clienteId = transacao.getCliente().getId();
        this.contaId = transacao.getConta().getId();
        this.categoriaId = transacao.getCategoria().getId();

        // Preenche os nomes para facilitar a exibição no frontend
        this.nomeConta = transacao.getConta().getNome();
        this.nomeCategoria = transacao.getCategoria().getNome();
    }

    // --- Getters e Setters Manuais ---

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public LocalDateTime getDataOperacao() { return dataOperacao; }
    public void setDataOperacao(LocalDateTime dataOperacao) { this.dataOperacao = dataOperacao; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }
    public String getContaId() { return contaId; }
    public void setContaId(String contaId) { this.contaId = contaId; }
    public String getCategoriaId() { return categoriaId; }
    public void setCategoriaId(String categoriaId) { this.categoriaId = categoriaId; }
    public String getNomeConta() { return nomeConta; }
    public void setNomeConta(String nomeConta) { this.nomeConta = nomeConta; }
    public String getNomeCategoria() { return nomeCategoria; }
    public void setNomeCategoria(String nomeCategoria) { this.nomeCategoria = nomeCategoria; }
}