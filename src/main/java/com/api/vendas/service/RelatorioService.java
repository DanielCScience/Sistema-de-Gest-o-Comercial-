package com.api.vendas.service;

import com.api.vendas.model.Pedido;
import com.api.vendas.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RelatorioService {

    private final PedidoRepository pedidoRepository;

    public List<Map<String, Object>> gerarGraficoVendasAnuais() {
        return pedidoRepository.findVendasAnuaisAgrupadasPorMes();
    }

    public Map<String, Object> gerarRelatorioVendasPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return pedidoRepository.findRelatorioVendasPorPeriodo(inicio, fim);
    }

    public List<Pedido> gerarHistoricoPorCliente(Long clienteId) {
        return pedidoRepository.findHistoricoByClienteId(clienteId);
    }
}
