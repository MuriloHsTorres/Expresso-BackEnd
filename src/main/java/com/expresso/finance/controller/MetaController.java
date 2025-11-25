package com.expresso.finance.controller;

import com.expresso.finance.dto.request.MetaRequest;
import com.expresso.finance.dto.response.MetaResponse;
import com.expresso.finance.exception.ResourceNotFoundException;
import com.expresso.finance.service.MetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/metas") // URL base para este controller
public class MetaController {

    @Autowired
    private MetaService metaService;

    /**
     * Endpoint: POST /api/metas
     * Cria uma nova meta financeira (E sua conta-cofrinho)
     */
    @PostMapping
    public ResponseEntity<?> criarMeta(@RequestBody MetaRequest request) {
        try {
            MetaResponse response = metaService.criarMeta(request);
            return ResponseEntity.status(201).body(response); // 201 Created
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint: GET /api/metas/cliente/{clienteId}
     * Lista todas as metas de um cliente
     */
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<MetaResponse>> listarMetasPorCliente(@PathVariable String clienteId) {
        List<MetaResponse> metas = metaService.listarMetasPorCliente(clienteId);
        return ResponseEntity.ok(metas);
    }

    /**
     * Endpoint: PUT /api/metas/{id}
     * Atualiza os dados principais de uma meta (nome, valor-alvo, data-alvo)
     * (E também o nome da conta-cofrinho associada)
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarMeta(@PathVariable String id, @RequestBody MetaRequest request) {
        try {
            MetaResponse response = metaService.atualizarMeta(id, request);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint: PUT /api/metas/{id}/valor - REMOVIDO
     * Esta funcionalidade será gerida pelo Módulo de Transferências
     */
    // O método/endpoint foi apagado.

    /**
     * Endpoint: DELETE /api/metas/{id}
     * Deleta uma meta (E sua conta-cofrinho associada)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarMeta(@PathVariable String id) {
        try {
            metaService.deletarMeta(id);
            // Retorna 204 No Content (sucesso, sem corpo de resposta)
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}