package com.expresso.finance.repository;

import com.expresso.finance.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// JpaRepository<Classe, TipoDoID>
public interface ClienteRepository extends JpaRepository<Cliente, String> {

    // Método para o Login: "Spring, me encontre um Cliente pelo campo 'email'"
    Optional<Cliente> findByEmail(String email);

    // Método para o Cadastro: "Spring, me diga se 'existe' um Cliente pelo 'email'"
    boolean existsByEmail(String email);
}