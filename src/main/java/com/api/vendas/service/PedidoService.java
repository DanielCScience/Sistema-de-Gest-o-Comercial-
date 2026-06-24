package com.api.vendas.service;

import com.api.vendas.model.*;
import com.api.vendas.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoService produtoService;
    private final ClienteService clienteService;

    @Transactional
    public Pedido registrarPedido(Pedido pedido) {
        // RF11: Data e Hora
        pedido.setData(LocalDateTime.now());
        pedido.setStatus(StatusPedido.PENDENTE);


        BigDecimal total = BigDecimal.ZERO;
        int qtdeTotal = 0;

        for (ItemPedido item : pedido.getItens()) {
            Produto produto = produtoService.buscarPorId(item.getProduto().getIdproduto());
            
            // Abater do estoque
            if (produto.getQuantidade() < item.getQuantidade()) {
                throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNome());
            }
            produto.setQuantidade(produto.getQuantidade() - item.getQuantidade());
            produtoService.cadastrar(produto); // atualiza o estoque

            item.setProduto(produto);
            item.setPedido(pedido);
            item.setValorUnitario(produto.getPreco());
            
            // RF10: Calcular valor
            BigDecimal subtotal = produto.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade()));
            total = total.add(subtotal);
            qtdeTotal += item.getQuantidade();
        }

        pedido.setValor(total);
        pedido.setQuantidade(qtdeTotal);

        return pedidoRepository.save(pedido);
    }

    public Pedido visualizarPedido(Long id) {
        return pedidoRepository.findById(id).orElseThrow(() -> new RuntimeException("Pedido não encontrado."));
    }

    @Transactional
    public Pedido alterarPedido(Long id, Pedido dadosNovos) {
        Pedido pedido = visualizarPedido(id);

        if (pedido.getStatus() == StatusPedido.CONCLUIDO || pedido.getStatus() == StatusPedido.CANCELADO) {
            throw new RuntimeException("Não é possível alterar um pedido finalizado ou cancelado.");
        }


        pedido.setStatus(dadosNovos.getStatus());
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public void cancelarPedido(Long id) {
        Pedido pedido = visualizarPedido(id);

        if (pedido.getStatus() == StatusPedido.CANCELADO) {
            throw new RuntimeException("Pedido já está cancelado.");
        }

        // RF14: Estornar estoque
        for (ItemPedido item : pedido.getItens()) {
            Produto produto = item.getProduto();
            produto.setQuantidade(produto.getQuantidade() + item.getQuantidade());
            produtoService.cadastrar(produto); // Salva as alterações
        }

        pedido.setStatus(StatusPedido.CANCELADO);
        pedidoRepository.save(pedido);
    }
}
