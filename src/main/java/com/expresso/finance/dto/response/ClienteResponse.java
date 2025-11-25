package com.expresso.finance.dto.response;

import com.expresso.finance.entity.Cliente; // Importar a entidade
import java.time.LocalDateTime;

// (Se você usa @Data do Lombok, pode manter, mas os construtores são necessários)
public class ClienteResponse {

    private String id;
    private String nome;
    private String email;
    private LocalDateTime dataCriacao;

    // --- Construtor 1 (que o seu método login() antigo usa) ---
    public ClienteResponse(String id, String nome, String email, LocalDateTime dataCriacao) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.dataCriacao = dataCriacao;
    }

    // --- Construtor 2 (O NOVO, que estava a faltar) ---
    // (usado pelo cadastrar() e atualizarCliente())
    public ClienteResponse(Cliente cliente) {
        this.id = cliente.getId();
        this.nome = cliente.getNome();
        this.email = cliente.getEmail();
        this.dataCriacao = cliente.getDataCriacao();
    }

    // --- Getters e Setters ---
    // (Se você não usa Lombok, adicione os Getters e Setters aqui)

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
}