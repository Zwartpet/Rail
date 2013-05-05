package server.handler;

import Pathfinder.Pathfinder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import model.PRTData;
import model.Path;
import model.Podcar;
import model.Ride;
import model.Vertex;

/**
 *
 * @author Casper
 */
public class RideHandler {

    private List<Podcar> availablePodcars;
    private static RideHandler rh;
    private Lock threadLock;
    private Queue<Ride> rides;

    private RideHandler() {
        availablePodcars = new ArrayList<>();
        availablePodcars.addAll(PRTData.getPRTData().getPodcars());
        threadLock = new ReentrantLock(true);
        rides = new LinkedList<>();
    }

    public static RideHandler getInstance() {
        if (rh == null) {
            rh = new RideHandler();
        }
        return rh;
    }

    public Podcar getNearestPodcar(Vertex location) {
        Podcar bestCar = null;

        // Lock the rest of the method so there will never be selected 1 podcar 
        // for multiple device requests.
        threadLock.lock();
        try {
            // If there are no available podcars, wait until there is one.
            if (availablePodcars.isEmpty()) {
                return bestCar;
            }

            if (availablePodcars.size() > 1) {
                //  Find with AStar the closest PODCar


                int minVertices = Integer.MAX_VALUE;
                Path p;
                for (Podcar car : availablePodcars) {
                    p = new Path();
                    Pathfinder algo = Pathfinder.getInstance();
                    p = algo.Astar(car.getCurrentLocation(), location, p);
                    if (p.getPath().size() < minVertices) {
                        bestCar = car;
                        minVertices = p.getPath().size();
                    }
                }
            } else if (availablePodcars.size() == 1) {
                bestCar = availablePodcars.get(0);
            }

            reservePodcar(bestCar);
        } finally {
            threadLock.unlock();
        }
        return bestCar;

    }

    private void reservePodcar(Podcar podcar) {
        availablePodcars.remove(podcar);
    }

    public void unreservePodcar(Podcar podcar) {
        availablePodcars.add(podcar);
    }

    public void addRide(Ride ride) {
        rides.add(ride);
    }

    public Ride getQueuedRide() {
        return rides.poll();
    }

    public Queue<Ride> getRides() {
        return rides;
    }
}
