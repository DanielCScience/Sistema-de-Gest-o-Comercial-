package com.api.vendas.ui.panels;

import com.api.vendas.model.Cliente;
import com.api.vendas.ui.ApiClient;
import com.fasterxml.jackson.core.type.TypeReference;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ClientePanel extends JPanel {
    private final ApiClient apiClient;
    private JTable table;
    private DefaultTableModel tableModel;

    public ClientePanel(ApiClient apiClient) {
        this.apiClient = apiClient;
        setLayout(new BorderLayout());

        // Configuração da Tabela
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nome", "CPF", "Email", "Telefone"}, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Painel de Botões
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

        // Ações dos Botões
        btnCarregar.addActionListener(e -> carregarDados());
        btnNovo.addActionListener(e -> showFormDialog(null));
        btnEditar.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                Long id = (Long) tableModel.getValueAt(row, 0);
                showFormDialog(id); // Para uma implementação real, buscaria pelo ID
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um cliente para editar");
            }
        });
        btnExcluir.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                Long id = (Long) tableModel.getValueAt(row, 0);
                try {
                    apiClient.delete("/clientes/" + id);
                    JOptionPane.showMessageDialog(this, "Cliente excluído!");
                    carregarDados();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um cliente para excluir");
            }
        });

        // Carrega dados iniciais
        carregarDados();
    }

    private void carregarDados() {
        try {
            List<Cliente> clientes = apiClient.get("/clientes", new TypeReference<List<Cliente>>() {});
            tableModel.setRowCount(0); // Limpa tabela
            for (Cliente c : clientes) {
                tableModel.addRow(new Object[]{c.getIdcliente(), c.getNome(), c.getCpf(), c.getEmail(), c.getTelefone()});
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar clientes: " + ex.getMessage());
        }
    }

    private void showFormDialog(Long id) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), id == null ? "Novo Cliente" : "Editar Cliente", true);
        dialog.setSize(300, 300);
        dialog.setLayout(new GridLayout(6, 2, 5, 5));
        dialog.setLocationRelativeTo(this);

        JTextField nomeField = new JTextField();
        JTextField cpfField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField senhaField = new JTextField();
        JTextField telefoneField = new JTextField();

        dialog.add(new JLabel("Nome:")); dialog.add(nomeField);
        dialog.add(new JLabel("CPF:")); dialog.add(cpfField);
        dialog.add(new JLabel("Email:")); dialog.add(emailField);
        dialog.add(new JLabel("Senha:")); dialog.add(senhaField);
        dialog.add(new JLabel("Telefone:")); dialog.add(telefoneField);

        JButton saveBtn = new JButton("Salvar");
        saveBtn.addActionListener(e -> {
            Cliente c = new Cliente();
            c.setNome(nomeField.getText());
            c.setCpf(cpfField.getText());
            c.setEmail(emailField.getText());
            c.setSenha(senhaField.getText());
            c.setTelefone(telefoneField.getText());

            try {
                if (id == null) {
                    apiClient.post("/clientes", c, new TypeReference<Cliente>() {});
                } else {
                    apiClient.put("/clientes/" + id, c, new TypeReference<Cliente>() {});
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
