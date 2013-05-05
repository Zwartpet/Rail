/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.subservers;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.PRTData;
import model.Podcar;

/**
 *
 * @author Razzy
 */
public class SimulationPodcarServer implements Runnable {

    public SimulationPodcarServer() {
    }

    @Override
    public void run() {

        ArrayList<Podcar> podcars = PRTData.getPRTData().getPodcars();
        while (true) {
            for (Podcar podcar : podcars) {
                podcar.updateCurrentLocation();
                podcar.updateRide(1);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SimulationPodcarServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
