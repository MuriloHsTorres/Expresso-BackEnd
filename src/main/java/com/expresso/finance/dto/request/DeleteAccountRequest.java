package com.expresso.finance.dto.request;

// (Pode usar @Data do Lombok)
public class DeleteAccountRequest {

    // A senha atual do utilizador, para confirmar a exclusão
    private String senhaAtual;

    // Getters e Setters
    public String getSenhaAtual() {
        return senhaAtual;
    }
    public void setSenhaAtual(String senhaAtual) {
        this.senhaAtual = senhaAtual;
    }
}