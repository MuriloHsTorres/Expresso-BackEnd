package com.expresso.finance.dto.request;

// DTO para POST /api/categorias
public class CategoriaRequest {

    private String nome;
    private String clienteId; // O ID do cliente dono da categoria

    // --- Getters e Setters Manuais ---

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }
}