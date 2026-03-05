package ude.edu.uy.taller2.ui.dessert;

import ude.edu.uy.taller2.client.controller.dessert.CreateDessertController;
import ude.edu.uy.taller2.dto.DessertDTO;
import ude.edu.uy.taller2.dto.LightDessertDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;

public class DessertCreationForm extends JDialog {
    private final CreateDessertController createDessertController;

    private final JTextField codeField;
    private final JTextField nameField;
    private final JTextField priceField;
    private final JTextField sweetenerField;
    private final JTextField descriptionField;

    private boolean created = false;

    public DessertCreationForm(Frame parent) {
        super(parent, "Dessert creation form", true);

        createDessertController = new CreateDessertController(this);

        // Componentes
        JPanel fields = new JPanel(new BorderLayout());
        JPanel basicFields = new JPanel(new GridLayout(0, 2, 5, 5));
        JPanel lightFields = new JPanel(new GridLayout(0, 2, 5, 5));
        JPanel buttons = new JPanel();
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Normal", "Light"});
        JButton btnSubmit = new JButton("Submit");

        codeField = new JTextField();
        nameField = new JTextField();
        priceField = new JTextField();
        sweetenerField = new JTextField();
        descriptionField = new JTextField();

        // Configuración
        setLayout(new BorderLayout());

        basicFields.setBorder(new EmptyBorder(10, 10, 5, 10));
        lightFields.setBorder(new EmptyBorder(0, 10, 10, 10));
        lightFields.setVisible(false);

        codeField.setToolTipText("Example: choco3");

        basicFields.add(new JLabel("Code:"));
        basicFields.add(codeField);
        basicFields.add(new JLabel("Name:"));
        basicFields.add(nameField);
        basicFields.add(new JLabel("Price:"));
        basicFields.add(priceField);
        basicFields.add(new JLabel("Type:"));
        basicFields.add(typeCombo);

        lightFields.add(new JLabel("Sweetener:"));
        lightFields.add(sweetenerField);
        lightFields.add(new JLabel("Description:"));
        lightFields.add(descriptionField);

        fields.add(basicFields, BorderLayout.NORTH);
        fields.add(lightFields, BorderLayout.CENTER);

        buttons.add(btnSubmit);

        add(fields, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        // Actions
        typeCombo.addActionListener(e -> {
            lightFields.setVisible(typeCombo.getSelectedItem().equals("Light"));
            pack();
        });

        btnSubmit.addActionListener(e -> {
            boolean isLight = typeCombo.getSelectedItem().equals("Light");

            if (!validateFields(isLight)) return;

            DessertDTO dessertDTO = isLight
                    ? new LightDessertDTO(
                    codeField.getText().trim(),
                    nameField.getText().trim(),
                    parsePrice(),
                    sweetenerField.getText().trim(),
                    descriptionField.getText().trim())
                    : new DessertDTO(
                    codeField.getText().trim(),
                    nameField.getText().trim(),
                    parsePrice());

            if (createDessertController.createDessert(dessertDTO)) {
                created = true;
                dispose();
            }
        });

        getRootPane().setDefaultButton(btnSubmit);
        pack();
        setLocationRelativeTo(parent);
    }

    public boolean wasCreated() {
        return created;
    }

    public boolean isConnected() {
        return createDessertController.isConnected();
    }

    public void showError(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    private boolean validateFields(boolean isLight) {
        if (codeField.getText().isBlank()) {
            showError("Validation error", "Code is required");
            return false;
        }
        if (nameField.getText().isBlank()) {
            showError("Validation error", "Name is required");
            return false;
        }
        if (priceField.getText().isBlank()) {
            showError("Validation error", "Price is required");
            return false;
        }
        try {
            parsePrice();
        } catch (NumberFormatException ex) {
            showError("Validation error", "Price must be a valid number");
            return false;
        }
        if (isLight && sweetenerField.getText().isBlank()) {
            showError("Validation error", "Sweetener is required for light desserts");
            return false;
        }
        return true;
    }

    private BigDecimal parsePrice() {
        return new BigDecimal(priceField.getText().trim().replace(",", "."));
    }
}