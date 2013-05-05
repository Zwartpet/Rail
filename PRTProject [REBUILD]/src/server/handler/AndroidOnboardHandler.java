package server.handler;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import model.Constants;
import model.PRTData;
import model.Podcar;
import model.Vertex;
import model.exception.NoSuchOnBoardAppException;
import model.exception.NoSuchPodcarException;
import model.exception.NoSuchVertexException;
import server.handler.model.BlueToothPodcar;
import server.handler.model.OnBoardApp;

/**
 *
 * @author Casper
 */
public class AndroidOnboardHandler extends StaticObjectHandler implements Runnable, Constants {

    private final Socket clientSocket;
//    private String[] testStations = {"Station A", "Station B", "Station C", "Station D", "Station E", "Station F", "Station G", "Station H", "Station I", "Station J"};
    private String[] testPodcars = {"PRTTAXI1", "PRTTAXI2", "Podcar 3", "Podcar 4"};
    private int nStations;
    private InputStream inputStream;
    private DataInputStream in;
    private OutputStream outputStream;
    private DataOutputStream out;
    private ObjectOutputStream ous;
    private BufferedReader br;
    private String clientIP;
    private PodcarHandlerOUT podcarHandler;
    private OnBoardApp onBoardApps;

    public AndroidOnboardHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.clientIP = clientSocket.getInetAddress().toString();
        clientIP = clientIP.substring(1);
        this.nStations = 0;

        this.podcarHandler = new PodcarHandlerOUT();

        try {
            inputStream = clientSocket.getInputStream();
            in = new DataInputStream(inputStream);

            outputStream = clientSocket.getOutputStream();
            out = new DataOutputStream(outputStream);
            ous = new ObjectOutputStream(outputStream);

            br = new BufferedReader(new InputStreamReader(inputStream));

        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    public void androidPodcarChangeDestination(String idNode) {
    }

    public void androidPodcarEmergencyStop() {
        PRTData prtData = PRTData.getPRTData();
        OnBoardApp app = null;
        try {
            app = prtData.findOnBoardApp(clientIP);
        } catch (NoSuchOnBoardAppException exc) {
            exc.printStackTrace();
        }
        Podcar p = app.getPodcar();

        BlueToothPodcar BTCar = p.getBtPodcar();
        podcarHandler.pushCommand(BTCar, "S");
    }

    @Override
    public void run() {

        try {
            int hello = in.readInt();
            System.out.println(hello);
            if (hello == HELLO_INT) {
                byte functionNumber = in.readByte();
                switch (functionNumber) {
                    case 000:
                        // Hello functie, inlezen welke podcar met welke ID verbonden.
                        // Bij verandering podcar wordt deze fucntie opnieuw aangeroepen.
                        System.out.println("Functie 000 is aangeroepen");
                        OnBoardApp obaTemp = new OnBoardApp();
                        Podcar podcar = null;
                        String deviceId;

                        deviceId = br.readLine();

                        // Lookup the PODCar
                        try {
                            podcar = PRTData.getPRTData().findPodcar(deviceId);
                        } catch (NoSuchPodcarException exc) {
                            exc.printStackTrace();
                        }

                        obaTemp.setIpAdress(clientIP);
                        obaTemp.setPodcar(podcar);

                        // Insert the oba or modify it.
                        PRTData.getPRTData().insertOBA(obaTemp, clientIP);
                        changeAppLocation(podcar.getCurrentLocation().label);
                        ous.write(250);
                        break;
                    case 001:
                        //vraagt alle stations op
                        System.out.println("Functie 001 is aangeroepen");
                        //Stuur alle stations
                        ous.writeObject(PRTData.getPRTData().getStationNamesArray());
                        ous.flush();

                        if (in.readInt() == 250) {
                            System.out.println("Sending stations complete!");
                        }
                        break;

                    case 002:
                        //vraag all podcarnames op
                        System.out.println("Functie 002 is aangeroepen");
                        //Stuur alle podcars
                        ous.writeObject(PRTData.getPRTData().getPodcarNamesArray());
                        ous.flush();
                        if (in.readInt() == 250) {
                            System.out.println("Sending array complete!");
                        }
                        break;

                    case 003:
                        // Change the destination
                        System.out.println("Functie 003 is aangeroepen");

                        int idNodeTo = in.readInt();

                        Vertex to = null;
                        // Try look up the to Vertex
                        try {
                            to = PRTData.getPRTData().findVertexWithStationId(idNodeTo);
                        } catch (NoSuchVertexException ex) {
                            ex.printStackTrace();
                        }


                        // Find the podcar object
                        List<OnBoardApp> onBAList = PRTData.getPRTData().getOnBoardApps();
                        Podcar theCar = null;
                        for (OnBoardApp oba : onBAList) {
                            if (oba.getIpAdress().equals(clientIP)) {
                                theCar = oba.getPodcar();
                                break;
                            }
                        }

                        // Change the destination
                        this.changeDestination(theCar, to);
                        changeAppDestination(to.getLabel());
                        System.out.println(idNodeTo);
                        break;
                    case 004:
                        System.out.println("Functie 004 is aangeroepen");
                        androidPodcarEmergencyStop();
                        break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void changeAppDestination(String dest) {
        Socket socket = null;
        DataOutputStream dos = null;
        try {
            socket = new Socket(clientIP, 6666);
            dos = new DataOutputStream(socket.getOutputStream());
            dos.writeBytes("D" + dest + "\n");
            dos.flush();
        } catch (IOException exc) {
            exc.printStackTrace();
        } finally {
            try {
                dos.close();
                socket.close();
            } catch (IOException exc) {
                exc.printStackTrace();
            }
        }
    }
    
    public void changeAppLocation(String loc) {
        Socket socket = null;
        DataOutputStream dos = null;
        try {            
            socket = new Socket(clientIP, 6666);
            dos = new DataOutputStream(socket.getOutputStream());
            dos.writeBytes("L" + loc + "\n");
            dos.flush();
        } catch (IOException exc) {
            exc.printStackTrace();
        } finally {
            try {
                dos.close();
                socket.close();
            } catch (IOException exc) {
                exc.printStackTrace();
            }
        }
    }
}
