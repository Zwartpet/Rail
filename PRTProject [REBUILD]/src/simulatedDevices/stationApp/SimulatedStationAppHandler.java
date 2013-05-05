/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulatedDevices.stationApp;

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

/**
 *
 * @author Administrator
 */
public class SimulatedStationAppHandler implements Runnable {
    private String[] stationNames;

    @Override
    public void run() {
        
            getStations();
            generateRandomRequest();

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
            Logger.getLogger(SimulatedStationAppHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SimulatedStationAppHandler.class.getName()).log(Level.SEVERE, null, ex);
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
            dataOutputStream.writeInt(0);
            dataOutputStream.writeInt(2);
        } catch (IOException ex) {
            Logger.getLogger(SimulatedStationAppHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
