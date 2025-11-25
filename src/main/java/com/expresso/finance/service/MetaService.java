package com.expresso.finance.service;

import com.expresso.finance.dto.request.MetaRequest;
import com.expresso.finance.dto.response.MetaResponse;
import com.expresso.finance.entity.Cliente;
import com.expresso.finance.entity.Conta; // Importe a Conta
import com.expresso.finance.entity.Meta;
import com.expresso.finance.entity.enums.TipoConta; // Importe o TipoConta
import com.expresso.finance.exception.ResourceNotFoundException;
import com.expresso.finance.repository.ClienteRepository;
import com.expresso.finance.repository.ContaRepository; // Importe o ContaRepository
import com.expresso.finance.repository.MetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importe o Transactional

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MetaService {

    @Autowired
    private MetaRepository metaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    // Precisamos do ContaRepository para atualizar o nome da conta-meta
    @Autowired
    private ContaRepository contaRepository;

    /**
     * Endpoint: POST /api/metas - REESCRITO
     * Cria uma nova meta E a sua "Conta-Cofrinho" associada
     */
    @Transactional // Garante que a Meta E a Conta sejam criadas juntas
    public MetaResponse criarMeta(MetaRequest request) {
        // 1. Buscar o cliente "dono"
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        // 2. Criar a "Conta-Cofrinho"
        Conta contaMeta = new Conta();
        contaMeta.setNome("Meta: " + request.getNome()); // Nomeia a conta-meta
        contaMeta.setSaldoAbertura(BigDecimal.ZERO);
        contaMeta.setSaldoAtual(BigDecimal.ZERO);
        contaMeta.setCliente(cliente);
        contaMeta.setTipoConta(TipoConta.CONTA_META); // Define o tipo!

        // 3. Criar a nova meta
        Meta meta = new Meta();
        meta.setNome(request.getNome());
        meta.setValorAlvo(request.getValorAlvo());
        meta.setDataAlvo(request.getDataAlvo());
        meta.setCliente(cliente);

        // 4. Ligar os dois. A Meta é "dona" da Conta
        meta.setContaAssociada(contaMeta);
        // (Não precisamos salvar a contaMeta separadamente, o CascadeType.ALL fará isso)

        // 5. Salvar a Meta (que salvará a Conta junto)
        Meta metaSalva = metaRepository.save(meta);

        // 6. Retornar o DTO de resposta (o construtor do DTO já sabe
        //    como ler o saldo da conta associada)
        return new MetaResponse(metaSalva);
    }

    /**
     * Endpoint: GET /api/metas/cliente/{clienteId}
     * (Este método não muda)
     */
    public List<MetaResponse> listarMetasPorCliente(String clienteId) {
        List<Meta> metas = metaRepository.findAllByClienteId(clienteId);

        return metas.stream()
                .map(MetaResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * Endpoint: PUT /api/metas/{id} - REESCRITO
     * Atualiza os dados de uma meta E o nome da sua conta associada
     */
    @Transactional
    public MetaResponse atualizarMeta(String id, MetaRequest request) {
        Meta meta = metaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meta não encontrada"));

        Conta contaAssociada = meta.getContaAssociada();

        // Atualiza os dados principais da Meta
        meta.setNome(request.getNome());
        meta.setValorAlvo(request.getValorAlvo());
        meta.setDataAlvo(request.getDataAlvo());

        // Atualiza o nome da Conta-Cofrinho para manter a consistência
        contaAssociada.setNome("Meta: " + request.getNome());

        // Salva as entidades
        // (Dentro de um @Transactional, o JPA/Hibernate deteta a mudança
        // na 'contaAssociada' (dirty checking) quando salvamos a 'meta')
        Meta metaAtualizada = metaRepository.save(meta);

        return new MetaResponse(metaAtualizada);
    }

    /**
     * Endpoint: PUT /api/metas/{id}/valor - REMOVIDO
     * Esta lógica será feita pelo Módulo de Transferências
     */
    // O método atualizarValorMeta() foi apagado.


    /**
     * Endpoint: DELETE /api/metas/{id}
     * (Este método não muda, mas a sua funcionalidade está mais poderosa)
     */
    public void deletarMeta(String id) {
        Meta meta = metaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meta não encontrada"));

        // Graças ao CascadeType.ALL que definimos na entidade Meta,
        // ao deletar a Meta, o JPA também deletará a Conta "cofrinho" associada.
        metaRepository.delete(meta);
    }
}