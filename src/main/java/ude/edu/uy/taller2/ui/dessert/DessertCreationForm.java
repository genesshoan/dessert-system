package ude.edu.uy.taller2.ui.dessert;

import ude.edu.uy.taller2.dto.DessertDTO;
import ude.edu.uy.taller2.dto.LightDessertDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;

/**
 * Diálogo modal para crear un postre ({@link DessertDTO} o {@link LightDessertDTO}) mediante una
 * interfaz gráfica.
 *
 * <p>Este formulario recoge los campos necesarios y construye el DTO correspondiente cuando el
 * usuario confirma la operación. Valida los campos básicos y muestra mensajes de error al usuario.</p>
 *
 * @author Equipo Taller2
 * @version 1.0
 */
public class DessertCreationForm extends JDialog {

    private final JTextField codeField;
    private final JTextField nameField;
    private final JTextField priceField;
    private final JTextField sweetenerField;
    private final JTextField descriptionField;

    private DessertDTO result;

    /**
     * Crea y configura el diálogo modal para ingresar los datos de un postre.
     *
     * @param parent ventana padre para centrar el diálogo; puede ser {@code null}
     * @throws HeadlessException si no hay entorno gráfico disponible
     */
    public DessertCreationForm(Frame parent) {
        super(parent, "Dessert creation form", true);

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

            BigDecimal price = parsePrice();

            result = isLight
                    ? new LightDessertDTO(
                    codeField.getText().trim(),
                    nameField.getText().trim(),
                    price,
                    sweetenerField.getText().trim(),
                    descriptionField.getText().trim())
                    : new DessertDTO(
                    codeField.getText().trim(),
                    nameField.getText().trim(),
                    price);

            dispose();
        });

        // Tamaño y posición
        getRootPane().setDefaultButton(btnSubmit);
        pack();
        setLocationRelativeTo(parent);
    }


    /**
     * Devuelve el DTO creado por el usuario.
     *
     * @return un objeto {@link DessertDTO} (o {@link LightDessertDTO}) si el usuario confirmó la creación;
     * {@code null} si canceló o cerró el formulario sin confirmar.
     */
    public DessertDTO getResult() {
        return result;
    }

    private boolean validateFields(boolean isLight) {

        if (codeField.getText().isBlank()) {
            showError("Code is required");
            return false;
        }

        if (nameField.getText().isBlank()) {
            showError("Name is required");
            return false;
        }

        if (priceField.getText().isBlank()) {
            showError("Price is required");
            return false;
        }

        try {
            parsePrice();
        } catch (NumberFormatException ex) {
            showError("Price must be a valid number");
            return false;
        }

        if (isLight && sweetenerField.getText().isBlank()) {
            showError("Sweetener is required for light desserts");
            return false;
        }

        return true;
    }

    private BigDecimal parsePrice() {
        String text = priceField.getText()
                .trim()
                .replace(",", ".");

        return new BigDecimal(text);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Validation error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}