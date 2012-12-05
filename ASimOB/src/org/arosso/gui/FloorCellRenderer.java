package org.arosso.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.arosso.model.BuildingModel;

public class FloorCellRenderer extends DefaultTableCellRenderer {
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
		// Cells are by default rendered as a JLabel.
		JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
		// Get the status for the current row.
		l.setBackground(Color.WHITE);
		int i = ((ElevatorTableModel)table.getModel()).getRowCount();
		// Return the JLabel which renders the cell.
		ElevatorTableModel tableModel = (ElevatorTableModel) table.getModel();
		String callsInFloor = tableModel.callsInFloor(row+1);
		l.setText(String.valueOf(i-row-1) + "               "+callsInFloor);
		return l;
	}

}