package com.api.vendas.controller;

import com.api.vendas.model.Pedido;
import com.api.vendas.service.RelatorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/relatorios")
@RequiredArgsConstructor
public class RelatorioController {

    private final RelatorioService relatorioService;

    // RF18: Limitar Acesso Restrito (Somente ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/vendas-anuais")
    public ResponseEntity<List<Map<String, Object>>> graficoVendasAnuais() {
        return ResponseEntity.ok(relatorioService.gerarGraficoVendasAnuais());
    }

    @GetMapping("/vendas-periodo")
    public ResponseEntity<Map<String, Object>> relatorioVendasPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        return ResponseEntity.ok(relatorioService.gerarRelatorioVendasPorPeriodo(inicio, fim));
    }

    @GetMapping("/historico-cliente/{clienteId}")
    public ResponseEntity<List<Pedido>> historicoComprasCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(relatorioService.gerarHistoricoPorCliente(clienteId));
    }
}
