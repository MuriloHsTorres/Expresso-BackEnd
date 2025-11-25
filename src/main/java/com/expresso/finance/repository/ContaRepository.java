package com.expresso.finance.repository;

import com.expresso.finance.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface ContaRepository extends JpaRepository<Conta, String> {

    // Método que o DataContext usa
    List<Conta> findAllByClienteId(String clienteId);

    // Método que o Passo 135 precisa
    @Transactional
    void deleteAllByClienteId(String clienteId);
}