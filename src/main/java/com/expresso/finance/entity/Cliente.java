package com.expresso.finance.entity;

import jakarta.persistence.*; // Importa as anotações do JPA
import org.hibernate.annotations.GenericGenerator; // Importa o gerador de UUID
import java.time.LocalDateTime; // Importa a classe para 'dataCriacao'

@Entity // Diz ao Spring que esta classe é uma tabela
@Table(name = "cliente") // O nome da tabela no banco
public class Cliente {

    @Id // Marca este campo como a Chave Primária
    @GeneratedValue(generator = "uuid") // Diz para usar um gerador
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator") // Define o gerador como UUID
    @Column(length = 36)
    private String id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 120) // 'unique = true' garante que não teremos emails repetidos
    private String email;

    @Column(nullable = false, length = 60) // Vamos definir 60 para o hash BCrypt (como no seu escopo [cite: 220])
    private String senha;

    @Column(nullable = false, updatable = false) // 'updatable = false' significa que este campo nunca será atualizado
    private LocalDateTime dataCriacao;

    // --- Getters e Setters (Manual, como pedido no escopo) ---
    // [cite: 224-229]

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

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    // --- Método de Ciclo de Vida do JPA ---
    // Este método é chamado automaticamente ANTES de um novo cliente ser salvo no banco
    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDateTime.now(); // Define a data de criação [cite: 231, 233]
    }
}