package com.api.vendas.service;

import com.api.vendas.model.Cliente;
import com.api.vendas.repository.ClienteRepository;
import com.api.vendas.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PedidoRepository pedidoRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Cliente cadastrar(Cliente cliente) {
        cliente.setSenha(passwordEncoder.encode(cliente.getSenha()));
        return clienteRepository.save(cliente);
    }

    public List<Cliente> buscarTodos() {
        return clienteRepository.findAll();
    }

    public List<Cliente> buscarPorNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome);
    }

    public Cliente buscarPorCpf(String cpf) {
        return clienteRepository.findByCpf(cpf).orElseThrow(() -> new RuntimeException("Cliente não encontrado com o CPF: " + cpf));
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Cliente não encontrado."));
    }

    @Transactional
    public Cliente alterar(Long id, Cliente dadosAtualizados) {
        Cliente cliente = buscarPorId(id);
        
        // RF03: Impedir alteração de CPF
        if (!cliente.getCpf().equals(dadosAtualizados.getCpf())) {
            throw new RuntimeException("Não é permitido alterar o CPF do cliente.");
        }

        cliente.setNome(dadosAtualizados.getNome());
        cliente.setEmail(dadosAtualizados.getEmail());
        cliente.setTelefone(dadosAtualizados.getTelefone());
        cliente.setEndereco(dadosAtualizados.getEndereco());

        // Se a senha foi preenchida, atualizamos com hash
        if (dadosAtualizados.getSenha() != null && !dadosAtualizados.getSenha().isEmpty()) {
            cliente.setSenha(passwordEncoder.encode(dadosAtualizados.getSenha()));
        }

        return clienteRepository.save(cliente);
    }

    @Transactional
    public void excluir(Long id) {
        Cliente cliente = buscarPorId(id);

        // RF04: Impedir exclusão se houver pedidos
        if (pedidoRepository.existsByCliente_Idcliente(id)) {
            throw new RuntimeException("Não é possível excluir o cliente pois ele possui histórico de pedidos.");
        }

        clienteRepository.delete(cliente);
    }
}
