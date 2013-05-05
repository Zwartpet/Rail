/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.subservers;

import java.util.ArrayList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import model.PRTData;
import model.Podcar;
import model.Ride;
import model.Station;
import model.Vertex;
import server.handler.RideHandler;
import server.main.PRTServerController;

/**
 *
 * @author John
 */
public class TableUpdateServer implements Runnable {

    private final PRTServerController serverController;

    public TableUpdateServer(PRTServerController pRTServerController) {
        this.serverController = pRTServerController;
    }

    @Override
    public void run() {
        ArrayList<Podcar> podcars = PRTData.getPRTData().getPodcars();
        DefaultTableModel podcarTablemodel = (DefaultTableModel) serverController.getPrtServerView().getPodcarTable().getModel();
        DefaultTableModel ridesTabelModel = (DefaultTableModel) serverController.getPrtServerView().getRidesTable().getModel();
        DefaultTableModel bufferedTabelModel = (DefaultTableModel) serverController.getPrtServerView().getBufferedRidesTable().getModel();

        for (Podcar podcar : podcars) {
            podcarTablemodel.addRow(new Object[]{podcar.getId(), podcar.getCurrentLocation().getLabel(), podcar.getRideModus(), podcar.getParkingLocation().getLabel()});
            ridesTabelModel.addRow(new Object[3]);


        }

        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(TableUpdateServer.class.getName()).log(Level.SEVERE, null, ex);
            }



            for (int i = 0; i < podcars.size(); i++) {
                Podcar podcar = podcars.get(i);
                podcarTablemodel.setValueAt(podcar.getId(), i, 0);
                podcarTablemodel.setValueAt(podcar.getCurrentLocation().getLabel(), i, 1);
                podcarTablemodel.setValueAt(podcar.getRideModus(), i, 2);
                podcarTablemodel.setValueAt(podcar.getParkingLocation().getLabel(), i, 3);
                //handle rides
                Ride ride = podcar.getRide();
                if (ride != null) {
                    String startName = getName(ride.getStartVertex());
                    String stopName = getName(ride.getEndVertex());
                    ridesTabelModel.setValueAt(startName, i, 0);
                    ridesTabelModel.setValueAt(stopName, i, 1);
                    ridesTabelModel.setValueAt(podcar.getId() + " - " + podcar.getRideModus(), i, 2);
                } else {
                    ridesTabelModel.setValueAt("", i, 0);
                    ridesTabelModel.setValueAt("", i, 1);
                    ridesTabelModel.setValueAt("", i, 2);
                }

            }
            Queue<Ride> rides = RideHandler.getInstance().getRides();
            Object[] ridesArray = rides.toArray();

            for (int i = 0; i < ridesArray.length; i++) {
                Ride ride = (Ride) ridesArray[i];
                String startName = getName(ride.getStartVertex());
                String stopName = getName(ride.getEndVertex());
                bufferedTabelModel.setValueAt(startName, i, 0);
                bufferedTabelModel.setValueAt(stopName, i, 1);
                bufferedTabelModel.setValueAt("BUFFERD", i, 2);
            }

            for (int i = ridesArray.length; i < bufferedTabelModel.getRowCount(); i++) {
                bufferedTabelModel.setValueAt("", i, 0);
                bufferedTabelModel.setValueAt("", i, 1);
                bufferedTabelModel.setValueAt("", i, 2);
            }
        }
    }

    private String getName(Vertex vertex) {
        ArrayList<Station> stations = PRTData.getPRTData().getStations();
        for (Station station : stations) {
            if (station.getxPosition() == vertex.getxPosition() && station.getyPosition() == vertex.getyPosition()) {
                return station.getName();
            }
        }

        return vertex.getLabel();
    }
}
