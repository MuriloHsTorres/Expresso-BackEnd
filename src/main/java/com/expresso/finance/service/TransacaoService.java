package com.expresso.finance.service;

import com.expresso.finance.dto.request.TransacaoRequest;
import com.expresso.finance.dto.response.TransacaoResponse;
import com.expresso.finance.entity.Cliente;
import com.expresso.finance.entity.Conta;
import com.expresso.finance.entity.Categoria;
import com.expresso.finance.entity.Transacao;
import com.expresso.finance.exception.ResourceNotFoundException;
import com.expresso.finance.repository.ClienteRepository;
import com.expresso.finance.repository.ContaRepository;
import com.expresso.finance.repository.CategoriaRepository;
import com.expresso.finance.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importe este

import java.math.BigDecimal; // Importe este
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    // Vamos precisar de todos os repositórios para buscar as entidades
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    /**
     * Endpoint: POST /api/transacoes
     * Cria uma nova transação E atualiza o saldo da conta
     */
    @Transactional // Garante que as duas operações (salvar transação, atualizar conta)
    // ou funcionam juntas, ou falham juntas.
    public TransacaoResponse criarTransacao(TransacaoRequest request) {
        // 1. Buscar as entidades relacionadas
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        Conta conta = contaRepository.findById(request.getContaId())
                .orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada"));

        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        // 2. Criar a nova transação
        Transacao transacao = new Transacao();
        transacao.setDescricao(request.getDescricao());
        transacao.setValor(request.getValor()); // O valor já vem +/-
        transacao.setDataOperacao(request.getDataOperacao());
        transacao.setCliente(cliente);
        transacao.setConta(conta);
        transacao.setCategoria(categoria);

        // 3. Salvar a transação
        Transacao transacaoSalva = transacaoRepository.save(transacao);

        // 4. *** REGRA DE NEGÓCIO: ATUALIZAR SALDO DA CONTA ***
        // O valor já está (positivo) ou (negativo)
        BigDecimal novoSaldo = conta.getSaldoAtual().add(transacao.getValor());
        conta.setSaldoAtual(novoSaldo);
        contaRepository.save(conta); // Salva a conta com o saldo atualizado

        // 5. Retornar o DTO de resposta
        return new TransacaoResponse(transacaoSalva);
    }

    /**
     * Endpoint: GET /api/transacoes/cliente/{clienteId}
     * Lista todas as transações de um cliente
     */
    public List<TransacaoResponse> listarTransacoesPorCliente(String clienteId) {
        List<Transacao> transacoes = transacaoRepository.findAllByClienteId(clienteId);

        return transacoes.stream()
                .map(TransacaoResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * Endpoint: GET /api/transacoes/conta/{contaId}
     * Lista todas as transações de uma conta (Extrato)
     */
    public List<TransacaoResponse> listarTransacoesPorConta(String contaId) {
        List<Transacao> transacoes = transacaoRepository.findAllByContaId(contaId);



        return transacoes.stream()
                .map(TransacaoResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * Endpoint: PUT /api/transacoes/{id}
     * Atualiza uma transação E re-calcula o saldo da conta
     */
    @Transactional
    public TransacaoResponse atualizarTransacao(String id, TransacaoRequest request) {
        // 1. Encontrar a transação original
        Transacao transacao = transacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transação não encontrada"));

        // 2. Encontrar a conta relacionada
        Conta conta = transacao.getConta();

        // 3. *** REGRA DE NEGÓCIO: RE-CALCULAR SALDO ***
        // 3a. Reverte o valor antigo
        BigDecimal saldoSemTransacao = conta.getSaldoAtual().subtract(transacao.getValor());
        // 3b. Aplica o valor novo
        BigDecimal novoSaldo = saldoSemTransacao.add(request.getValor());
        conta.setSaldoAtual(novoSaldo);
        contaRepository.save(conta);

        // 4. (Opcional: verificar se a conta ou categoria mudaram)
        // (Vamos manter simples por agora e só atualizar os valores)

        // 5. Atualizar os dados da transação
        transacao.setDescricao(request.getDescricao());
        transacao.setValor(request.getValor());
        transacao.setDataOperacao(request.getDataOperacao());

        Transacao transacaoAtualizada = transacaoRepository.save(transacao);
        return new TransacaoResponse(transacaoAtualizada);
    }

    /**
     * Endpoint: DELETE /api/transacoes/{id}
     * Deleta uma transação E re-calcula (desfaz) o saldo da conta
     */
    @Transactional
    public void deletarTransacao(String id) {
        // 1. Encontrar a transação
        Transacao transacao = transacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transação não encontrada"));

        // 2. *** REGRA DE NEGÓCIO: Bloquear exclusão de Transferências ***
        Categoria categoria = transacao.getCategoria();
        if (categoria != null && "Transferências".equals(categoria.getNome())) {
            throw new RuntimeException("Transações de 'Transferências' não podem ser excluídas diretamente.");
        }
        // FIM DA NOVA REGRA

        // 3. Encontrar a conta relacionada
        Conta conta = transacao.getConta();

        // 4. REGRA DE NEGÓCIO: REVERTER SALDO (Seu código está correto)
        BigDecimal novoSaldo = conta.getSaldoAtual().subtract(transacao.getValor());
        conta.setSaldoAtual(novoSaldo);
        contaRepository.save(conta);

        // 5. Deletar a transação
        transacaoRepository.delete(transacao);
    }
}