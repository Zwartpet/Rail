package model;

import java.util.ArrayList;
import java.util.List;
import model.exception.NoSuchOnBoardAppException;
import model.exception.NoSuchPodcarException;
import model.exception.NoSuchStationException;
import server.handler.model.BlueToothPodcar;
import server.handler.model.OnBoardApp;
import model.exception.NoSuchVertexException;

/**
 *
 * @author Administrator
 */
public class PRTData {

    private String saveFilesLocation = "";
    private static PRTData system;
    private ArrayList<Podcar> podcars;
    private ArrayList<Station> stations;
    private ArrayList<Vertex> vertexes;
    private ArrayList<Passenger> passengers;
    private ArrayList<BlueToothPodcar> btPodcars;
    private List<OnBoardApp> onBoardApps;
    private Vertex parkingLocation;

    private PRTData() {
        podcars = new ArrayList<>();
        stations = new ArrayList<>();
        vertexes = new ArrayList<>();
        onBoardApps = new ArrayList<>();
    }

    public static PRTData getPRTData() {
        if (system == null) // it's ok, we can call this constructor
        {
            system = new PRTData();
        }
        return system;
    }

    public List<OnBoardApp> getOnBoardApps() {
        return onBoardApps;
    }

    public Vertex findVertex(String label) throws NoSuchVertexException {
        for (Vertex vertex : vertexes) {
            if (vertex.label.equals(label)) {
                return vertex;
            }
        }
        throw new NoSuchVertexException("No such Vertex as '" + label + "' was defined");
    }

    public void setOnBoardApps(List<OnBoardApp> onBoardApps) {
        this.onBoardApps = onBoardApps;
    }

    public String[] getStationNamesArray() {
        String[] stationNames = new String[stations.size()];
        for (int i = 0; i < stationNames.length; i++) {
            stationNames[i] = stations.get(i).getName();
        }
        return stationNames;

    }

    public void setParkingLocation(Vertex parkingLocation) {
        this.parkingLocation = parkingLocation;
    }

    public Podcar findPodcar(String deviceName) throws NoSuchPodcarException {
        for (Podcar car : podcars) {
            if (car.getBtPodcar().getName().equals(deviceName)) {
                return car;
            }
        }
        throw new NoSuchPodcarException("No such Podcar as '" + deviceName + "' was defined");
    }

//    public BlueToothPodcar findBTPodcar(String deviceName) {
//        BlueToothPodcar podcar = null;
//        for (BlueToothPodcar car : btPodcars) {
//            if (car.getName().equals(deviceName)) {
//                podcar = car;
//                break;
//            }
//        }
//        return podcar;
//    }
    public Vertex findVertexWithStationId(int id) throws NoSuchVertexException {
        Station toFind = null;
        for (int i = 0; i < stations.size(); i++) {
            if (stations.get(i).getId() == id) {
                toFind = stations.get(i);
                break;
            }
        }

        // Find in vertexces
        for (Vertex v : vertexes) {
            if ((toFind.getxPosition() == v.getxPosition()) && (toFind.getyPosition() == v.getyPosition())) {
                return v;
            }
        }
        // not found
        throw new NoSuchVertexException("No such Vertex with id '" + id + "' was defined");
    }
    
    public Station findStationWithVertex(Vertex vertex) throws NoSuchStationException{
        for (Station station : stations) {
            if ((station.getxPosition() == vertex.getxPosition()) && (station.getyPosition() == vertex.getyPosition())) {
                return station;
            }
        }
        throw new NoSuchStationException("No such Station with vertex was defined");
    }

    public String[] getPodcarNamesArray() {
        String[] podcarNames = new String[podcars.size()];
        for (int i = 0; i < podcarNames.length; i++) {
            podcarNames[i] = podcars.get(i).getId();
        }

        return podcarNames;
    }

    public ArrayList<Podcar> getPodcars() {
        return podcars;
    }

    public ArrayList<Station> getStations() {
        return stations;
    }

    public ArrayList<Vertex> getVertexes() {
        return vertexes;
    }

    public void setStations(ArrayList<Station> stations) {
        this.stations = stations;
    }

    public void setVertexes(ArrayList<Vertex> vertexes) {
        this.vertexes = vertexes;
    }

    public void setPassengers(ArrayList<Passenger> passengers) {
        this.passengers = passengers;

    }

//    public void setPodcars(ArrayList<Podcar> podcars) {
//        this.podcars = podcars;
//
//    }
    public void insertOBA(OnBoardApp obaTemp, String ip) {
        System.out.println("1: " + ip);
        boolean exists = false;

        String thisIp = obaTemp.getIpAdress();
        System.out.println("2: " + thisIp);
        for (int i = 0; i < onBoardApps.size(); i++) {
            String tmpIp = onBoardApps.get(i).getIpAdress();
            System.out.println("3: " + tmpIp);
            if (tmpIp.equals(thisIp)) {
                onBoardApps.get(i).setIpAdress(ip);
                exists = true;
            }
        }
        if (exists == false) {
            onBoardApps.add(obaTemp);
        }
    }

    public OnBoardApp findOnBoardApp(String ip) throws NoSuchOnBoardAppException {
        for (OnBoardApp app : onBoardApps) {
            if (app.getIpAdress().equals(ip)) {
                return app;
            }
        }
        throw new NoSuchOnBoardAppException("No such On Board App with ip '" + ip + "' was found");
    }

    public void addPodcar(Podcar podcar) {
        this.podcars.add(podcar);
    }

    Vertex getParkingLocation() {
        return this.parkingLocation;
    }

    public OnBoardApp findOnBoardApp(Podcar car) throws NoSuchOnBoardAppException {
        for (OnBoardApp app : onBoardApps) {
            if (app.getPodcar().equals(car)) {
                return app;
            }
        }
        throw new NoSuchOnBoardAppException("No such On Board App with this podcar was found");
    }
}
