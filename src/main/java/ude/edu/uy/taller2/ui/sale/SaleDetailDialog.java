package ude.edu.uy.taller2.ui.sale;

import ude.edu.uy.taller2.client.controller.sale.SaleDetailController;
import ude.edu.uy.taller2.dto.SaleDTO;
import ude.edu.uy.taller2.dto.SaleItemDTO;
import ude.edu.uy.taller2.server.collection.SaleDecision;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class SaleDetailDialog extends JDialog {
    private final SaleDetailController saleDetailController;

    private final SaleDTO saleDto;

    private final JPanel panelInfo;
    private final JPanel panelAdd;
    private final JPanel panelSouth;
    private final JScrollPane scrollPane;
    private final JTable table;
    private final DefaultTableModel model;

    private final JLabel lblInfo;
    private final JLabel lblTotal;

    private final JTextField dessertCodeField;
    private final JSpinner quantitySpinner;

    private final JButton btnAdd;
    private final JButton btnRemove;
    private final JButton btnComplete;
    private final JButton btnCancel;

    public SaleDetailDialog(JFrame parent, SaleDTO saleDTO) {
        super(parent, "Sale detail", true);

        saleDetailController = new SaleDetailController(this);
        this.saleDto = saleDTO;

        // Componentes
        panelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelAdd = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSouth = new JPanel(new BorderLayout());

        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.LEFT));

        lblInfo = new JLabel(String.format("Sale #%d  |  %s  |  %s  |  %s",
                saleDTO.getId(),
                saleDTO.getAddress(),
                saleDTO.getDate(),
                saleDTO.getStatus()));

        lblTotal = new JLabel("Total: $0.00");

        dessertCodeField = new JTextField(10);
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 40, 1));

        btnAdd = new JButton("Add");
        btnRemove = new JButton("Remove units");
        btnComplete = new JButton("Complete");
        btnCancel = new JButton("Cancel sale");

        String[] columns = {"Code", "Name", "Price", "Quantity", "DTO"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.removeColumn(table.getColumnModel().getColumn(4));
        scrollPane = new JScrollPane(table);

        // Configuracion
        setLayout(new BorderLayout(0, 0));
        setSize(700, 500);
        setLocationRelativeTo(parent);

        panelInfo.setBorder(new EmptyBorder(10, 10, 5, 10));
        panelAdd.setBorder(new EmptyBorder(5, 10, 5, 10));

        panelInfo.add(lblInfo);

        panelAdd.add(new JLabel("Dessert code:"));
        panelAdd.add(dessertCodeField);
        panelAdd.add(new JLabel("Quantity:"));
        panelAdd.add(quantitySpinner);
        panelAdd.add(btnAdd);
        panelAdd.add(btnRemove);

        JPanel panelNorth = new JPanel(new BorderLayout());
        panelNorth.add(panelInfo, BorderLayout.NORTH);
        panelNorth.add(panelAdd, BorderLayout.SOUTH);

        panelTotal.setBorder(new EmptyBorder(5, 10, 5, 10));
        panelTotal.add(lblTotal);

        panelButtons.setBorder(new EmptyBorder(5, 10, 5, 10));
        panelButtons.add(btnComplete);
        panelButtons.add(btnCancel);

        panelSouth.add(panelTotal, BorderLayout.WEST);
        panelSouth.add(panelButtons, BorderLayout.EAST);

        add(panelNorth, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelSouth, BorderLayout.SOUTH);

        btnAdd.addActionListener((e) -> {
            if (saleDetailController.addDessertUnits(
                    getDessertCode(),
                    saleDTO.getId(),
                    getQuantity())) {
                refreshTable();
            }
        });

        btnRemove.addActionListener((e) -> {
            if (saleDetailController.deleteDessertUnits(
                    getDessertCode(),
                    saleDTO.getId(),
                    getQuantity()
            )) {
                refreshTable();
            }
        });

        btnComplete.addActionListener((e) -> closeSale(SaleDecision.COMPLETE));

        btnCancel.addActionListener((e) -> closeSale(SaleDecision.CANCEL));

        if (saleDetailController.isConnected()) {
            refreshTable();
        }
    }

    public boolean isConnected() {
        return saleDetailController.isConnected();
    }

    private void refreshTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < model.getRowCount(); i++) {
            BigDecimal price = (BigDecimal) model.getValueAt(i, 2);
            int quantity = (int) model.getValueAt(i, 3);
            total = total.add(price.multiply(BigDecimal.valueOf(quantity)));
        }

        lblTotal.setText("Total: $" + total.toPlainString());
    }

    private void closeSale(SaleDecision saleDecision) {
        if (saleDetailController.finalizeSale(
                saleDto.getId(),
                saleDecision
        )) {
            dispose();
        }
    }

    private void refreshTable() {
        model.setRowCount(0);

        saleDetailController.getDessertsBySaleId(saleDto.getId())
                .forEach(s -> model.addRow(new Object[]{
                        s.getDessert().getCode(),
                        s.getDessert().getName(),
                        s.getDessert().getPrice(),
                        s.getQuantity(),
                        s
        }));

        refreshTotal();
    }

    private String getDessertCode() {
        return dessertCodeField.getText().trim();
    }

    private int getQuantity() {
        return (int) quantitySpinner.getValue();
    }
}