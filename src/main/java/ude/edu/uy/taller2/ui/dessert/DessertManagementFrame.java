package ude.edu.uy.taller2.ui.dessert;

import ude.edu.uy.taller2.client.controller.dessert.DessertListController;
import ude.edu.uy.taller2.dto.DessertDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class DessertManagementFrame extends JFrame {

	private final JTable table;
	private final DefaultTableModel model;
	private final DessertListController dessertListController;

	private final JTextField searchField;

	public DessertManagementFrame(JFrame parent) {
		dessertListController = new DessertListController(this);

		// Componentes
		JScrollPane scrollPane = new JScrollPane();
		JPanel panelNorth = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel panelSearch = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel panelSouth = new JPanel();
		JButton btnAddDessert = new JButton("Add dessert");
		JButton btnUpdate = new JButton("Update");
		JButton btnDetail = new JButton("Detail");
		JButton btnSearch = new JButton("Search");
		JButton btnSummarize = new JButton("Summarize");

		searchField = new JTextField(12);

		String[] columns = {"Code", "Name", "Price", "Type", "DTO"};
		model = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		// Configuración
		setTitle("Desserts");
		setSize(800, 600);
		setLocationRelativeTo(parent);
		setLayout(new BorderLayout(0, 0));
		((JComponent) getContentPane()).setBorder(new EmptyBorder(10, 10, 5, 10));

		table = new JTable(model);
		table.removeColumn(table.getColumnModel().getColumn(4));
		scrollPane.setViewportView(table);

		panelNorth.add(btnAddDessert);
		panelNorth.add(btnUpdate);

		panelSearch.add(new JLabel("Code:"));
		panelSearch.add(searchField);
		panelSearch.add(btnSearch);

		JPanel panelTop = new JPanel(new BorderLayout());
		panelTop.add(panelNorth, BorderLayout.NORTH);
		panelTop.add(panelSearch, BorderLayout.SOUTH);

		panelSouth.add(btnDetail);
		panelSouth.add(btnSummarize);

		add(scrollPane, BorderLayout.CENTER);
		add(panelTop, BorderLayout.NORTH);
		add(panelSouth, BorderLayout.SOUTH);

		// Actions
		btnAddDessert.addActionListener(e -> {
			DessertCreationForm form = new DessertCreationForm((Frame) SwingUtilities.getWindowAncestor(this));
			form.setVisible(form.isConnected());
			if (form.wasCreated()) {
				refreshTable();
			}
		});

		btnUpdate.addActionListener(e -> refreshTable());

		btnSearch.addActionListener(e -> {
			String code = searchField.getText().trim();
			if (code.isBlank()) {
				showError("Warning", "Enter a code to search");
				return;
			}
			DessertDTO dessertDTO = dessertListController.getDessertByCode(code);
			if (dessertDTO != null) {
				DessertDetailDialog dessertDetailDialog = new DessertDetailDialog(this, dessertDTO);
				dessertDetailDialog.setVisible(true);
			}
		});

		btnDetail.addActionListener(e -> {
			int selectedRow = table.getSelectedRow();
			if (selectedRow == -1) {
				showError("Warning", "Select a dessert first");
				return;
			}
			int modelRow = table.convertRowIndexToModel(selectedRow);
			DessertDTO dessertDTO = (DessertDTO) model.getValueAt(modelRow, 4);
			DessertDetailDialog dialog = new DessertDetailDialog(
					(Frame) SwingUtilities.getWindowAncestor(this), dessertDTO);
			dialog.setVisible(true);
		});

		btnSummarize.addActionListener(e -> {
			String code = null;
			int selectedRow = table.getSelectedRow();
			if (selectedRow != -1) {
				int modelRow = table.convertRowIndexToModel(selectedRow);
				code = (String) model.getValueAt(modelRow, 0);
			}
			DessertSummaryDialog dessertSummaryDialog = new DessertSummaryDialog(
					(Frame) SwingUtilities.getWindowAncestor(this), code);
			dessertSummaryDialog.setVisible(dessertSummaryDialog.isConnected());
		});

		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() == 2) {
					btnDetail.doClick();
				}
			}
		});

		if (dessertListController.isConnected()) {
			refreshTable();
		}
	}

	public boolean isConnected() {
		return dessertListController.isConnected();
	}

	public void showError(String title, String message) {
		JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
	}

	private void refreshTable() {
		new SwingWorker<List<DessertDTO>, Void>() {
			@Override
			protected List<DessertDTO> doInBackground() {
				return dessertListController.getAllDesserts();
			}

			@Override
			protected void done() {
				try {
					List<DessertDTO> desserts = get();
					model.setRowCount(0);
					desserts.forEach(d -> model.addRow(new Object[]{
							d.getCode(),
							d.getName(),
							d.getPrice(),
							d.getType(),
							d
					}));
				} catch (Exception e) {
					showError("Unexpected error", "Cannot retrieve desserts");
				}
			}
		}.execute();
	}
}