package com.expresso.finance.controller;

import com.expresso.finance.dto.request.TransacaoRequest;
import com.expresso.finance.dto.response.TransacaoResponse;
import com.expresso.finance.exception.ResourceNotFoundException;
import com.expresso.finance.service.TransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transacoes") // URL base para este controller
public class TransacaoController {

    @Autowired
    private TransacaoService transacaoService;

    /**
     * Endpoint: POST /api/transacoes
     * Regista uma nova receita ou despesa
     */
    @PostMapping
    public ResponseEntity<?> criarTransacao(@RequestBody TransacaoRequest request) {
        try {
            TransacaoResponse response = transacaoService.criarTransacao(request);
            return ResponseEntity.status(201).body(response); // 201 Created
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint: GET /api/transacoes/cliente/{clienteId}
     * Lista todas as transações de um cliente
     */
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<TransacaoResponse>> listarTransacoesPorCliente(@PathVariable String clienteId) {
        List<TransacaoResponse> transacoes = transacaoService.listarTransacoesPorCliente(clienteId);
        return ResponseEntity.ok(transacoes);
    }

    /**
     * Endpoint: GET /api/transacoes/conta/{contaId}
     * Lista todas as transações de uma conta (Extrato)
     */
    @GetMapping("/conta/{contaId}")
    public ResponseEntity<List<TransacaoResponse>> listarTransacoesPorConta(@PathVariable String contaId) {
        List<TransacaoResponse> transacoes = transacaoService.listarTransacoesPorConta(contaId);
        return ResponseEntity.ok(transacoes);
    }

    /**
     * Endpoint: PUT /api/transacoes/{id}
     * Atualiza uma transação (valor, descrição, etc)
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarTransacao(@PathVariable String id, @RequestBody TransacaoRequest request) {
        try {
            TransacaoResponse response = transacaoService.atualizarTransacao(id, request);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint: DELETE /api/transacoes/{id}
     * Deleta uma transação
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarTransacao(@PathVariable String id) {
        try {
            transacaoService.deletarTransacao(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (RuntimeException e) { // <-- 1. ADICIONAR ESTE CATCH
            // Isto vai capturar "Transações de 'Transferências'..."
            // e enviar APENAS a string da mensagem
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}