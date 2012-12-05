package org.arosso.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.arosso.model.BuildingModel;

public class ElevatorCellRenderer extends DefaultTableCellRenderer {
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
		// Cells are by default rendered as a JLabel.
		JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
		// Get the status for the current row.
		ElevatorTableModel tableModel = (ElevatorTableModel) table.getModel();
		if (tableModel.isElevatorInFloor(row, col)) {
			l.setText(tableModel.getPassenger(col));
			switch (tableModel.getState(col)) {
			case MOVING:
				l.setBackground(Color.CYAN);
				break;
			case OPEN_DOOR:
				l.setBackground(Color.GREEN);
				break;
			case COMING_PASS:
				l.setBackground(Color.YELLOW);
				break;
			case EXIT_PASS:
				l.setBackground(Color.ORANGE);
				break;
			case CLOSE_DOOR:
				l.setBackground(Color.MAGENTA);
				break;
			case RESTING:
				l.setBackground(Color.DARK_GRAY);
				break;
			case OUT_OF_SERVICE:
				l.setBackground(Color.BLACK);
				break;
			}
		} else {
			l.setBackground(Color.GRAY);
		}
		
		// Return the JLabel which renders the cell.
		return l;

	}

}