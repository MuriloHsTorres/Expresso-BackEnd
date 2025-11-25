package com.expresso.finance.service;

import com.expresso.finance.dto.request.ContaRequest;
import com.expresso.finance.dto.response.ContaResponse;
import com.expresso.finance.entity.Cliente;
import com.expresso.finance.entity.Conta;
import com.expresso.finance.entity.enums.TipoConta; // Importe o Enum
import com.expresso.finance.exception.ResourceNotFoundException;
import com.expresso.finance.repository.ClienteRepository;
import com.expresso.finance.repository.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    /**
     * Método antigo (do Módulo 1) - ATUALIZADO
     */
    public void criarCarteiraPadrao(Cliente cliente) {
        Conta carteira = new Conta();
        carteira.setNome("Carteira");
        carteira.setSaldoAbertura(BigDecimal.ZERO);
        carteira.setSaldoAtual(BigDecimal.ZERO);
        carteira.setCliente(cliente);

        // --- MUDANÇA IMPORTANTE ---
        carteira.setTipoConta(TipoConta.CONTA_CORRENTE);

        contaRepository.save(carteira);
    }

    /**
     * Endpoint: POST /api/contas - ATUALIZADO
     * Cria uma nova conta (Ex: Nubank, Inter) para um cliente
     */
    public ContaResponse criarConta(ContaRequest request) {
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id: " + request.getClienteId()));

        Conta conta = new Conta();
        conta.setNome(request.getNome());
        conta.setSaldoAbertura(request.getSaldoAbertura());
        conta.setSaldoAtual(request.getSaldoAbertura());
        conta.setCliente(cliente);

        // --- MUDANÇA IMPORTANTE ---
        conta.setTipoConta(TipoConta.CONTA_CORRENTE);

        Conta contaSalva = contaRepository.save(conta);
        return new ContaResponse(contaSalva);
    }

    /**
     * Endpoint: GET /api/contas/cliente/{clienteId}
     * Lista todas as contas de um cliente específico
     */
    public List<ContaResponse> listarContasPorCliente(String clienteId) {
        List<Conta> contas = contaRepository.findAllByClienteId(clienteId);

        // Vamos filtrar para mostrar apenas as Contas Correntes
        // As Contas-Meta (cofrinhos) não devem aparecer aqui
        return contas.stream()
                .filter(conta -> conta.getTipoConta() == TipoConta.CONTA_CORRENTE)
                .map(ContaResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * Endpoint: GET /api/contas/{id}
     */
    public ContaResponse buscarContaPorId(String id) {
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada com id: " + id));

        return new ContaResponse(conta);
    }

    /**
     * Endpoint: PUT /api/contas/{id}
     */
    public ContaResponse atualizarConta(String id, ContaRequest request) {
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada com id: " + id));

        // Não permitir atualizar nome de Conta-Meta por aqui
        if (conta.getTipoConta() == TipoConta.CONTA_META) {
            throw new RuntimeException("Use a API de Metas para gerenciar esta conta.");
        }

        conta.setNome(request.getNome());

        Conta contaAtualizada = contaRepository.save(conta);
        return new ContaResponse(contaAtualizada);
    }

    /**
     * Endpoint: DELETE /api/contas/{id}
     */
    public void deletarConta(String id) {
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada com id: " + id));

        if (conta.getNome().equalsIgnoreCase("Carteira")) {
            throw new RuntimeException("Não é permitido deletar a conta 'Carteira'.");
        }

        if (conta.getTipoConta() == TipoConta.CONTA_META) {
            throw new RuntimeException("Use a API de Metas para gerenciar esta conta.");
        }

        contaRepository.delete(conta);
    }
}