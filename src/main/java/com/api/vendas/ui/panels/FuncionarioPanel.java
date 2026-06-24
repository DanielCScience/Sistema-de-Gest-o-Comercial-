package com.api.vendas.ui.panels;

import com.api.vendas.model.Funcionario;
import com.api.vendas.model.Perfil;
import com.api.vendas.ui.ApiClient;
import com.fasterxml.jackson.core.type.TypeReference;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class FuncionarioPanel extends JPanel {
    private final ApiClient apiClient;
    private JTable table;
    private DefaultTableModel tableModel;

    public FuncionarioPanel(ApiClient apiClient) {
        this.apiClient = apiClient;
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"ID", "Nome", "CPF", "Email", "Cargo", "Salário"}, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton btnCarregar = new JButton("Carregar");
        JButton btnNovo = new JButton("Novo (POST)");

        buttonPanel.add(btnCarregar);
        buttonPanel.add(btnNovo);
        add(buttonPanel, BorderLayout.SOUTH);

        btnCarregar.addActionListener(e -> carregarDados());
        btnNovo.addActionListener(e -> showFormDialog());

        carregarDados();
    }

    private void carregarDados() {
        try {
            List<Funcionario> funcionarios = apiClient.get("/funcionarios", new TypeReference<List<Funcionario>>() {});
            tableModel.setRowCount(0);
            for (Funcionario f : funcionarios) {
                tableModel.addRow(new Object[]{f.getIdfuncionario(), f.getNome(), f.getCpf(), f.getEmail(), f.getCargo(), f.getSalario()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar funcionários: " + ex.getMessage());
        }
    }

    private void showFormDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Novo Funcionário", true);
        dialog.setSize(400, 450);
        dialog.setLayout(new GridLayout(9, 2, 5, 5));
        dialog.setLocationRelativeTo(this);

        JTextField nomeField = new JTextField();
        JTextField cpfField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField senhaField = new JPasswordField();
        JTextField telefoneField = new JTextField();
        JTextField enderecoField = new JTextField();
        JTextField salarioField = new JTextField("0.0");
        JTextField cargoField = new JTextField();

        dialog.add(new JLabel("Nome:")); dialog.add(nomeField);
        dialog.add(new JLabel("CPF:")); dialog.add(cpfField);
        dialog.add(new JLabel("Email:")); dialog.add(emailField);
        dialog.add(new JLabel("Senha:")); dialog.add(senhaField);
        dialog.add(new JLabel("Telefone:")); dialog.add(telefoneField);
        dialog.add(new JLabel("Endereço:")); dialog.add(enderecoField);
        dialog.add(new JLabel("Salário:")); dialog.add(salarioField);
        dialog.add(new JLabel("Cargo:")); dialog.add(cargoField);

        JButton saveBtn = new JButton("Salvar");
        saveBtn.addActionListener(e -> {
            Funcionario f = new Funcionario();
            f.setNome(nomeField.getText());
            f.setCpf(cpfField.getText());
            f.setEmail(emailField.getText());
            f.setSenha(new String(senhaField.getPassword()));
            f.setTelefone(telefoneField.getText());
            f.setEndereco(enderecoField.getText());
            f.setSalario(new BigDecimal(salarioField.getText()));
            f.setCargo(cargoField.getText());
            f.setDataadmissao(LocalDate.now());
            f.setPerfil(Perfil.VENDEDOR);

            try {
                apiClient.post("/funcionarios", f, new TypeReference<Funcionario>() {});
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
