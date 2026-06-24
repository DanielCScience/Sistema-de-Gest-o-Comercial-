package com.api.vendas.ui.panels;

import com.api.vendas.model.Venda;
import com.api.vendas.ui.ApiClient;
import com.fasterxml.jackson.core.type.TypeReference;

import javax.swing.*;
import java.awt.*;

public class VendaPanel extends JPanel {
    private final ApiClient apiClient;

    public VendaPanel(ApiClient apiClient) {
        this.apiClient = apiClient;
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.gridy = 0;

        add(new JLabel("ID do Pedido a Faturar:"), gbc);
        
        gbc.gridx = 1;
        JTextField pedidoIdField = new JTextField(15);
        add(pedidoIdField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Forma de Pagamento:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> formaPagamentoCombo = new JComboBox<>(new String[]{"DINHEIRO", "CARTAO_CREDITO", "CARTAO_DEBITO", "PIX"});
        add(formaPagamentoCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnFaturar = new JButton("Faturar Venda");
        add(btnFaturar, gbc);

        btnFaturar.addActionListener(e -> {
            String pedidoId = pedidoIdField.getText();
            String formaPagamento = (String) formaPagamentoCombo.getSelectedItem();
            if (pedidoId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o ID do pedido!");
                return;
            }

            try {
                // Post expects an object body, but this endpoint uses PathVariable and RequestParam. 
                // We'll pass a null body or an empty string, the ApiClient will serialize it to json.
                Venda venda = apiClient.post("/vendas/faturar/" + pedidoId + "?formaPagamento=" + formaPagamento, "", new TypeReference<Venda>() {});
                JOptionPane.showMessageDialog(this, "Venda realizada com sucesso! ID da Venda: " + venda.getIdvenda());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao faturar: " + ex.getMessage());
            }
        });
    }
}
