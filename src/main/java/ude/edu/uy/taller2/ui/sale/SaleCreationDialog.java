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

    private JPanel fields;
    private JPanel buttons;

    private JLabel addressLabel;
    private JLabel dateLabel;

    private JTextField addressField;

    private SpinnerDateModel model;
    private JSpinner dateSpinner;

    private JButton btnSubmit;
    private JButton btnCancel;

    private boolean created = false;

    public SaleCreationDialog(JFrame parent) {
        super(parent, "Sale creation form", true);

        createSaleController = new CreateSaleController(this);

        // Componentes
        fields = new JPanel(new GridLayout(0, 2, 5, 5));
        buttons = new JPanel(new GridLayout(0, 2, 5, 5));

        addressLabel = new JLabel("Address:");
        dateLabel = new JLabel("Date:");

        addressField = new JTextField();

        model = new SpinnerDateModel();
        dateSpinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dateSpinner, "yyyy/MM/dd");

        btnSubmit = new JButton("Submit");
        btnCancel = new JButton("Cancel");

        // Configuración
        setLayout(new BorderLayout());

        fields.setBorder(new EmptyBorder(10, 10, 10, 10));
        buttons.setBorder(new EmptyBorder(0, 10, 10, 10));

        fields.add(addressLabel);
        fields.add(addressField);
        fields.add(dateLabel);
        fields.add(editor);

        buttons.add(btnCancel);
        buttons.add(btnSubmit);

        add(fields, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        // Actions
        btnSubmit.addActionListener((e) -> {
            if (!validateAddress()) {
                return;
            }

            Date date = (Date) dateSpinner.getValue();

            LocalDate localDate = date.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            if (createSaleController.createSale(addressField.getText(), localDate)) {
                created = true;
                dispose();
            }
        });

        btnCancel.addActionListener((e) -> dispose());

        pack();
        setLocationRelativeTo(parent);
    }

    public boolean wasCreated() {
        return created;
    }

    public boolean isConnected() {
        return createSaleController.isConnected();
    }

    private boolean validateAddress() {
        return !addressField.getText().isBlank();
    }
}
