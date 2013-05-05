/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mapmaker;

import java.util.ArrayList;
import model.Station;
import model.Vertex;
import processing.core.PApplet;
import processing.core.PFont;

/**
 *
 * @author Razzy
 */
class MapMakerSketch extends PApplet {

    private final MapMakerController mapMakerController;
    private int gridSize;
    private PFont font;
    private int mode;
    private final int SELECT = 1;
    private final int VERTEX = 2;
    private final int STATION = 3;
    private final int DELETE = 4;

    public MapMakerSketch(MapMakerController mapMakerController) {
        this.mapMakerController = mapMakerController;
        mode = SELECT;

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

        //Update gridSize
        this.gridSize = mapMakerController.getGridSize();

        drawGrid();


        drawModes();

        drawVertexes();



    }

    private void drawGrid() {
        stroke(30);
        for (int i = 0; i < width / gridSize + 1; i++) {
            line(0, i * gridSize, width, i * gridSize);
        }

        for (int i = 0; i < height / gridSize + 1; i++) {
            line(i * gridSize, 0, i * gridSize, height);
        }

    }

    @Override
    public void mouseClicked() {


        switch (mode) {
            case SELECT:
                if (mouseButton == LEFT) {
                    mapMakerController.selectTile(mouseX / gridSize, mouseY / gridSize);
                } else if (mouseButton == RIGHT) {
                    mapMakerController.toggleAdjecent(mouseX / gridSize, mouseY / gridSize);
                }

                break;
            case VERTEX:
                mapMakerController.createVertex(mouseX / gridSize, mouseY / gridSize);
                break;
            case STATION:
                mapMakerController.createStation(mouseX / gridSize, mouseY / gridSize);
                break;
            case DELETE:
                mapMakerController.deleteTile(mouseX / gridSize, mouseY / gridSize);




        }
    }

    @Override
    public void keyPressed() {
        switch (key) {
            case '1':
                mode = SELECT;
                break;
            case '2':
                mode = VERTEX;
                break;
            case '3':
                mode = STATION;
                break;
            case '4':
                mode = DELETE;
                break;
            case 's':
                mapMakerController.saveMap();
                break;
            case 'l':
                mapMakerController.loadMap();
                break;
           
        }


    }

    private void drawModes() {
        textFont(font, 16);
        fill(255);
        switch (mode) {
            case SELECT:
                text("Select", 8, 16);
                break;
            case VERTEX:
                text("Vertex", 8, 16);
                break;
            case STATION:
                text("Station", 8, 16);
                break;
            case DELETE:
                fill(255, 0, 0);
                text("Delete", 8, 16);
                fill(255);
                break;
        }
    }

    private void drawVertexes() {
        ArrayList<Station> allStations = mapMakerController.getAllStations();
        ArrayList<Vertex> allVertexes = mapMakerController.getAllVertexes();


        Station selectedStation = mapMakerController.getSelectedStation();
        Vertex selectedVertex = mapMakerController.getSelectedVertex();


        stroke(153, 204, 0);
        noFill();
        if (selectedVertex != null) {
            rect(selectedVertex.getxPosition() * gridSize, selectedVertex.getyPosition() * gridSize, gridSize, gridSize);
            textFont(font, 12);
            text(selectedVertex.getLabel(), 10, 40);
            text("x:" + selectedVertex.getxPosition() + " y:" + selectedVertex.getyPosition(), 10, 58);
            String adjecentVertexesText = "";
            for (Vertex adjecentVertex : selectedVertex.getAdjacencyList()) {
                adjecentVertexesText += adjecentVertex.getLabel() + ",";
                fill(153, 204, 0);

                rect(adjecentVertex.getxPosition() * gridSize, adjecentVertex.getyPosition() * gridSize, gridSize, gridSize);

            }

            text("Can travel to", 10, 76);
            text(adjecentVertexesText, 10, 94);



        }
        stroke(255, 68, 68);
        fill(255, 68, 68);
        for (Vertex vertex : allVertexes) {
            ellipseMode(CENTER);
            ellipse(vertex.getxPosition() * gridSize + (gridSize / 2), vertex.getyPosition() * gridSize + (gridSize / 2), gridSize / 3, gridSize / 3);
            ArrayList<Vertex> adjacencyList = vertex.getAdjacencyList();
            for (Vertex adjecentVertex : adjacencyList) {

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


    }
}