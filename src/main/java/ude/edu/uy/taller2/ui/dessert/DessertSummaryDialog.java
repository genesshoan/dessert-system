package ude.edu.uy.taller2.ui.dessert;

import ude.edu.uy.taller2.client.controller.dessert.DessertSalesSummaryController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DessertSummaryDialog extends JDialog {

    private final DessertSalesSummaryController dessertSalesSummaryController;

    private final JTextField codeField;
    private final JSpinner dateSpinner;
    private final JLabel lblUnits;
    private final JLabel lblAmount;

    public DessertSummaryDialog(Frame parent, String prefilledCode) {
        super(parent, "Sales summary", true);

        dessertSalesSummaryController = new DessertSalesSummaryController(this);

        // Componentes
        JPanel panelFields = new JPanel(new GridLayout(0, 2, 5, 5));
        JPanel panelResult = new JPanel(new GridLayout(0, 2, 5, 5));
        JPanel panelButtons = new JPanel();

        JButton btnSearch = new JButton("Search");

        codeField = new JTextField(prefilledCode != null ? prefilledCode : "", 12);

        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor =
                new JSpinner.DateEditor(dateSpinner, "yyyy/MM/dd");
        dateSpinner.setEditor(editor);

        lblUnits = new JLabel("-");
        lblAmount = new JLabel("-");

        // Configuración ventana
        setLayout(new BorderLayout());

        panelFields.setBorder(new EmptyBorder(10, 10, 5, 10));
        panelResult.setBorder(new EmptyBorder(5, 10, 10, 10));

        panelFields.add(new JLabel("Dessert code:"));
        panelFields.add(codeField);
        panelFields.add(new JLabel("Date:"));
        panelFields.add(dateSpinner); // ← CORRECTO

        panelResult.add(new JLabel("Total units:"));
        panelResult.add(lblUnits);
        panelResult.add(new JLabel("Total amount:"));
        panelResult.add(lblAmount);

        panelButtons.add(btnSearch);

        JPanel panelCenter = new JPanel(new BorderLayout());
        panelCenter.add(panelFields, BorderLayout.NORTH);
        panelCenter.add(panelResult, BorderLayout.CENTER);

        add(panelCenter, BorderLayout.CENTER);
        add(panelButtons, BorderLayout.SOUTH);

        // Acción botón
        btnSearch.addActionListener(e -> {

            String code = codeField.getText().trim();
            if (code.isBlank()) {
                showError("Warning", "Enter a dessert code");
                return;
            }

            try {
                dateSpinner.commitEdit(); // fuerza validación del texto
            } catch (Exception ex) {
                showError("Warning", "Invalid date format");
                return;
            }

            LocalDate date = ((Date) dateSpinner.getValue())
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            dessertSalesSummaryController.getSalesByDessert(code, date);
        });

        getRootPane().setDefaultButton(btnSearch);

        pack();
        setLocationRelativeTo(parent);
    }

    public void setSummary(int units, String amount) {
        lblUnits.setText(String.valueOf(units));
        lblAmount.setText("$" + amount);
    }

    public boolean isConnected() {
        return dessertSalesSummaryController.isConnected();
    }

    public void showError(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }
}