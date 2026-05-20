package com.api.vendas.controller;

import com.api.vendas.model.Cliente;
import com.api.vendas.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<Cliente> cadastrar(@RequestBody Cliente cliente) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.cadastrar(cliente));
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listarTodos(@RequestParam(required = false) String nome, @RequestParam(required = false) String cpf) {
        if (nome != null) {
            return ResponseEntity.ok(clienteService.buscarPorNome(nome));
        } else if (cpf != null) {
            return ResponseEntity.ok(List.of(clienteService.buscarPorCpf(cpf)));
        }
        return ResponseEntity.ok(clienteService.buscarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> alterar(@PathVariable Long id, @RequestBody Cliente cliente) {
        return ResponseEntity.ok(clienteService.alterar(id, cliente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        clienteService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
