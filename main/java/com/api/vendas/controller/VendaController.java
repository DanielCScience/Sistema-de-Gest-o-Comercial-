package com.api.vendas.controller;

import com.api.vendas.model.Venda;
import com.api.vendas.service.VendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vendas")
@RequiredArgsConstructor
public class VendaController {

    private final VendaService vendaService;

    @PostMapping("/faturar/{pedidoId}")
    public ResponseEntity<Venda> realizarVenda(@PathVariable Long pedidoId, @RequestParam String formaPagamento) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vendaService.realizarVenda(pedidoId, formaPagamento));
    }
}
