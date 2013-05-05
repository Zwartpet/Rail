/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Tony
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Tony OG
 */
public class Vertex implements Serializable {

    public String label;
    private int xPosition;
    private int yPosition;
    public ArrayList<Vertex> adjacencyList;

    public Vertex(String label, int xPosition, int yPosition) {

        this.label = label;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        adjacencyList = new ArrayList<Vertex>();
    }

    public void addAdjacentVertex(Vertex vertex) {
        this.adjacencyList.add(vertex);

    }

    public ArrayList<Vertex> getAdjacencyList() {
        return adjacencyList;
    }

    public int getxPosition() {
        return xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public boolean checkPosition(int x, int y) {
        if (this.xPosition == x && this.yPosition == y) {
            return true;
        }

        return false;

    }

    public String getLabel() {
        return label;
    }
}
