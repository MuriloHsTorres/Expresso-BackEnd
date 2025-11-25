package com.expresso.finance.dto.request;

// Nota: Se você usa Lombok, pode usar @Data e apagar os Getters/Setters
public class PerfilUpdateRequest {

    private String nome;
    private String email;
    private String senhaAtual; // O campo que você pediu
    private String novaSenha;  // A nova senha (opcional)

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenhaAtual() { return senhaAtual; }
    public void setSenhaAtual(String senhaAtual) { this.senhaAtual = senhaAtual; }
    public String getNovaSenha() { return novaSenha; }
    public void setNovaSenha(String novaSenha) { this.novaSenha = novaSenha; }
}