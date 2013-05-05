package server.handler;

import com.bluetooth.handler.DataHandler;
import com.bluetooth.handler.Message;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.PRTData;
import model.Podcar;
import model.Vertex;
import model.exception.NoSuchPodcarException;
import model.exception.NoSuchVertexException;
import server.exception.UnknownCommandException;
import server.exception.UnknownStationIDException;
import server.exception.UnknownStatusMessageException;

/**
 * Handles the data incomming from the Bluetooth server.
 *
 * @author Casper
 */
public class PodcarHandlerIN extends DataHandler {

    private Podcar connectedPodcar;

    public PodcarHandlerIN() {
    }

    @Override
    public void run() {
        Message message;
        while (true) {

            if ((message = getMessage()) != null) {
                // Retreive the data until there is no more.
                String deviceName = message.getDeviceName();
                String data = message.getData();
                System.out.println("Message from " + deviceName);

                // Find the connected podcar
                try {
                    connectedPodcar = PRTData.getPRTData().findPodcar(deviceName);
                } catch (NoSuchPodcarException exc) {
                    exc.printStackTrace();
                }

                // Handle the data
                try {
                    processData(data);
                } catch (UnknownCommandException | UnknownStationIDException | UnknownStatusMessageException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void processData(String data) throws UnknownCommandException,
            UnknownStationIDException, UnknownStatusMessageException {

        char id = data.charAt(0);
        data = data.substring(1);
        switch (id) {
            case 'S':
                id = data.charAt(0);
                switch (id) {
                    case 'C':
                        int stationID = -1;
                        data = data.substring(1);
                        id = data.charAt(0);
                        switch (id) {
                            case 'A':
                                stationID = 0;
                                break;
                            case 'B':
                                stationID = 1;
                                break;
                            case 'C':
                                stationID = 2;
                                break;
                            case 'D':
                                stationID = 3;
                                break;
                            case 'E':
                                stationID = 4;
                                break;
                            case 'F':
                                stationID = 5;
                                break;
                            case 'G':
                                stationID = 6;
                                break;
                            case 'H':
                                stationID = 7;
                                break;
                            case 'I':
                                stationID = 8;
                                break;
                            case 'J':
                                stationID = 9;
                                break;
                            case 'K':
                                stationID = -1;
                                // ignore
                                break;
                            default:
                                throw new UnknownStationIDException(id);
                        }
                        if (stationID != -1) {
                            Vertex currentStation = null;
                            // Try look up the id
                            try {
                                currentStation = PRTData.getPRTData().findVertexWithStationId(stationID);
                            } catch (NoSuchVertexException ex) {
                                Logger.getLogger(PodcarHandlerIN.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            connectedPodcar.setCurrentLocation(currentStation);
                        }

                        break;
                    default:
                        throw new UnknownStatusMessageException(id);
                }
                break;

            case 's':
                connectedPodcar.setSentStop(false);
                connectedPodcar.updateCurrentLocation();
                connectedPodcar.updateRide(2);
                break;
            case 'L':
            case 'R':
            case '0':
                connectedPodcar.updateCurrentLocation();
                connectedPodcar.updateRide(1);
                break;
            default:
                throw new UnknownCommandException(id);
        }
    }
}
