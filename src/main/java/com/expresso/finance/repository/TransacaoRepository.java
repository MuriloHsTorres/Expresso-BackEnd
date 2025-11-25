package com.expresso.finance.repository;

import com.expresso.finance.entity.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional; // 1. IMPORTAR
import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, String> {

    List<Transacao> findAllByClienteId(String clienteId);

    List<Transacao> findAllByClienteIdOrderByDataOperacaoDescDataCriacaoDesc(String clienteId);

    List<Transacao> findAllByContaId(String contaId);

    boolean existsByCategoriaId(String categoriaId);

    // 2. ADICIONAR ESTA LINHA (PARA O PASSO 135)
    @Transactional
    void deleteAllByClienteId(String clienteId);
}