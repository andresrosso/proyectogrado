package org.arosso.gui;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

public class ElevatorTableModel extends DefaultTableModel {

	private static final long serialVersionUID = 1L;
	private GuiModel guiModel;
	
	
	public ElevatorTableModel(GuiModel guiModel) {
		this.guiModel = guiModel;
		addTableModelListener( new TablaListener() );
	}
	
	List<Color> rowColours = Arrays.asList(
        Color.RED,
        Color.GREEN,
        Color.CYAN
    );
	
    public void setRowColour(int row, Color c) {
        rowColours.set(row, c);
        fireTableRowsUpdated(row, row);
    }
    
    public Color getRowColour(int row) {
        return rowColours.get(row);
    }
    
    //floors
    @Override
    public int getRowCount() {
        return super.getRowCount();
    }
    
    //floors
    @Override
    public void setRowCount(int rowCount) {
    	super.setRowCount(rowCount);
    }
    
    //elevators
    @Override
    public int getColumnCount() {
        return super.getColumnCount();
    }
    
    //elevators
    @Override
    public void setColumnCount(int columnCount) {
    	super.setColumnCount(columnCount);
    }

    @Override
    public Object getValueAt(int row, int column) {
        return "";
    }
    
    public boolean isElevatorInFloor(int floor, int elevator){
    	floor = getRowCount() - floor;
    	if((guiModel.getBuildingModel().getElevators().get(elevator-1).getPosition()).intValue()==floor){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    class TablaListener implements TableModelListener {
        public void tableChanged( TableModelEvent evt ) {
          System.out.println("Se modifico algun datos en la tabla : "+evt.getSource().toString());
        }
      }
}
