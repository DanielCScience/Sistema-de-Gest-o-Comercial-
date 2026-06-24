package com.api.vendas.ui.panels;

import com.api.vendas.model.Produto;
import com.api.vendas.ui.ApiClient;
import com.fasterxml.jackson.core.type.TypeReference;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class ProdutoPanel extends JPanel {
    private final ApiClient apiClient;
    private JTable table;
    private DefaultTableModel tableModel;

    public ProdutoPanel(ApiClient apiClient) {
        this.apiClient = apiClient;
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"ID", "Nome", "Descrição", "Preço", "Quantidade"}, 0);
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
                JOptionPane.showMessageDialog(this, "Selecione um produto");
            }
        });
        btnExcluir.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                Long id = (Long) tableModel.getValueAt(row, 0);
                try {
                    apiClient.delete("/produtos/" + id);
                    JOptionPane.showMessageDialog(this, "Produto excluído!");
                    carregarDados();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um produto");
            }
        });

        carregarDados();
    }

    private void carregarDados() {
        try {
            List<Produto> produtos = apiClient.get("/produtos", new TypeReference<List<Produto>>() {});
            tableModel.setRowCount(0);
            for (Produto p : produtos) {
                tableModel.addRow(new Object[]{p.getIdproduto(), p.getNome(), p.getDescricao(), p.getPreco(), p.getQuantidade()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar produtos: " + ex.getMessage());
        }
    }

    private void showFormDialog(Long id) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), id == null ? "Novo Produto" : "Editar Produto", true);
        dialog.setSize(300, 250);
        dialog.setLayout(new GridLayout(5, 2, 5, 5));
        dialog.setLocationRelativeTo(this);

        JTextField nomeField = new JTextField();
        JTextField descField = new JTextField();
        JTextField precoField = new JTextField();
        JTextField qtdField = new JTextField();

        dialog.add(new JLabel("Nome:")); dialog.add(nomeField);
        dialog.add(new JLabel("Descrição:")); dialog.add(descField);
        dialog.add(new JLabel("Preço:")); dialog.add(precoField);
        dialog.add(new JLabel("Quantidade:")); dialog.add(qtdField);

        JButton saveBtn = new JButton("Salvar");
        saveBtn.addActionListener(e -> {
            Produto p = new Produto();
            p.setNome(nomeField.getText());
            p.setDescricao(descField.getText());
            p.setPreco(new BigDecimal(precoField.getText()));
            p.setQuantidade(Integer.parseInt(qtdField.getText()));

            try {
                if (id == null) {
                    apiClient.post("/produtos", p, new TypeReference<Produto>() {});
                } else {
                    apiClient.put("/produtos/" + id, p, new TypeReference<Produto>() {});
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
