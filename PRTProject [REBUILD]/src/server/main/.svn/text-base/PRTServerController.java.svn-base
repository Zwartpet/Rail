/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.main;

import server.subservers.TableUpdateServer;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import model.PRTData;
import model.Passenger;
import model.Podcar;
import model.Station;
import model.Vertex;
import model.exception.NoSuchVertexException;
import server.handler.communication.BTInterface;
import server.handler.model.BlueToothPodcar;
import server.subservers.AndroidMobileServer;
import server.subservers.AndroidOnboardServer;
import server.subservers.AndroidStationServer;
import server.subservers.SimulationPodcarServer;
import server.views.PRTServerView;

/**
 *
 * @author Administrator
 */
public class PRTServerController {

    private PRTServerView prtServerView;
    private PRTData system;
    public static boolean simulationMode;
    private ArrayList<Thread> serverThreads;

    public PRTServerController() {
        system = PRTData.getPRTData();
        prtServerView = new PRTServerView(this);
        prtServerView.setVisible(true);
        serverThreads = new ArrayList<>();

        this.setSimulatorMode();

        // Add code to connect to podcars 

        // Add BTPodcars and Podcars to PRTData

    }

    public void loadMap() {

        URL mainURL = PRTServer.class.getResource("PRTServer.class");

        JFileChooser fileChooser = new JFileChooser(mainURL.getPath() + "../../../maps");
        int showOpenDialog = fileChooser.showOpenDialog(prtServerView);
        if (showOpenDialog == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                ArrayList<Station> stations = (ArrayList<Station>) objectInputStream.readObject();
                ArrayList<Vertex> vertexes = (ArrayList<Vertex>) objectInputStream.readObject();
                objectInputStream.close();
                fileInputStream.close();

                PRTData.getPRTData().setStations(stations);
                PRTData.getPRTData().setVertexes(vertexes);


                this.prtServerView.getLoadMapButton().setEnabled(false);
                this.prtServerView.getStartServerButton().setEnabled(true);


            } catch (FileNotFoundException ex) {
                Logger.getLogger(PRTServerController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(PRTServerController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(PRTServerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }



    }

    public void showData() {
        //TODO
    }

    public void setRealTimeMode() {
        this.simulationMode = false;
        this.prtServerView.getRealTimeButton().setBackground(Color.green);
        this.prtServerView.getSimulatorButton().setBackground(Color.red);

        stopServer();




    }

    public void setSimulatorMode() {
        this.simulationMode = true;
        this.prtServerView.getRealTimeButton().setBackground(Color.red);
        this.prtServerView.getSimulatorButton().setBackground(Color.green);
        stopServer();
    }

    public void fillPRTData() {
        Vertex loc1 = null;
        Vertex loc2 = null;
        try {
            loc1 = PRTData.getPRTData().findVertex("N17_9");
            loc2 = PRTData.getPRTData().findVertex("N14_9"); //edit the vertex for each podcar
        } catch (NoSuchVertexException exc) {
            exc.printStackTrace();
        }

        Podcar podcar1 = new Podcar("PRTTAXI1", loc1);
        BlueToothPodcar bluetoothPodcar1 = new BlueToothPodcar("PRTTAXI1", "0007804C4658");
        podcar1.setBtPodcar(bluetoothPodcar1);

        PRTData.getPRTData().addPodcar(podcar1);
        BTInterface.getInstance().connectToPodcar(podcar1.getBtPodcar());

        Podcar podcar2 = new Podcar("PRTTAXI2", loc2);
        BlueToothPodcar bluetoothPodcar2 = new BlueToothPodcar("PRTTAXI2", "0007804C4650");
        podcar2.setBtPodcar(bluetoothPodcar2);

        PRTData.getPRTData().addPodcar(podcar2);
        BTInterface.getInstance().connectToPodcar(podcar2.getBtPodcar());

// "N15_6"




        //        btPodcars.add(new server.handler.model.Podcar("PRTTAXI1", "0007804C4658"));

//        btPodcars.add(new server.handler.model.Podcar("PRTTAXI3", "00078096E0EE"));




//        Vertex loc1 = PRTData.getPRTData().findVertex("N10_7"); //edit the vertex for each podcar
//        Podcar p1 = new Podcar("PRTTAXI1", 4, loc1);
//        p1.setBtPodcar(btPodcars.get(0));
//        podcars.add(p1);






//        Vertex loc3 = PRTData.getPRTData().findVertex("N10_7"); //edit the vertex for each podcar
//        Podcar p3 = new Podcar("PRTTAXI3", 4, loc3);
//        p3.setBtPodcar(btPodcars.get(2));
//        podcars.add(p3);



        // Add the data to the PRTData Singleton
//        PRTData.getPRTData().setBTPodcars(btPodcars);
//        PRTData.getPRTData().setPodcars(podcars);
    }

    public void startServer() {
        if (!simulationMode) {
            fillPRTData();
        } else {

            SimulationPodcarServer simulationPodcarServer = new SimulationPodcarServer();
            Thread simulationThread = new Thread(simulationPodcarServer, "Simulation Thread");
            serverThreads.add(simulationThread);

        }

        TableUpdateServer tableUpdateServer = new TableUpdateServer(this);
        AndroidMobileServer androidMobileServer = new AndroidMobileServer();
        AndroidOnboardServer androidOnboardServer = new AndroidOnboardServer();
        AndroidStationServer androidStationServer = new AndroidStationServer();

        Thread androidMobileServerThread = new Thread(androidMobileServer, "Android Mobile Server Thread");
        Thread androidOnboardServerThread = new Thread(androidOnboardServer, "Android Onboard Server Thread");
        Thread androidStationServerThread = new Thread(androidStationServer, "Android Station Server Thread");
        Thread tableUpdateServerThread = new Thread(tableUpdateServer, "Table Update Server Thread");

        //  tableUpdateServerThread.setPriority(Thread.MIN_PRIORITY);

        serverThreads.add(androidMobileServerThread);
        serverThreads.add(androidOnboardServerThread);
        serverThreads.add(androidStationServerThread);
        serverThreads.add(tableUpdateServerThread);

        for (Thread thread : serverThreads) {
            thread.start();
        }



        this.prtServerView.getStartServerButton().setEnabled(false);
        this.prtServerView.getStopServerButton().setEnabled(true);



    }

    public void stopServer() {
        if (serverThreads != null) {
            for (Thread thread : serverThreads) {
                if (thread != null) {
                    thread.stop();
                }
            }

            this.prtServerView.getStartServerButton().setEnabled(true);
            this.prtServerView.getStopServerButton().setEnabled(false);
        }


    }

    public PRTServerView getPrtServerView() {
        return prtServerView;
    }

    public void generatePodcars() {
        int numberGenerated = Integer.parseInt(this.prtServerView.getNumberOfPodcarsMustBeGenerated().getText());
        for (int i = 0; i < numberGenerated; i++) {
            Podcar podcar = new Podcar("GEN" + i, PRTData.getPRTData().getVertexes().get(new Random().nextInt(PRTData.getPRTData().getVertexes().size())));
            PRTData.getPRTData().addPodcar(podcar);
        }
    }
}
