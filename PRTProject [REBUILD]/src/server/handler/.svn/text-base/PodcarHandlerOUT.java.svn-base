/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.handler;

import server.handler.communication.BTInterface;
import server.handler.model.BlueToothPodcar;

/**
 *
 * @author Casper
 */
public class PodcarHandlerOUT {

    private BTInterface btInterface;

    public PodcarHandlerOUT() {
        btInterface = BTInterface.getInstance();
    }

    public void pushCommand(BlueToothPodcar podcar, String command) {
        btInterface.sendData(podcar, command);
    }
}
