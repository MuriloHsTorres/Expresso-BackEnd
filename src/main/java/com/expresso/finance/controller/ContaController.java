package com.expresso.finance.controller;

import com.expresso.finance.dto.request.ContaRequest;
import com.expresso.finance.dto.response.ContaResponse;
import com.expresso.finance.exception.ResourceNotFoundException;
import com.expresso.finance.service.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contas") // URL base para este controller
public class ContaController {

    @Autowired
    private ContaService contaService;

    /**
     * Endpoint: POST /api/contas
     * Cria uma nova conta bancária (Nubank, Inter, etc)
     */
    @PostMapping
    public ResponseEntity<?> criarConta(@RequestBody ContaRequest request) {
        try {
            ContaResponse response = contaService.criarConta(request);
            return ResponseEntity.status(201).body(response); // 201 Created
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint: GET /api/contas/cliente/{clienteId}
     * Lista todas as contas de um cliente
     */
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<ContaResponse>> listarContasPorCliente(@PathVariable String clienteId) {
        // Este método não precisa de try-catch, se o cliente não existir
        // ele apenas retornará uma lista vazia, o que está correto.
        List<ContaResponse> contas = contaService.listarContasPorCliente(clienteId);
        return ResponseEntity.ok(contas);
    }

    /**
     * Endpoint: GET /api/contas/{id}
     * Busca uma conta específica pelo seu próprio ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarContaPorId(@PathVariable String id) {
        try {
            ContaResponse response = contaService.buscarContaPorId(id);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    /**
     * Endpoint: PUT /api/contas/{id}
     * Atualiza o nome de uma conta
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarConta(@PathVariable String id, @RequestBody ContaRequest request) {
        try {
            ContaResponse response = contaService.atualizarConta(id, request);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint: DELETE /api/contas/{id}
     * Deleta uma conta
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarConta(@PathVariable String id) {
        try {
            contaService.deletarConta(id);
            // Retorna 204 No Content (sucesso, sem corpo de resposta)
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (RuntimeException e) {
            // Captura a exceção de "Não pode deletar Carteira"
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}