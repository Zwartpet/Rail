/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.views;

import java.awt.Point;
import java.util.ArrayList;
import model.Station;
import model.Vertex;
import processing.core.PApplet;

/**
 *
 * @author Administrator
 */
public class MapPreviewSketch extends PApplet {

    private int gridSize = 16; //must be an even number
    private final ArrayList<Station> stations;
    private final ArrayList<Vertex> vertexes;

    MapPreviewSketch(ArrayList<Station> allStations, ArrayList<Vertex> allVertexes) {
        this.stations = allStations;
        this.vertexes = allVertexes;
    }

    @Override
    public void setup() {
        size(1024, 768);
        background(0);
    }

    @Override
    public void draw() {
        background(0);
        stroke(255);
        for (int i = 0; i < 1000; i = i + gridSize) {
            for (int j = 0; j < 1000; j = j + gridSize) {
                point(i, j);
            }
        }

        for (Station station : stations) {
            stroke(127, 0, 0);
            fill(127, 0, 0);
            rect(station.getxPosition() * gridSize, station.getyPosition() * gridSize, gridSize, gridSize);
        }

        for (Vertex vertex : vertexes) {
            ArrayList<Vertex> adjacencyList = vertex.getAdjacencyList();

            Point middlePoint = new Point((gridSize * vertex.getxPosition()) + Math.round(gridSize / 2), (gridSize * vertex.getyPosition()) + Math.round(gridSize / 2));

            ArrayList<Point> connectionPoints = new ArrayList<Point>();
            for (Vertex adjecentVertex : adjacencyList) {
                int xDiff = adjecentVertex.getxPosition() - vertex.getxPosition();
                int yDiff = adjecentVertex.getyPosition() - vertex.getyPosition();

                Point point = new Point();
                point.x = middlePoint.x + (Math.round(gridSize / 2) * xDiff);
                point.y = middlePoint.y + (Math.round(gridSize / 2) * yDiff);

                connectionPoints.add(point);
            }

            for (int i = 0; i < connectionPoints.size(); i++) {

                if (connectionPoints.size() > 1 && i + 1 < connectionPoints.size()) {
                    stroke(127, 0, 0);
                    fill(0, 127, 0);
                    line(connectionPoints.get(i).x, connectionPoints.get(i).y, connectionPoints.get(i + 1).x, connectionPoints.get(i + 1).y);
                }

            }

        }

    }

    @Override
    public void mousePressed() {

        int x = mouseX / this.gridSize;
        int y = mouseY / this.gridSize;
        Station s = new Station(stations.size(), "lol", "lol", x, y);
        stations.add(s);


    }
}
