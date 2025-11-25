package com.expresso.finance.repository;

import com.expresso.finance.entity.Transferencia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
public interface TransferenciaRepository extends JpaRepository<Transferencia, String> {

    // "Spring, encontre todas as Transferencias pelo 'clienteId'"
    List<Transferencia> findAllByClienteId(String clienteId);

    @Transactional
    void deleteAllByClienteId(String clienteId);
}