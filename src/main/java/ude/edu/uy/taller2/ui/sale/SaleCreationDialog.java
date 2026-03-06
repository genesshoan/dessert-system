package ude.edu.uy.taller2.ui.sale;

import ude.edu.uy.taller2.client.controller.sale.CreateSaleController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class SaleCreationDialog extends JDialog {

    private final CreateSaleController createSaleController;

    private final JTextField addressField;
    private final JSpinner dateSpinner;

    private boolean created = false;

    public SaleCreationDialog(JFrame parent) {
        super(parent, "Sale creation form", true);

        createSaleController = new CreateSaleController(this);

        // Paneles
        JPanel fields = new JPanel(new GridLayout(0, 2, 5, 5));
        JPanel buttons = new JPanel(new GridLayout(1, 2, 5, 5));

        JButton btnSubmit = new JButton("Submit");
        JButton btnCancel = new JButton("Cancel");

        addressField = new JTextField();

        // Spinner de fecha correctamente configurado
        SpinnerDateModel model = new SpinnerDateModel();
        dateSpinner = new JSpinner(model);
        JSpinner.DateEditor editor =
                new JSpinner.DateEditor(dateSpinner, "yyyy/MM/dd");
        dateSpinner.setEditor(editor);

        // Configuración ventana
        setLayout(new BorderLayout());

        fields.setBorder(new EmptyBorder(10, 10, 10, 10));
        buttons.setBorder(new EmptyBorder(0, 10, 10, 10));

        fields.add(new JLabel("Address:"));
        fields.add(addressField);
        fields.add(new JLabel("Date:"));
        fields.add(dateSpinner); // ← CORRECTO

        buttons.add(btnCancel);
        buttons.add(btnSubmit);

        add(fields, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        // Acción Submit
        btnSubmit.addActionListener(e -> {

            if (addressField.getText().isBlank()) {
                JOptionPane.showMessageDialog(this,
                        "Address is required",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                dateSpinner.commitEdit();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Invalid date format",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Date date = (Date) dateSpinner.getValue();

            LocalDate localDate = date.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            if (createSaleController.createSale(
                    addressField.getText().trim(),
                    localDate)) {

                created = true;
                dispose();
            }
        });

        btnCancel.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(parent);
    }

    public boolean wasCreated() {
        return created;
    }

    public boolean isConnected() {
        return createSaleController.isConnected();
    }
}