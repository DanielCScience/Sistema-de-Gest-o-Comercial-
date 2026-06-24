package com.api.vendas.ui.panels;

import com.api.vendas.model.Cliente;
import com.api.vendas.model.Funcionario;
import com.api.vendas.model.Pedido;
import com.api.vendas.model.StatusPedido;
import com.api.vendas.ui.ApiClient;
import com.fasterxml.jackson.core.type.TypeReference;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PedidoPanel extends JPanel {
    private final ApiClient apiClient;
    private JTable table;
    private DefaultTableModel tableModel;

    public PedidoPanel(ApiClient apiClient) {
        this.apiClient = apiClient;
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"ID", "Data", "Valor", "Qtd", "Status", "Cliente ID", "Func. ID"}, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton btnCarregar = new JButton("Carregar");
        JButton btnNovo = new JButton("Novo");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");

        buttonPanel.add(btnCarregar);
        buttonPanel.add(btnNovo);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnExcluir);
        add(buttonPanel, BorderLayout.SOUTH);

        btnCarregar.addActionListener(e -> carregarDados());
        btnNovo.addActionListener(e -> showFormDialog(null));
        btnEditar.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                Long id = (Long) tableModel.getValueAt(row, 0);
                showFormDialog(id);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um pedido");
            }
        });
        btnExcluir.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                Long id = (Long) tableModel.getValueAt(row, 0);
                try {
                    apiClient.delete("/pedidos/" + id);
                    JOptionPane.showMessageDialog(this, "Pedido excluído!");
                    carregarDados();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um pedido");
            }
        });

        carregarDados();
    }

    private void carregarDados() {
        try {
            List<Pedido> pedidos = apiClient.get("/pedidos", new TypeReference<List<Pedido>>() {});
            tableModel.setRowCount(0);
            for (Pedido p : pedidos) {
                tableModel.addRow(new Object[]{
                    p.getIdpedido(), 
                    p.getData(), 
                    p.getValor(), 
                    p.getQuantidade(), 
                    p.getStatus(),
                    p.getCliente() != null ? p.getCliente().getIdcliente() : "",
                    p.getFuncionario() != null ? p.getFuncionario().getIdfuncionario() : ""
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar pedidos: " + ex.getMessage());
        }
    }

    private void showFormDialog(Long id) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), id == null ? "Novo Pedido" : "Editar Pedido", true);
        dialog.setSize(300, 300);
        dialog.setLayout(new GridLayout(6, 2, 5, 5));
        dialog.setLocationRelativeTo(this);

        JTextField valorField = new JTextField();
        JTextField qtdField = new JTextField();
        JComboBox<StatusPedido> statusCombo = new JComboBox<>(StatusPedido.values());
        JTextField clienteIdField = new JTextField();
        JTextField funcIdField = new JTextField();

        dialog.add(new JLabel("Valor Total:")); dialog.add(valorField);
        dialog.add(new JLabel("Quantidade:")); dialog.add(qtdField);
        dialog.add(new JLabel("Status:")); dialog.add(statusCombo);
        dialog.add(new JLabel("Cliente ID:")); dialog.add(clienteIdField);
        dialog.add(new JLabel("Funcionário ID:")); dialog.add(funcIdField);

        JButton saveBtn = new JButton("Salvar");
        saveBtn.addActionListener(e -> {
            Pedido p = new Pedido();
            p.setValor(new BigDecimal(valorField.getText()));
            p.setQuantidade(Integer.parseInt(qtdField.getText()));
            p.setStatus((StatusPedido) statusCombo.getSelectedItem());
            p.setData(LocalDateTime.now());
            
            Cliente c = new Cliente();
            c.setIdcliente(Long.parseLong(clienteIdField.getText()));
            p.setCliente(c);

            Funcionario f = new Funcionario();
            f.setIdfuncionario(Long.parseLong(funcIdField.getText()));
            p.setFuncionario(f);

            try {
                if (id == null) {
                    apiClient.post("/pedidos", p, new TypeReference<Pedido>() {});
                } else {
                    apiClient.put("/pedidos/" + id, p, new TypeReference<Pedido>() {});
                }
                dialog.dispose();
                carregarDados();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erro: " + ex.getMessage());
            }
        });

        dialog.add(new JLabel("")); // Spacer
        dialog.add(saveBtn);
        dialog.setVisible(true);
    }
}
