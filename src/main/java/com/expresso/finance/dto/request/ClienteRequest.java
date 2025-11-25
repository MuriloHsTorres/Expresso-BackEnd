package com.expresso.finance.dto.request;

// (Mais tarde, podemos adicionar anotações de validação aqui, como @Email)
public class ClienteRequest {

    private String nome;
    private String email;
    private String senha;

    // --- Getters e Setters Manuais ---

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}