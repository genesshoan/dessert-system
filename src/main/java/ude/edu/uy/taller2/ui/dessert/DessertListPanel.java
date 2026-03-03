package ude.edu.uy.taller2.ui.dessert;

import ude.edu.uy.taller2.client.controller.DessertController;
import ude.edu.uy.taller2.dto.DessertDTO;
import ude.edu.uy.taller2.exception.DessertAlreadyExistsException;
import ude.edu.uy.taller2.exception.InvalidAmountException;
import ude.edu.uy.taller2.exception.RequiredFieldIsEmptyException;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.rmi.RemoteException;

/**
 * Panel que muestra una lista de postres en una tabla y ofrece acciones para crear, actualizar
 * y ver detalles de los postres.
 *
 * <p>El panel interactúa con un {@link ude.edu.uy.taller2.client.controller.DessertController}
 * para obtener y modificar los datos. Los errores de conexión o de validación se muestran
 * mediante diálogos al usuario.</p>
 *
 * @author Equipo Taller2
 * @version 1.0
 */
public class DessertListPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final JTable table;
	private final DefaultTableModel model;

	private final DessertController dessertController;

	/**
	 * Construye el panel de lista de postres y realiza la carga inicial de datos.
	 *
	 * @param dessertController controlador cliente usado para operaciones sobre postres; no debe ser {@code null}
	 */
	public DessertListPanel(DessertController dessertController) {
		this.dessertController = dessertController;

		// Componentes
        JScrollPane scrollPane = new JScrollPane();
        JPanel panelNorth = new JPanel();
        JPanel panelSouth = new JPanel();
        JButton btnAddDessert = new JButton("Add dessert");
        JButton btnUpdate = new JButton("Update");
		JButton btnDetail = new JButton("Detail");

		String[] columns = {"Code", "Name", "Price", "Type", "DTO"};
		model = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		// Configuración
		setLayout(new BorderLayout(0, 0));
		setBorder(new EmptyBorder(10, 10, 5, 10));

		table = new JTable(model);
		table.removeColumn(table.getColumnModel().getColumn(4));
		scrollPane.setViewportView(table);

		add(scrollPane, BorderLayout.CENTER);
		add(panelNorth, BorderLayout.NORTH);
		add(panelSouth, BorderLayout.SOUTH);

		panelNorth.add(btnAddDessert);
		panelNorth.add(btnUpdate);
		panelSouth.add(btnDetail);

		// Listeners
		btnAddDessert.addActionListener(e -> createDessert());

		btnUpdate.addActionListener(e -> refreshTable());

		btnDetail.addActionListener(e -> {
			int selectedRow = table.getSelectedRow();

			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(this,
						"Select a dessert first",
						"Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}

			int modelRow = table.convertRowIndexToModel(selectedRow);
			DessertDTO dessertDTO = (DessertDTO) model.getValueAt(modelRow, 4);

			DessertDetailDialog dialog = new DessertDetailDialog(
					(Frame) SwingUtilities.getWindowAncestor(this),
					dessertDTO);
			dialog.setVisible(true);
		});

		table.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				if (evt.getClickCount() == 2) {
					btnDetail.doClick();
				}
			}
		});

		// Carga inicial
		refreshTable();
	}

	private void createDessert() {
		DessertCreationForm form = new DessertCreationForm(
				(Frame) SwingUtilities.getWindowAncestor(this));
		form.setVisible(true);
		DessertDTO dessertDTO = form.getResult();

		if (dessertDTO != null) {
			try {
				dessertController.createDessert(dessertDTO);
				refreshTable();
			} catch (RemoteException ex) {
				JOptionPane.showMessageDialog(this,
						"Server is not available right now",
						"Connection error", JOptionPane.ERROR_MESSAGE);
			} catch (RequiredFieldIsEmptyException ex) {
				JOptionPane.showMessageDialog(this,
						ex.getMessage(),
						"Required field is missing", JOptionPane.ERROR_MESSAGE);
			} catch (InvalidAmountException ex) {
				JOptionPane.showMessageDialog(this,
						ex.getMessage(),
						"Invalid amount", JOptionPane.ERROR_MESSAGE);
			} catch (DessertAlreadyExistsException ex) {
				JOptionPane.showMessageDialog(this,
						ex.getMessage(),
						"Dessert already exists", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void refreshTable() {
		try {
			model.setRowCount(0);

			dessertController.getAllDesserts()
					.forEach(d ->
							model.addRow(new Object[]{
									d.getCode(),
									d.getName(),
									d.getPrice(),
									d.getType(),
									d
							}));
		} catch (RemoteException ex) {
			JOptionPane.showMessageDialog(
					this,
					"Cannot connect with the server",
					"Connection error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}