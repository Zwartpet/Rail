/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class Station extends Vertex implements Serializable {

    private int id;
    private String name;
    private ArrayList<Station> connectedTo;

    public Station(int id, String name, String label, int xPosition, int yPosition) {
        super(label, xPosition, yPosition);
        this.name = name;
        this.id = id;
        this.connectedTo = new ArrayList<Station>();
    }

    public void addConnectedStation(Station station) {
        this.connectedTo.add(station);
    }

    public boolean isConnected(Station station) {
        boolean connected = false;
        for (Station checkedStation : connectedTo) {
            if (station == checkedStation) {
                connected = true;
            }
        }
        return connected;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
