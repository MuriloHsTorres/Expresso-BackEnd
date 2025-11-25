package com.expresso.finance.service;

// 1. VERIFIQUE SE ESTES IMPORTS ESTÃO CORRETOS
import com.expresso.finance.dto.request.ClienteRequest;
import com.expresso.finance.dto.request.LoginRequest;
import com.expresso.finance.dto.response.ClienteResponse;
import com.expresso.finance.entity.Cliente;
import com.expresso.finance.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.expresso.finance.dto.request.DeleteAccountRequest;


import com.expresso.finance.dto.request.PerfilUpdateRequest;

import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ContaService contaService;

    @Autowired
    private CategoriaService categoriaService;

    // 2. ADICIONAR A INJEÇÃO QUE FALTAVA
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private TransacaoRepository transacaoRepository;
    @Autowired
    private MetaRepository metaRepository;
    @Autowired
    private ContaRepository contaRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private TransferenciaRepository transferenciaRepository;

    /**
     * Lógica de Cadastro (POST /api/clientes/cadastro)
     * MÉTODO ATUALIZADO PARA USAR BCRYPT
     */
    @Transactional
    public ClienteResponse cadastrar(ClienteRequest request) { // MUDANÇA: Retorna ClienteResponse

        // 1. Validar se o email já existe
        if (clienteRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("E-mail já cadastrado.");
        }

        // 2. Criar a nova entidade Cliente
        Cliente cliente = new Cliente();
        cliente.setNome(request.getNome());
        cliente.setEmail(request.getEmail());

        // 3. MUDANÇA: Salvar a senha CRIPTOGRAFADA
        cliente.setSenha(bCryptPasswordEncoder.encode(request.getSenha()));
        // O @PrePersist na entidade 'Cliente' vai cuidar da 'dataCriacao'

        // 4. Salvar o cliente no banco
        Cliente clienteSalvo = clienteRepository.save(cliente);

        // 5. Executar as Regras de Negócio Pós-Cadastro
        contaService.criarCarteiraPadrao(clienteSalvo);
        categoriaService.criarCategoriasPadrao(clienteSalvo);

        // 6. MUDANÇA: Retornar o DTO de Resposta (sem a senha)
        return new ClienteResponse(clienteSalvo);
    }

    /**
     * Lógica de Login (POST /api/clientes/login)
     * MÉTODO ATUALIZADO PARA USAR BCRYPT
     */
    public ClienteResponse login(LoginRequest request) {

        // 1. Buscar o cliente pelo email
        Cliente cliente = clienteRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email ou senha inválidos."));

        // 2. MUDANÇA: Verificar a senha (CRIPTOGRAFADA)
        // bCryptPasswordEncoder.matches(senha_digitada, senha_salva_no_banco)
        if (!bCryptPasswordEncoder.matches(request.getSenha(), cliente.getSenha())) {
            throw new RuntimeException("Email ou senha inválidos.");
        }

        // 3. Se deu tudo certo, retornar o DTO de Resposta
        return new ClienteResponse(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getDataCriacao()
        );
    }

    /**
     * Lógica de Atualização (PUT /api/clientes/{id})
     * MÉTODO NOVO (AGORA FUNCIONAL)
     */
    @Transactional
    public ClienteResponse atualizarCliente(String id, PerfilUpdateRequest request) {
        // 1. Encontrar o cliente existente
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + id));

        // 2. MUDANÇA: VALIDAR A SENHA ATUAL
        // Verifica se a 'senhaAtual' enviada bate com a senha no banco
        if (!bCryptPasswordEncoder.matches(request.getSenhaAtual(), cliente.getSenha())) {
            throw new RuntimeException("Senha atual incorreta.");
        }

        // 3. Se a senha está correta, prosseguir com as atualizações

        // 4. Verificar e-mail
        if (request.getEmail() != null && !request.getEmail().equals(cliente.getEmail())) {
            Optional<Cliente> clienteExistenteComEmail = clienteRepository.findByEmail(request.getEmail());
            if (clienteExistenteComEmail.isPresent()) {
                throw new RuntimeException("E-mail já está em uso por outra conta.");
            }
            cliente.setEmail(request.getEmail());
        }

        // 5. Atualizar nome
        if (request.getNome() != null) {
            cliente.setNome(request.getNome());
        }

        // 6. Lidar com a NOVA senha (opcional)
        if (request.getNovaSenha() != null && !request.getNovaSenha().isEmpty()) {
            cliente.setSenha(bCryptPasswordEncoder.encode(request.getNovaSenha()));
        }

        // 7. Salvar as alterações
        Cliente clienteAtualizado = clienteRepository.save(cliente);

        // 8. Retornar a resposta (sem a senha)
        return new ClienteResponse(clienteAtualizado);
    }

    @Transactional
    public void deletarCliente(String id, DeleteAccountRequest request) {
        // 1. Encontrar o cliente
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        // 2. VALIDAR A SENHA (A regra de segurança)
        if (!bCryptPasswordEncoder.matches(request.getSenhaAtual(), cliente.getSenha())) {
            throw new RuntimeException("Senha atual incorreta. Exclusão cancelada.");
        }

        // 3. DELEÇÃO EM CASCATA (Ordem de "baixo para cima")

        // (Assumindo que Transferencias são deletadas com Transacoes, se não, delete-as primeiro)

        // 3.1. Deleta Transferencias (filho de Transacao)
        transferenciaRepository.deleteAllByClienteId(id);

        // Deleta Transações (dependem de Contas e Categorias)
        transacaoRepository.deleteAllByClienteId(id);

        // Deleta Metas (dependem de Contas)
        metaRepository.deleteAllByClienteId(id);

        // Deleta Contas (dependem do Cliente)
        contaRepository.deleteAllByClienteId(id);

        // Deleta Categorias (dependem do Cliente)
        categoriaRepository.deleteAllByClienteId(id);

        // 4. DELETA O CLIENTE (Agora que ele não tem mais "filhos")
        clienteRepository.delete(cliente);
    }
}