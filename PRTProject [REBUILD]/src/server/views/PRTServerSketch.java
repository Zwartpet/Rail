/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.views;

import java.awt.Point;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.PRTData;
import model.Path;
import model.Podcar;
import model.Station;
import model.Vertex;
import processing.core.PApplet;
import processing.core.PFont;

/**
 *
 * @author Administrator
 */
public class PRTServerSketch extends PApplet {

    private int gridSize = 30; //must be an even number
    private PFont font;

    public PRTServerSketch() {
    }

    @Override
    public void setup() {
        size(2000, 2000);
        background(0);
        font = createFont("Arial", 16, true);
    }

    @Override
    public void draw() {
        background(0);
        drawVertexes();
    }

    private void drawVertexes() {
        ArrayList<Station> allStations = PRTData.getPRTData().getStations();
        ArrayList<Vertex> allVertexes = PRTData.getPRTData().getVertexes();

        stroke(255, 68, 68);
        fill(255, 68, 68);
        for (Vertex vertex : allVertexes) {
            ellipseMode(CENTER);
            ellipse(vertex.getxPosition() * gridSize + (gridSize / 2), vertex.getyPosition() * gridSize + (gridSize / 2), gridSize / 3, gridSize / 3);
            ArrayList<Vertex> adjacencyList = vertex.getAdjacencyList();


            for (Vertex adjecentVertex : adjacencyList) {
                stroke(255, 68, 68);
                line(vertex.getxPosition() * gridSize + (gridSize / 2), vertex.getyPosition() * gridSize + (gridSize / 2), adjecentVertex.getxPosition() * gridSize + (gridSize / 2), adjecentVertex.getyPosition() * gridSize + (gridSize / 2));


            }

        }
        noStroke();
        fill(204, 0, 0);
        for (Station station : allStations) {
            rect(station.getxPosition() * gridSize, station.getyPosition() * gridSize, gridSize, gridSize);
            textFont(font, 12);
            text(station.getName(), station.getxPosition() * gridSize + gridSize + 4, station.getyPosition() * gridSize + gridSize / 4);
        }

        fill(51, 181, 229);
        
        
        ArrayList<Podcar> podcars = PRTData.getPRTData().getPodcars();
        for (Podcar podcar : PRTData.getPRTData().getPodcars()) {
            Vertex currentLocation = podcar.getCurrentLocation();
            ellipseMode(CENTER);
            ellipse(currentLocation.getxPosition() * gridSize + (gridSize / 2), currentLocation.getyPosition() * gridSize + (gridSize / 2), gridSize / 3, gridSize / 3);

            Path path = podcar.getPath();

            Vertex lastVertex = podcar.getCurrentLocation();
            if (path != null) {
                ArrayList<Vertex> path1 = null;
                try {
                    path1 = path.getPath();
                } catch (ConcurrentModificationException exc) {
                    System.out.println("Waiting for drawing. Cause: ConcurrentModificationException");
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(PRTServerSketch.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return;
                }
                for (Vertex pathVertex : path1) {


                    stroke(0, 153, 204);
                    line(lastVertex.getxPosition() * gridSize + (gridSize / 2), lastVertex.getyPosition() * gridSize + (gridSize / 2), pathVertex.getxPosition() * gridSize + (gridSize / 2), pathVertex.getyPosition() * gridSize + (gridSize / 2));
                    lastVertex = pathVertex;

                }
            }
        }
    }
}
