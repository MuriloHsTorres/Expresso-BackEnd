package com.expresso.finance.controller;

import com.expresso.finance.dto.request.TransferenciaRequest;
import com.expresso.finance.dto.response.TransferenciaResponse;
import com.expresso.finance.exception.ResourceNotFoundException;
import com.expresso.finance.service.TransferenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transferencias") // URL base para este controller
public class TransferenciaController {

    @Autowired
    private TransferenciaService transferenciaService;

    /**
     * Endpoint: POST /api/transferencias
     * Executa uma transferência entre duas contas (seja para outra conta
     * ou para uma "Conta-Meta").
     */
    @PostMapping
    public ResponseEntity<?> executarTransferencia(@RequestBody TransferenciaRequest request) {
        try {
            TransferenciaResponse response = transferenciaService.executarTransferencia(request);
            return ResponseEntity.status(201).body(response); // 201 Created
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (RuntimeException e) {
            // Captura erros como "Saldo insuficiente"
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    /**
     * Endpoint: GET /api/transferencias/cliente/{clienteId}
     * Lista o histórico de "recibos" de transferências do cliente.
     */
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<TransferenciaResponse>> listarTransferenciasPorCliente(@PathVariable String clienteId) {
        List<TransferenciaResponse> transferencias = transferenciaService.listarTransferenciasPorCliente(clienteId);
        return ResponseEntity.ok(transferencias);
    }
}