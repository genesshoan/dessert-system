package ude.edu.uy.taller2.ui.dessert;

import ude.edu.uy.taller2.dto.DessertDTO;
import ude.edu.uy.taller2.dto.LightDessertDTO;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Diálogo modal para mostrar los detalles de un postre ({@link DessertDTO}).
 *
 * <p>Presenta los campos del DTO en etiquetas de solo lectura. Si el DTO es una instancia de
 * {@link LightDessertDTO}, se muestran los campos adicionales correspondientes.</p>
 *
 * @author Equipo Taller2
 * @version 1.0
 */
public class DessertDetailDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	/**
	 * Crea un diálogo modal que muestra la información contenida en el {@code dessertDTO}.
	 *
	 * @param parent ventana padre para centrar el diálogo; puede ser {@code null}
	 * @param dessertDTO DTO con los datos a mostrar; no se modifica por el diálogo
	 */
	public DessertDetailDialog(Frame parent, DessertDTO dessertDTO) {
		super(parent, "Dessert detail", true);

		// Componentes
		JPanel basicFields = new JPanel(new GridLayout(0, 2, 5, 5));
		JPanel lightFields = new JPanel(new GridLayout(0, 2, 5, 5));
		JPanel buttons = new JPanel();
		JButton btnClose = new JButton("Close");

		// Configuración
		setLayout(new BorderLayout());

		basicFields.setBorder(new EmptyBorder(10, 10, 5, 10));
		lightFields.setBorder(new EmptyBorder(0, 10, 10, 10));
		lightFields.setVisible(false);

		basicFields.add(new JLabel("Code:"));
		basicFields.add(new JLabel(dessertDTO.getCode()));

		basicFields.add(new JLabel("Name:"));
		basicFields.add(new JLabel(dessertDTO.getName()));

		basicFields.add(new JLabel("Price:"));
		basicFields.add(new JLabel(dessertDTO.getPrice().toPlainString()));

		basicFields.add(new JLabel("Type:"));
		basicFields.add(new JLabel(dessertDTO.getType()));

		if (dessertDTO instanceof LightDessertDTO ld) {
			lightFields.add(new JLabel("Sweetener:"));
			lightFields.add(new JLabel(ld.getSweetener()));

			lightFields.add(new JLabel("Description:"));
			JTextArea descArea = new JTextArea(ld.getDescription());
			descArea.setLineWrap(true);
			descArea.setWrapStyleWord(true);
			descArea.setEditable(false);
			descArea.setOpaque(false);
			descArea.setFont(UIManager.getFont("Label.font"));
			lightFields.add(descArea);

			lightFields.setVisible(true);
		}

		buttons.add(btnClose);

		add(basicFields, BorderLayout.NORTH);
		add(lightFields, BorderLayout.CENTER);
		add(buttons, BorderLayout.SOUTH);

		// Actions
		btnClose.addActionListener(e -> dispose());

		// Tamaño y posición
		setMinimumSize(new Dimension(350, 200));
		pack();
		setLocationRelativeTo(parent);
	}
}