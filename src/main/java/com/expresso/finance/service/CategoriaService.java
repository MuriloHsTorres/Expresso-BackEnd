package com.expresso.finance.service;

import com.expresso.finance.dto.request.CategoriaRequest;
import com.expresso.finance.dto.response.CategoriaResponse;
import com.expresso.finance.entity.Categoria;
import com.expresso.finance.entity.Cliente;
import com.expresso.finance.exception.ResourceNotFoundException;
import com.expresso.finance.repository.CategoriaRepository;
import com.expresso.finance.repository.ClienteRepository;
import com.expresso.finance.repository.TransacaoRepository; // 1. IMPORTAR
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional; // Importar se não estiver
import java.util.stream.Collectors;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    // 2. INJETAR O TRANSACAO REPOSITORY
    @Autowired
    private TransacaoRepository transacaoRepository;

    // REGRA DE NEGÓCIO: Lista de categorias padrão
    private static final List<String> CATEGORIAS_PADRAO_NOMES = Arrays.asList(
            "Salário", "Alimentação", "Transporte", "Transferências"
    );

    // ... (método criarCategoriasPadrao) ...
    public void criarCategoriasPadrao(Cliente cliente) {
        // (Seu código existente aqui, sem mudança)
        CATEGORIAS_PADRAO_NOMES.forEach(nome -> {
            Categoria categoria = new Categoria();
            categoria.setNome(nome);
            categoria.setCliente(cliente);
            categoriaRepository.save(categoria);
        });
    }


    /**
     * Endpoint: POST /api/categorias
     * (Com a correção de duplicados do Passo 116)
     */
    public CategoriaResponse criarCategoria(CategoriaRequest request) {
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id: " + request.getClienteId()));

        // Verificação de duplicados
        Optional<Categoria> categoriaExistente = categoriaRepository
                .findByNomeAndClienteId(request.getNome(), request.getClienteId());

        if (categoriaExistente.isPresent()) {
            throw new RuntimeException("Categoria '" + request.getNome() + "' já existe.");
        }

        Categoria categoria = new Categoria();
        categoria.setNome(request.getNome());
        categoria.setCliente(cliente);

        Categoria categoriaSalva = categoriaRepository.save(categoria);
        return new CategoriaResponse(categoriaSalva);
    }

    // ... (método listarCategoriasPorCliente) ...
    public List<CategoriaResponse> listarCategoriasPorCliente(String clienteId) {
        // (Seu código existente aqui, sem mudança)
        return categoriaRepository.findAllByClienteId(clienteId)
                .stream()
                .map(CategoriaResponse::new)
                .collect(Collectors.toList());
    }


    /**
     * Endpoint: PUT /api/categorias/{id}
     * ATUALIZADO com a nova regra
     */
    public CategoriaResponse atualizarCategoria(String id, CategoriaRequest request) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com id: " + id));

        // REGRA 1: Não permitir alterar categorias padrão
        if (isCategoriaPadrao(categoria.getNome())) {
            throw new RuntimeException("Não é permitido atualizar uma categoria padrão.");
        }

        // 3. NOVA REGRA: Não permitir alterar categorias EM USO
        boolean emUso = transacaoRepository.existsByCategoriaId(id);
        if (emUso) {
            throw new RuntimeException("Não é permitido atualizar uma categoria que já está em uso por transações.");
        }
        // FIM DA NOVA REGRA

        categoria.setNome(request.getNome());
        Categoria categoriaAtualizada = categoriaRepository.save(categoria);
        return new CategoriaResponse(categoriaAtualizada);
    }

    /**
     * Endpoint: DELETE /api/categorias/{id}
     * ATUALIZADO com a nova regra
     */
    public void deletarCategoria(String id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com id: " + id));

        // REGRA 1: Não permitir deletar categorias padrão
        if (isCategoriaPadrao(categoria.getNome())) {
            throw new RuntimeException("Não é permitido deletar uma categoria padrão.");
        }

        // 4. NOVA REGRA: Não permitir deletar categorias EM USO
        boolean emUso = transacaoRepository.existsByCategoriaId(id);
        if (emUso) {
            throw new RuntimeException("Não é permitido deletar uma categoria que já está em uso por transações.");
        }
        // FIM DA NOVA REGRA

        categoriaRepository.delete(categoria);
    }

    /**
     * Método auxiliar privado
     */
    private boolean isCategoriaPadrao(String nomeCategoria) {
        return CATEGORIAS_PADRAO_NOMES.stream()
                .anyMatch(nomePadrao -> nomePadrao.equalsIgnoreCase(nomeCategoria));
    }
}