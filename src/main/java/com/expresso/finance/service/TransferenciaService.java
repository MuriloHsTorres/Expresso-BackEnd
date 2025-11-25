package com.expresso.finance.service;

import com.expresso.finance.dto.request.TransacaoRequest; // DTO do módulo de Transação
import com.expresso.finance.dto.request.TransferenciaRequest;
import com.expresso.finance.dto.response.TransacaoResponse; // Response do módulo de Transação
import com.expresso.finance.dto.response.TransferenciaResponse;
import com.expresso.finance.entity.*;
import com.expresso.finance.exception.ResourceNotFoundException;
import com.expresso.finance.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransferenciaService {

    @Autowired
    private TransferenciaRepository transferenciaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private TransacaoRepository transacaoRepository; // Para buscar as transações salvas

    // Vamos REUTILIZAR o TransacaoService para criar as transações
    @Autowired
    private TransacaoService transacaoService;

    /**
     * Endpoint: POST /api/transferencias
     * Executa uma transferência entre duas contas do cliente
     */
    @Transactional // Garante que TODAS as operações falhem ou tenham sucesso juntas
    public TransferenciaResponse executarTransferencia(TransferenciaRequest request) {
        // 1. Validar Valor
        if (request.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("O valor da transferência deve ser positivo.");
        }

        // 2. Buscar Entidades Principais
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        Conta contaOrigem = contaRepository.findById(request.getContaOrigemId())
                .orElseThrow(() -> new ResourceNotFoundException("Conta de Origem não encontrada"));

        Conta contaDestino = contaRepository.findById(request.getContaDestinoId())
                .orElseThrow(() -> new ResourceNotFoundException("Conta de Destino não encontrada"));

        // 3. REGRA DE NEGÓCIO: Verificar Saldo
        if (contaOrigem.getSaldoAtual().compareTo(request.getValor()) < 0) {
            // Saldo (ex: 100) é menor que o valor (ex: 150)
            throw new RuntimeException("Saldo insuficiente na conta de origem.");
        }

        // 4. Buscar Categoria "Transferências" (que foi criada por padrão)
        Categoria categoriaTransf = categoriaRepository
                .findByNomeAndClienteId("Transferências", cliente.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoria 'Transferências' não encontrada. " +
                                "O sistema pode não ter sido inicializado corretamente."));

        // 5. Criar Transação de DÉBITO (Saída)
        TransacaoRequest debitoRequest = new TransacaoRequest();
        debitoRequest.setClienteId(cliente.getId());
        debitoRequest.setContaId(contaOrigem.getId());
        debitoRequest.setCategoriaId(categoriaTransf.getId());
        debitoRequest.setDataOperacao(request.getDataOperacao());
        debitoRequest.setDescricao("Transferência para " + contaDestino.getNome());
        debitoRequest.setValor(request.getValor().negate()); // Valor NEGATIVO

        TransacaoResponse debitoResponse = transacaoService.criarTransacao(debitoRequest);

        // 6. Criar Transação de CRÉDITO (Entrada)
        TransacaoRequest creditoRequest = new TransacaoRequest();
        creditoRequest.setClienteId(cliente.getId());
        creditoRequest.setContaId(contaDestino.getId());
        creditoRequest.setCategoriaId(categoriaTransf.getId());
        creditoRequest.setDataOperacao(request.getDataOperacao());
        creditoRequest.setDescricao("Transferência de " + contaOrigem.getNome());
        creditoRequest.setValor(request.getValor()); // Valor POSITIVO

        TransacaoResponse creditoResponse = transacaoService.criarTransacao(creditoRequest);

        // 7. Buscar as entidades de Transação que acabaram de ser criadas
        Transacao transacaoDebito = transacaoRepository.findById(debitoResponse.getId()).get();
        Transacao transacaoCredito = transacaoRepository.findById(creditoResponse.getId()).get();

        // 8. Criar o "Recibo" de Transferência
        Transferencia recibo = new Transferencia();
        recibo.setCliente(cliente);
        recibo.setContaOrigem(contaOrigem);
        recibo.setContaDestino(contaDestino);
        recibo.setDataOperacao(request.getDataOperacao());
        recibo.setValor(request.getValor());
        recibo.setTransacaoDebito(transacaoDebito);
        recibo.setTransacaoCredito(transacaoCredito);

        Transferencia reciboSalvo = transferenciaRepository.save(recibo);

        // 9. Retornar o DTO de Resposta (o recibo completo)
        return new TransferenciaResponse(reciboSalvo);
    }

    /**
     * Endpoint: GET /api/transferencias/cliente/{clienteId}
     * Lista o histórico de recibos de transferências
     */
    public List<TransferenciaResponse> listarTransferenciasPorCliente(String clienteId) {
        return transferenciaRepository.findAllByClienteId(clienteId)
                .stream()
                .map(TransferenciaResponse::new)
                .collect(Collectors.toList());
    }
}