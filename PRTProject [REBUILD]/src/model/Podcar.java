/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import Pathfinder.Pathfinder;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import listeners.LocationListener;
import model.exception.NoSuchOnBoardAppException;
import model.exception.NoSuchStationException;
import server.handler.PathTranslator;
import server.handler.RideHandler;
import server.handler.communication.BTInterface;
import server.handler.model.BlueToothPodcar;
import server.handler.model.OnBoardApp;
import server.main.PRTServer;
import server.main.PRTServerController;

/**
 *
 * @author Bennet
 */
public class Podcar {

    private String podcarId;
    private Vertex currentLocation;
    private Path path;
    private List<LocationListener> locListeners;
    private BlueToothPodcar btPodcar;
    private Ride ride;
    private Vertex parkingLocation;
    private boolean rideChanged;
    private boolean sentStop;

    private enum RideModus {

        PICKUPMODE, TRANSPORTMODE, PARKINGMODE, IDLEMODE
    };
    private RideModus rideModus;

    public Podcar(String id, Vertex defaultLocation) {
        this.podcarId = id;
        this.locListeners = new ArrayList<>();
        this.parkingLocation = defaultLocation;
        setCurrentLocation(defaultLocation);
    }

    public String getId() {
        return podcarId;
    }

    public Vertex getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Vertex currentLocation) {
        this.currentLocation = currentLocation;
        // Update all listeners
        try {
        for (LocationListener listener : locListeners) {
            listener.updateLocation(currentLocation.getLabel());
        }
        }catch(ConcurrentModificationException exc){
            System.out.println(exc);
        }
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path pathP) {
        this.path = pathP;
    }

    public void registerLocationListener(LocationListener listener) {
        locListeners.add(listener);
    }

    public BlueToothPodcar getBtPodcar() {
        return btPodcar;
    }

    public void setBtPodcar(BlueToothPodcar btPodcar) {
        this.btPodcar = btPodcar;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    public void setSentStop(boolean sentStop) {
        this.sentStop = sentStop;
    }

    public void startRide() {
        OnBoardApp onBoardApp = null;
        Vertex tempDes = null;
        Station tempStat = null;

        Path toPickupStationPath = new Path();
        Pathfinder pathFinder = Pathfinder.getInstance();
        toPickupStationPath = pathFinder.Astar(currentLocation, ride.getStartVertex(), toPickupStationPath);
        this.path = toPickupStationPath;
        this.rideModus = RideModus.PICKUPMODE;

        try {
            onBoardApp = PRTData.getPRTData().findOnBoardApp(this);
        } catch (NoSuchOnBoardAppException ex) {
           // ignore
        }

        if (btPodcar != null) {
            String translation = PathTranslator.translate(toPickupStationPath);
            translation = translation.substring(0, 2);

            BTInterface.getInstance().sendData(btPodcar, translation);
        }

        if (onBoardApp != null) {
            tempDes = ride.getStartVertex();
            try {
                tempStat = PRTData.getPRTData().findStationWithVertex(tempDes);
            } catch (NoSuchStationException ex) {
                Logger.getLogger(Podcar.class.getName()).log(Level.SEVERE, null, ex);
            }

            onBoardApp.updateDestination("Station " + tempStat.getName());
        }

        path.removeFirstVertex();
        path.removeFirstVertex();
    }

    public void updateRide(int commands) {
        if (path == null) {
            return;
        } else if (path.getPath().size() <= 0) {
            return;
        }


        Path toDestinationPath = new Path();
        Pathfinder pathFinder = Pathfinder.getInstance();

        switch (rideModus) {
            case PICKUPMODE:
                toDestinationPath = pathFinder.Astar(currentLocation, ride.getStartVertex(), toDestinationPath);

                break;
            case TRANSPORTMODE:
                toDestinationPath = pathFinder.Astar(currentLocation, ride.getEndVertex(), toDestinationPath);
                break;
            case PARKINGMODE:
                if (rideChanged) {
                    Ride queuedRide = RideHandler.getInstance().getQueuedRide();
                    if (queuedRide != null) {
                        setRide(queuedRide);
                        startRide();
                        return; // Skip the rest.
                    }
                }
//                    RideHandler.getInstance().unreservePodcar(this);
//                    parkingLocation = PRTData.getPRTData().getParkingLocation();
                toDestinationPath = pathFinder.Astar(currentLocation, parkingLocation, toDestinationPath);

                break;
        }

        this.path = toDestinationPath;
        if (btPodcar != null && !rideChanged && !sentStop) {
            String translation = PathTranslator.translate(toDestinationPath);
            translation = translation.substring(0, commands);

            BTInterface.getInstance().sendData(btPodcar, translation);
            for (int i = 0; i < commands; i++) {
                path.removeFirstVertex();
            }
        } else if (btPodcar != null && rideChanged) { //When we change the ride we've got to build a buffer in the ivibot again
            String translation;// = PathTranslator.translate(toDestinationPath);
            translation = "S";// + translation.substring(0, 1); //Send 2 commands instead of 1 to build a buffer
            sentStop = true;
            rideChanged = false;
            BTInterface.getInstance().sendData(btPodcar, translation);
            //path.removeFirstVertex();
        } else if (PRTServerController.simulationMode) {
            path.removeFirstVertex();
        }
    }

    public void updateCurrentLocation() {
        OnBoardApp onBoardApp = null;
        Vertex tempDes = null;
        Station tempStat = null;
        if (path == null) {
            return;
        } else if (path.getPath().size() <= 0) {
            return;
        }

        try {
            onBoardApp = PRTData.getPRTData().findOnBoardApp(this);
        } catch (NoSuchOnBoardAppException ex) {
            //ignore >:D
        }

        setCurrentLocation(path.peekFirstVertex());
        if (currentLocation == ride.getStartVertex() && rideModus == RideModus.PICKUPMODE) {
            rideModus = RideModus.TRANSPORTMODE;
            if (onBoardApp != null) {
                tempDes = ride.getEndVertex();
                try {
                    tempStat = PRTData.getPRTData().findStationWithVertex(tempDes);
                } catch (NoSuchStationException ex) {
                    Logger.getLogger(Podcar.class.getName()).log(Level.SEVERE, null, ex);
                }
                onBoardApp.updateDestination("Station " + tempStat.getName());
            }
            rideChanged = true;
        } else if (currentLocation == ride.getEndVertex() && rideModus == RideModus.TRANSPORTMODE) {
            rideModus = RideModus.PARKINGMODE;
            if (onBoardApp != null) {
                onBoardApp.updateDestination("Parking location: " + parkingLocation.label);
            }
            rideChanged = true;
        } else if (currentLocation == parkingLocation && rideModus == RideModus.PARKINGMODE) {
            rideModus = RideModus.IDLEMODE;
            if (onBoardApp != null) {
                onBoardApp.updateDestination("No Destination");
            }
            rideChanged = true;
            RideHandler.getInstance().unreservePodcar(this);
        }
    }

    public Vertex getParkingLocation() {
        return parkingLocation;
    }

    public RideModus getRideModus() {
        return rideModus;
    }

    public Ride getRide() {
        return ride;
    }
}
