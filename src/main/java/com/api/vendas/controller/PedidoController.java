package com.api.vendas.controller;

import com.api.vendas.model.Pedido;
import com.api.vendas.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<Pedido> registrarPedido(@RequestBody Pedido pedido) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.registrarPedido(pedido));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> visualizarPedido(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.visualizarPedido(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> alterarPedido(@PathVariable Long id, @RequestBody Pedido pedido) {
        return ResponseEntity.ok(pedidoService.alterarPedido(id, pedido));
    }

    @DeleteMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarPedido(@PathVariable Long id) {
        pedidoService.cancelarPedido(id);
        return ResponseEntity.noContent().build();
    }
}
