package com.api.vendas.ui;

import com.api.vendas.model.Funcionario;
import com.api.vendas.model.Perfil;
import com.fasterxml.jackson.core.type.TypeReference;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class MainApp extends JFrame {

    private final ApiClient apiClient;
    private JTabbedPane tabbedPane;

    public MainApp() {
        apiClient = new ApiClient();
        setTitle("Sistema de Gestão Comercial");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void start() {
        if (showLoginDialog()) {
            buildMainUI();
            setVisible(true);
        } else {
            System.exit(0);
        }
    }

    private boolean showLoginDialog() {
        JDialog loginDialog = new JDialog(this, "Login", true);
        loginDialog.setSize(350, 200);
        loginDialog.setLocationRelativeTo(this);
        loginDialog.setLayout(new GridLayout(4, 2, 10, 10));

        loginDialog.add(new JLabel("Email:"));
        JTextField emailField = new JTextField();
        loginDialog.add(emailField);

        loginDialog.add(new JLabel("Senha:"));
        JPasswordField passwordField = new JPasswordField();
        loginDialog.add(passwordField);

        JButton loginButton = new JButton("Login");
        JButton createAccountButton = new JButton("Criar Conta");

        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String senha = new String(passwordField.getPassword());
            if (apiClient.login(email, senha)) {
                JOptionPane.showMessageDialog(loginDialog, "Login realizado com sucesso!\nToken JWT verificado.");
                loginDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(loginDialog, "Falha no login. Verifique as credenciais e se a API está rodando.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        createAccountButton.addActionListener(e -> {
            showCreateAccountDialog(loginDialog);
        });

        loginDialog.add(loginButton);
        loginDialog.add(createAccountButton);

        loginDialog.setVisible(true);

        return apiClient.getJwtToken() != null;
    }

    private void showCreateAccountDialog(JDialog parent) {
        JDialog createDialog = new JDialog(parent, "Criar Conta", true);
        createDialog.setSize(400, 450);
        createDialog.setLocationRelativeTo(parent);
        createDialog.setLayout(new GridLayout(9, 2, 5, 5));

        JTextField nomeField = new JTextField();
        JTextField cpfField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField senhaField = new JPasswordField();
        JTextField telefoneField = new JTextField();
        JTextField enderecoField = new JTextField();
        JTextField salarioField = new JTextField("0.0");
        JTextField cargoField = new JTextField();

        createDialog.add(new JLabel("Nome:")); createDialog.add(nomeField);
        createDialog.add(new JLabel("CPF:")); createDialog.add(cpfField);
        createDialog.add(new JLabel("Email:")); createDialog.add(emailField);
        createDialog.add(new JLabel("Senha:")); createDialog.add(senhaField);
        createDialog.add(new JLabel("Telefone:")); createDialog.add(telefoneField);
        createDialog.add(new JLabel("Endereço:")); createDialog.add(enderecoField);
        createDialog.add(new JLabel("Salário:")); createDialog.add(salarioField);
        createDialog.add(new JLabel("Cargo:")); createDialog.add(cargoField);

        JButton saveButton = new JButton("Salvar");
        saveButton.addActionListener(e -> {
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
            f.setPerfil(Perfil.USER);

            try {
                apiClient.post("/funcionarios", f, new TypeReference<Funcionario>(){});
                JOptionPane.showMessageDialog(createDialog, "Conta criada com sucesso! Faça o login.");
                createDialog.dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(createDialog, "Erro ao criar conta: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        createDialog.add(new JLabel("")); // spacer
        createDialog.add(saveButton);

        createDialog.setVisible(true);
    }

    private void buildMainUI() {
        tabbedPane = new JTabbedPane();
        
        tabbedPane.addTab("Clientes", new com.api.vendas.ui.panels.ClientePanel(apiClient));
        tabbedPane.addTab("Produtos", new com.api.vendas.ui.panels.ProdutoPanel(apiClient));
        tabbedPane.addTab("Pedidos", new com.api.vendas.ui.panels.PedidoPanel(apiClient));
        tabbedPane.addTab("Funcionários", new com.api.vendas.ui.panels.FuncionarioPanel(apiClient));
        tabbedPane.addTab("Nova Venda", new com.api.vendas.ui.panels.VendaPanel(apiClient));
        tabbedPane.addTab("Relatórios", new com.api.vendas.ui.panels.RelatorioPanel(apiClient));

        add(tabbedPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainApp().start();
        });
    }
}
