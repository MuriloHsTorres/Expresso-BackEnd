package com.expresso.finance.repository;

import com.expresso.finance.entity.Meta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface MetaRepository extends JpaRepository<Meta, String> {

    // Método que o DataContext usa
    List<Meta> findAllByClienteId(String clienteId);

    // Método que o Passo 135 precisa
    @Transactional
    void deleteAllByClienteId(String clienteId);
}