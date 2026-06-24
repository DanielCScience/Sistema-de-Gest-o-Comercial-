package com.api.vendas.ui.panels;

import com.api.vendas.model.Pedido;
import com.api.vendas.ui.ApiClient;
import com.fasterxml.jackson.core.type.TypeReference;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class RelatorioPanel extends JPanel {
    private final ApiClient apiClient;
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> reportTypeCombo;
    private JTextField paramField;

    public RelatorioPanel(ApiClient apiClient) {
        this.apiClient = apiClient;
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        reportTypeCombo = new JComboBox<>(new String[]{"Vendas Anuais (ADMIN)", "Vendas por Período", "Histórico de Cliente"});
        paramField = new JTextField(15);
        paramField.setToolTipText("Ex: Para Período use '2026-01-01T00:00:00,2026-12-31T23:59:59'. Para Cliente use o ID.");
        JButton btnGerar = new JButton("Gerar Relatório");

        topPanel.add(new JLabel("Tipo:"));
        topPanel.add(reportTypeCombo);
        topPanel.add(new JLabel("Parâmetros (ID ou Datas):"));
        topPanel.add(paramField);
        topPanel.add(btnGerar);

        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        btnGerar.addActionListener(e -> gerarRelatorio());
    }

    private void gerarRelatorio() {
        int index = reportTypeCombo.getSelectedIndex();
        String params = paramField.getText();
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        try {
            if (index == 0) {
                // Vendas Anuais
                List<Map<String, Object>> dados = apiClient.get("/relatorios/vendas-anuais", new TypeReference<List<Map<String, Object>>>() {});
                if (dados != null && !dados.isEmpty()) {
                    for (String key : dados.get(0).keySet()) {
                        tableModel.addColumn(key);
                    }
                    for (Map<String, Object> row : dados) {
                        Object[] rowData = new Object[tableModel.getColumnCount()];
                        int c = 0;
                        for (Object val : row.values()) {
                            rowData[c++] = val;
                        }
                        tableModel.addRow(rowData);
                    }
                }
            } else if (index == 1) {
                // Vendas Período
                String[] dts = params.split(",");
                if (dts.length != 2) {
                    JOptionPane.showMessageDialog(this, "Informe as datas no formato: yyyy-MM-dd'T'HH:mm:ss,yyyy-MM-dd'T'HH:mm:ss");
                    return;
                }
                Map<String, Object> dados = apiClient.get("/relatorios/vendas-periodo?inicio=" + dts[0] + "&fim=" + dts[1], new TypeReference<Map<String, Object>>() {});
                tableModel.addColumn("Métrica");
                tableModel.addColumn("Valor");
                if (dados != null) {
                    for (Map.Entry<String, Object> entry : dados.entrySet()) {
                        tableModel.addRow(new Object[]{entry.getKey(), entry.getValue()});
                    }
                }
            } else if (index == 2) {
                // Histórico
                if (params.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Informe o ID do cliente");
                    return;
                }
                List<Pedido> pedidos = apiClient.get("/relatorios/historico-cliente/" + params, new TypeReference<List<Pedido>>() {});
                tableModel.addColumn("ID Pedido");
                tableModel.addColumn("Data");
                tableModel.addColumn("Valor Total");
                tableModel.addColumn("Status");

                if (pedidos != null) {
                    for (Pedido p : pedidos) {
                        tableModel.addRow(new Object[]{p.getIdpedido(), p.getData(), p.getValor(), p.getStatus()});
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao gerar relatório (verifique se tem permissão): " + ex.getMessage());
        }
    }
}
