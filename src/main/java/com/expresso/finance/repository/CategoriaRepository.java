package com.expresso.finance.repository;

import com.expresso.finance.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional; // 1. IMPORTAR
import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, String> {

    List<Categoria> findAllByClienteId(String clienteId);

    Optional<Categoria> findByNomeAndClienteId(String nome, String clienteId);

    // 2. ADICIONAR ESTA LINHA (PARA O PASSO 135)
    @Transactional
    void deleteAllByClienteId(String clienteId);
}