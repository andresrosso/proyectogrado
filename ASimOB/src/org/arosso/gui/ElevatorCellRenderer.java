package org.arosso.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ElevatorCellRenderer extends DefaultTableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

		// Cells are by default rendered as a JLabel.
		JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

		// Get the status for the current row.
		ElevatorTableModel tableModel = (ElevatorTableModel) table.getModel();
		if (tableModel.isElevatorInFloor(row, col)) {
			switch (tableModel.getState(col)) {
			case MOVING:
				l.setBackground(Color.GREEN);
				break;
			case OPEN_DOOR:
				l.setBackground(Color.RED);
				break;
			case COMING_PASS:
				l.setBackground(Color.RED);
				break;
			case EXIT_PASS:
				l.setBackground(Color.RED);
				break;
			case CLOSE_DOOR:
				l.setBackground(Color.YELLOW);
				break;
			case RESTING:
				l.setBackground(Color.BLUE);
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