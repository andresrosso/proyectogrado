package org.arosso.gui;

import java.awt.Color;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import org.arosso.model.BuildingModel;
import org.arosso.model.Elevator;
import org.arosso.model.Passenger;

public class ElevatorTableModel extends DefaultTableModel {

	private static final long serialVersionUID = 1L;
	private GuiModel guiModel;
	private BuildingModel buildingModel;
	
	
	public ElevatorTableModel(GuiModel guiModel) {
		this.guiModel = guiModel;
		try {
			buildingModel = BuildingModel.getInstance();
			addTableModelListener( new TablaListener() );
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
    	if(((guiModel.getBuildingModel().getElevators().get(elevator-1).getPosition()).intValue()+1)==floor){
    		return true;
    	}else{
    		return false;
    	}
    } 
    
    public String callsInFloor(int floor){
    	String callsInFloor = "";
    	floor = getRowCount() - floor;
    	for( Elevator el : guiModel.getBuildingModel().getElevators() ){
    		Vector<Passenger> calls = (Vector<Passenger>)el.getCalls().clone();
    		for(Passenger pass : calls){
    			if(pass.getOriginFloor()==floor){
    				callsInFloor += pass;
    			}
    		}
    	}
    	return callsInFloor;
    } 

    public Elevator.State getState(int elevator){
    	return guiModel.getBuildingModel().getElevators().get(elevator-1).getState();
    }
    
    public String getPassenger(int elevator){
    	return guiModel.getBuildingModel().getElevators().get(elevator-1).getPassengers().toString();
    }
    
    class TablaListener implements TableModelListener {
        public void tableChanged( TableModelEvent evt ) {
          System.out.println("Se modifico algun dato en la tabla : "+evt.getSource().toString());
        }
      }

	public BuildingModel getBuildingModel() {
		return buildingModel;
	}

	public void setBuildingModel(BuildingModel buildingModel) {
		this.buildingModel = buildingModel;
	}
}
