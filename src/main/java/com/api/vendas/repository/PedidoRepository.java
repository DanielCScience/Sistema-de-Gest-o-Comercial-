package com.api.vendas.repository;

import com.api.vendas.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // RF15: Gráfico de Vendas Anuais
    @Query("SELECT FUNCTION('MONTH', p.data) as mes, SUM(p.valor) as total FROM Pedido p WHERE FUNCTION('YEAR', p.data) = FUNCTION('YEAR', CURRENT_DATE) AND p.status = 'CONCLUIDO' GROUP BY FUNCTION('MONTH', p.data)")
    List<Map<String, Object>> findVendasAnuaisAgrupadasPorMes();

    // RF16: Relatório de Vendas por Período
    @Query("SELECT new map(SUM(p.valor) as volumeFinanceiro, COUNT(p) as quantidadePedidos) FROM Pedido p WHERE p.data BETWEEN :inicio AND :fim AND p.status = 'CONCLUIDO'")
    Map<String, Object> findRelatorioVendasPorPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    // RF17: Histórico de Compras por Cliente
    @Query("SELECT p FROM Pedido p WHERE p.cliente.idcliente = :clienteId ORDER BY p.data DESC")
    List<Pedido> findHistoricoByClienteId(@Param("clienteId") Long clienteId);

    // Método auxiliar para ver se o cliente tem pedidos
    boolean existsByCliente_Idcliente(Long clienteId);
}
