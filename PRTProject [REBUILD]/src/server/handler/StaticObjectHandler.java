/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.handler;

import java.util.logging.Level;
import java.util.logging.Logger;
import model.PRTData;
import model.Path;
import model.Podcar;
import model.Ride;
import model.Vertex;
import model.exception.NoSuchVertexException;

/**
 *
 * @author Casper
 */
public class StaticObjectHandler {

    private static PodcarHandlerOUT podHandler;
    private RideHandler rideHandler;

    public StaticObjectHandler() {
        podHandler = new PodcarHandlerOUT();
        rideHandler = RideHandler.getInstance();
    }

    public void allocateRide(int idFrom, int idTo) {
        Vertex fromStation = null;
        Vertex toStation = null;;

        // Try to find the vertex with the given ID.
        try {
            fromStation = PRTData.getPRTData().findVertexWithStationId(idFrom);
            toStation = PRTData.getPRTData().findVertexWithStationId(idTo);
        } catch (NoSuchVertexException ex) {
            Logger.getLogger(StaticObjectHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        Podcar nearestPodcar = rideHandler.getNearestPodcar(fromStation);


        Ride transportRide = new Ride(fromStation, toStation);

        // There is a car available, add the ride to the car.
        if (nearestPodcar != null) {
            System.out.println("Available podcar found");
            nearestPodcar.setRide(transportRide);
            nearestPodcar.startRide();
        } else { // No car currently available, add to the queue
            rideHandler.addRide(transportRide);
        }








        //        Path pathToPassenger = new Path();
        //        Path pathToDestination = new Path();
        //        Podcar serverPodcar = null;
        //
        //        BlueToothPodcar reservedBTPodcar = null;
        //
        ////        if (!PRTServerController.simulationMode) {
        //            reservedBTPodcar = rideHandler.getNearestPodcarRealTime(from);
        //            serverPodcar = PRTData.getPRTData().findPodcar(reservedBTPodcar.getName());
        //        } else {
        //            Podcar reservedPodcar;
        //            reservedPodcar = rideHandler.getNearestPodcar(from);
        //            serverPodcar = reservedPodcar;
        //        }
        //
        //
        //        serverPodcar.setDestination(to);
        //
        //        Pathfinder.Pathfinder algo = Pathfinder.Pathfinder.getInstance();
        //        // Find the path to the station where called from.
        //        pathToPassenger = algo.Astar(serverPodcar.getCurrentLocation(), from, pathToPassenger);
        //        pathToDestination = algo.Astar(from, to, pathToDestination);
        //
        //        // Translate the path to LRRLRLRLR21N
        //
        //
        //        // Send command
        //        if (!PRTServerController.simulationMode) {
        //            String command = null;
        //            command = PathTranslator.translate(pathToPassenger);
        //            System.out.println(command);
        //            podHandler.pushCommand(reservedBTPodcar, command); //.substring(0, 2)
        //            try {
        //                Thread.sleep(1000);
        //            } catch (InterruptedException ex) {
        //                Logger.getLogger(StaticObjectHandler.class.getName()).log(Level.SEVERE, null, ex);
        //            }
        //            command = PathTranslator.translate(pathToDestination);
        //            podHandler.pushCommand(reservedBTPodcar, command);
        //        }
        ////        // Adjust path and command
        ////        command = command.substring(2);
        ////        path.removeFirstVertex();
        ////        path.removeFirstVertex();
        //
        //        serverPodcar.setPath(pathToDestination);
    }

    public static void changeDestination(model.Podcar pod, Vertex to) {
        Path path = new Path();
        server.handler.model.BlueToothPodcar podBT = null;
        Vertex from = pod.getCurrentLocation();

        Pathfinder.Pathfinder algo = Pathfinder.Pathfinder.getInstance();
        path = algo.Astar(from, to, path);

        // Translate the path to LRRLRLRLR21N
        String command = null;
        command = PathTranslator.translate(path);
        // Translate the Podcar to BTPodcar for Server -> PODCar communication
        podBT = pod.getBtPodcar();

//        // Adjust path and command
//        command = command.charAt(0) + "";
//        path.removeFirstVertex();
        // Send command
        podHandler.pushCommand(podBT, command);
    }
}
