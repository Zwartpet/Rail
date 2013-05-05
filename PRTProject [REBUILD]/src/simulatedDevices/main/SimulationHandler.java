/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulatedDevices.main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Administrator
 */
public class SimulationHandler implements Runnable {

    private String[] stationNames;
    private final SimulationController simulationController;
    private boolean running = false;

    SimulationHandler(SimulationController simulationController) {
        this.simulationController = simulationController;
    }

    @Override
    public void run() {
        getStations();
        generateRandomRequest();

        while (running) {

            int perHowMuchSeconds = Integer.parseInt(simulationController.getSimulationView().getPerHowMuchSecondsTextField().getText());


            long diffTime = 0;
            long startTime = System.nanoTime();




            while (true) {
                long estimatedTime = System.nanoTime() - startTime;

                int percentageFinished = (int) ((((double) estimatedTime / 1000000000) / perHowMuchSeconds) * 100);
                simulationController.getSimulationView().getToNextRideProgressBar().setValue(percentageFinished + 2);


                if (estimatedTime / 1000000000 >= perHowMuchSeconds) {

                    getStations();
                    generateRandomRequest();



                    break;
                }
            }

        }



    }

    private void getStations() {
        try {
            Socket socket = new Socket("localhost", 3333);

            InputStream inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);


            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            dataOutputStream.writeInt(1337);
            dataOutputStream.writeByte(000);
            this.stationNames = (String[]) objectInputStream.readObject();
            dataOutputStream.writeInt(250);
            dataOutputStream.flush();
            socket.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SimulationHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SimulationHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void generateRandomRequest() {
        try {
            Socket socket = new Socket("localhost", 3333);

            InputStream inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);


            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            int stationFrom = new Random().nextInt(stationNames.length);
            int stationTo = stationFrom;
            while (stationFrom == stationTo) {
                stationTo = new Random().nextInt(stationNames.length);
            }


            dataOutputStream.writeInt(1337);
            dataOutputStream.writeByte(001);
            dataOutputStream.writeInt(stationFrom);
            dataOutputStream.writeInt(stationTo);

            DefaultTableModel model = (DefaultTableModel) this.simulationController.getSimulationView().getRideTable().getModel();
            model.insertRow(0, new Object[]{"001", stationNames[stationFrom], stationNames[stationTo], "Reserved"});
        } catch (IOException ex) {
            Logger.getLogger(SimulationHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
