package server.handler.communication;

import com.bluetooth.BluetoothService;
import com.bluetooth.tools.BluetoothDebugger;
import java.util.List;
import server.handler.PodcarHandlerIN;
import server.handler.model.BlueToothPodcar;

/**
 *
 * @author Casper
 */
public class BTInterface {

    // The Bluetooth Service
    private BluetoothService bs;
    private static BTInterface bti;

    private BTInterface() {
        bs = new BluetoothService();
        BluetoothDebugger.VERBOSE = true;
    }

    public static BTInterface getInstance() {
        if (bti == null) {
            bti = new BTInterface();
        }
        return bti;
    }

    public void connectToPodcar(BlueToothPodcar podcar) {
        PodcarHandlerIN podHandler = new PodcarHandlerIN();
        bs.connectToDevice(podcar, podHandler);
    }

    /**
     * Connects to all the known Podcars
     */
    public void connectToPodcars(List<BlueToothPodcar> podcars) {

        // Connect to all the Podcars

        for (BlueToothPodcar car : podcars) {
            connectToPodcar(car);
        }
    }

    /**
     * Send data to a specified podcar. '*' and 'Z' are added to match the PRTP2
     * Protocol.
     *
     * @param podcar the car to send data to
     * @param data the data to be send
     */
    public void sendData(BlueToothPodcar podcar, String data) {
        bs.sendString( '*' + data + 'Z', podcar);
    }
}
