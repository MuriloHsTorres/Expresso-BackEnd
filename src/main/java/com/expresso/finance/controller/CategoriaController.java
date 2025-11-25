package com.expresso.finance.controller;

import com.expresso.finance.dto.request.CategoriaRequest;
import com.expresso.finance.dto.response.CategoriaResponse;
import com.expresso.finance.exception.ResourceNotFoundException;
import com.expresso.finance.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias") // URL base para este controller
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    /**
     * Endpoint: POST /api/categorias
     * Cria uma nova categoria personalizada (Ex: Academia)
     */
    @PostMapping
    public ResponseEntity<?> criarCategoria(@RequestBody CategoriaRequest request) {
        try {
            CategoriaResponse response = categoriaService.criarCategoria(request);
            return ResponseEntity.status(201).body(response); // 201 Created
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint: GET /api/categorias/cliente/{clienteId}
     * Lista todas as categorias (padrão + personalizadas) de um cliente
     */
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<CategoriaResponse>> listarCategoriasPorCliente(@PathVariable String clienteId) {
        // Se o cliente não existir, o serviço retornará uma lista vazia,
        // o que está correto (não precisa de try-catch aqui).
        List<CategoriaResponse> categorias = categoriaService.listarCategoriasPorCliente(clienteId);
        return ResponseEntity.ok(categorias);
    }

    /**
     * Endpoint: PUT /api/categorias/{id}
     * Atualiza o nome de uma categoria personalizada
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarCategoria(@PathVariable String id, @RequestBody CategoriaRequest request) {
        try {
            CategoriaResponse response = categoriaService.atualizarCategoria(id, request);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (RuntimeException e) {
            // Captura a exceção de "Não pode atualizar categoria padrão"
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint: DELETE /api/categorias/{id}
     * Deleta uma categoria personalizada
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarCategoria(@PathVariable String id) {
        try {
            categoriaService.deletarCategoria(id);
            // Retorna 204 No Content (sucesso, sem corpo de resposta)
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (RuntimeException e) {
            // Captura a exceção de "Não pode deletar categoria padrão"
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}