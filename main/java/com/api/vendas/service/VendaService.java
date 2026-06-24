package com.api.vendas.service;

import com.api.vendas.model.Pedido;
import com.api.vendas.model.StatusPedido;
import com.api.vendas.model.Venda;
import com.api.vendas.repository.VendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VendaService {

    private final VendaRepository vendaRepository;
    private final PedidoService pedidoService;

    @Transactional
    public Venda realizarVenda(Long pedidoId, String formaPagamento) {
        Pedido pedido = pedidoService.visualizarPedido(pedidoId);

        if (pedido.getStatus() != StatusPedido.PENDENTE && pedido.getStatus() != StatusPedido.EM_ANDAMENTO) {
            throw new RuntimeException("Não é possível faturar este pedido. Status atual: " + pedido.getStatus());
        }

        pedido.setStatus(StatusPedido.CONCLUIDO);

        Venda venda = new Venda();
        venda.setPedido(pedido);
        venda.setData(LocalDateTime.now());
        venda.setValor(pedido.getValor());
        venda.setFormapagamento(formaPagamento);

        return vendaRepository.save(venda);
    }
}
