package ude.edu.uy.taller2.ui.sale;

import ude.edu.uy.taller2.client.controller.sale.ListSaleController;
import ude.edu.uy.taller2.dto.SaleDTO;
import ude.edu.uy.taller2.server.collection.SaleFilter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class SaleManagementFrame extends JFrame {
    private final ListSaleController listSaleController;

    private final JScrollPane scrollPane;
    private final JPanel panelNorth;
    private final JPanel panelSouth;
    private final JTable table;
    private final DefaultTableModel model;
    private final JButton btnAddSale;
    private final JButton btnUpdate;
    private final JButton btnDetail;
    private final JComboBox<SaleFilter> filterCombo;

    public SaleManagementFrame(JFrame parent) {
        this.listSaleController = new ListSaleController(this);

        // Componentes
        scrollPane = new JScrollPane();
        panelNorth = new JPanel(new FlowLayout());
        panelSouth = new JPanel(new FlowLayout());

        String[] columns = {"ID", "Date", "Address", "Status", "DTO"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.removeColumn(table.getColumnModel().getColumn(4));

        btnAddSale = new JButton("Add sale");
        btnUpdate = new JButton("Update");
        btnDetail = new JButton("View detail");

        filterCombo = new JComboBox<>(SaleFilter.values());

        // Configuración
        setLayout(new BorderLayout(0, 0));
        setTitle("Sales");
        setSize(800, 600);
        setLocationRelativeTo(parent);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        panelNorth.add(btnAddSale);
        panelNorth.add(filterCombo);
        panelNorth.add(btnUpdate);

        scrollPane.setViewportView(table);

        panelSouth.add(btnDetail);

        add(panelNorth, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelSouth, BorderLayout.SOUTH);

        // Actions
        btnAddSale.addActionListener((e) -> {
            SaleCreationDialog saleCreationDialog = new SaleCreationDialog(this);

            saleCreationDialog.setVisible(saleCreationDialog.isConnected());

            if (saleCreationDialog.wasCreated()) {
                refreshTable();
            }
        });

        btnUpdate.addActionListener((e) -> refreshTable());

        btnDetail.addActionListener((e) -> {
            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {
                showError("Warning", "Select a sale first");
                return;
            }

            int modelRow = table.convertRowIndexToModel(selectedRow);
            SaleDTO saleDTO = (SaleDTO) model.getValueAt(modelRow, 4);

            SaleDetailDialog saleDetailDialog = new SaleDetailDialog(this, saleDTO);

            saleDetailDialog.setVisible(saleDetailDialog.isConnected());
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    btnDetail.doClick();
                }
            }
        });

        filterCombo.addActionListener((e) -> refreshTable());

        if (listSaleController.isConnected()) {
            refreshTable();
        }
    }

    public boolean isConnected() {
        return listSaleController.isConnected();
    }

    private void showError(String title, String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                title,
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void refreshTable() {
        new SwingWorker<List<SaleDTO>, Void>() {
            @Override
            protected List<SaleDTO> doInBackground() throws Exception {
                return listSaleController.getSalesByStatus((SaleFilter) filterCombo.getSelectedItem());
            }

            @Override
            protected void done() {
                try {
                    List<SaleDTO> saleDTOList = get();

                    model.setRowCount(0);

                    saleDTOList.forEach((s) -> {
                        model.addRow(new Object[]{
                                s.getId(),
                                s.getDate(),
                                s.getAddress(),
                                s.getStatus(),
                                s
                        });
                    });
                } catch (Exception e) {
                    showError("Unexpected error", "Cannot retrieve sales");
                }
            }
        }.execute();
    }
}
