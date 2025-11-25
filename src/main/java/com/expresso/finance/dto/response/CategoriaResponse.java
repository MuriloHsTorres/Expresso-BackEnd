package com.expresso.finance.dto.response;

import com.expresso.finance.entity.Categoria;

// DTO para GET /api/categorias
public class CategoriaResponse {

    private String id;
    private String nome;
    private String clienteId;

    // Construtor que facilita a conversão da Entidade para o DTO
    public CategoriaResponse(Categoria categoria) {
        this.id = categoria.getId();
        this.nome = categoria.getNome();
        this.clienteId = categoria.getCliente().getId();
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

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }
}