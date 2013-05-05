/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mapmaker;

import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import model.Podcar;

/**
 *
 * @author Bennet
 */
public class PodcarTableModel extends AbstractTableModel{

    private String[] columnNames = {"ID", "Start X", "Start Y", "Is Realtime", "BT Name", "BT Adress"};
    ArrayList<Podcar> allPodcars;

    PodcarTableModel(MapMakerController makerController) {
        allPodcars = makerController.getAllPodcars();
        
        JCheckBox isRealtimeCheckBox = new JCheckBox();               
    
    }

    @Override
    public int getRowCount() {
        return allPodcars.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];

    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Podcar tempPodcar = allPodcars.get(rowIndex);

        boolean isRealtime = false;
        if (tempPodcar.getBtPodcar() != null) {
            isRealtime = true;
        }

        switch (columnIndex) {
            case 0:
                return tempPodcar.getId();
            case 1:
                return tempPodcar.getParkingLocation().getxPosition();
            case 2:
                return tempPodcar.getParkingLocation().getyPosition();
            case 3:
                return isRealtime;

            case 4:
                if (isRealtime) {
                    return tempPodcar.getBtPodcar().getName();
                } else {
                    return null;
                }

            case 5:
                if (isRealtime) {
                    return tempPodcar.getBtPodcar().getAddress();
                } else {
                    return null;
                }
                
        

        }


        return null;

    }
    
    public Class getColumnClass(int columnIndex) {
       switch (columnIndex) {
           case 0: case 4: case 5:
               return String.class;
               
           case 1: case 2:
               return Integer.class;
            
           case 3:
               return Boolean.class;
                
        

       }
        return null;
    }
    

    @Override
    public boolean isCellEditable(int row, int column) {
        if(column > 2){
            return true;
        }
        return false;
    }



    
}
