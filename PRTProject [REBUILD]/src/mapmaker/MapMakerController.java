/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mapmaker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import model.PRTData;
import model.Podcar;
import model.Station;
import model.Vertex;
import server.main.PRTServer;
import server.main.PRTServerController;

/**
 *
 * @author Razzy
 */
class MapMakerController {

    private ArrayList<Vertex> allVertexes;
    private ArrayList<Station> allStations;
    private ArrayList<Podcar> allPodcars;
    private int gridSize;
    MapMakerView mapMakerView;
    Vertex selectedVertex;
    Station selectedStation;

    public MapMakerController() {


        allVertexes = new ArrayList<>();
        allStations = new ArrayList<>();
        allPodcars = new ArrayList<>();
        gridSize = 16;


        mapMakerView = new MapMakerView(this);
        mapMakerView.setVisible(true);

        Podcar podcarEen = new Podcar("test", new Vertex("test", 0, 0));
        allPodcars.add(podcarEen);

    }

    public int getGridSize() {
        return gridSize;
    }

    public void loadMap() {

        URL mainURL = PRTServer.class.getResource("PRTServer.class");

        JFileChooser fileChooser = new JFileChooser(mainURL.getPath() + "../../../../../../maps");
        int showOpenDialog = fileChooser.showOpenDialog(this.mapMakerView);
        if (showOpenDialog == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                ArrayList<Station> stations = (ArrayList<Station>) objectInputStream.readObject();
                ArrayList<Vertex> vertexes = (ArrayList<Vertex>) objectInputStream.readObject();
                objectInputStream.close();
                fileInputStream.close();

                allStations = stations;
                allVertexes = vertexes;

            } catch (FileNotFoundException ex) {
                Logger.getLogger(PRTServerController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(PRTServerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    void createVertex(int x, int y) {
        for (Vertex vertex : allVertexes) {
            if (vertex.checkPosition(x, y)) {
                return;
            }
        }

        Vertex newVertex = new Vertex("N" + x + "_" + y, x, y);
        allVertexes.add(newVertex);

        for (Vertex checkVertex : allVertexes) {
            int diffX = Math.abs(checkVertex.getxPosition() - newVertex.getxPosition());
            int diffY = Math.abs(checkVertex.getyPosition() - newVertex.getyPosition());

            if ((diffX == 1) != (diffY == 1)) {
                if ((diffX == 0) != (diffY == 0)) {
                    checkVertex.addAdjacentVertex(newVertex);
                }
            }
        }
    }

    void createStation(int x, int y) {
        for (Station station : allStations) {
            if (station.checkPosition(x, y)) {
                return;
            }
        }

        String nameStation = JOptionPane.showInputDialog("Name station");

        if (nameStation != null) {
            if (!nameStation.equals("")) {
                Station station = new Station(allStations.size(), nameStation, "N" + x + "_" + y, x, y);
                allStations.add(station);
                this.createVertex(station.getxPosition(), station.getyPosition());
            }
        }
    }

    public ArrayList<Vertex> getAllVertexes() {
        return allVertexes;
    }

    public ArrayList<Station> getAllStations() {
        return allStations;
    }

    public synchronized boolean deleteTile(int x, int y) {
        Station tempStation = null;
        for (Station station : allStations) {
            if (station.checkPosition(x, y)) {
                tempStation = station;
                break;
            }
        }
        allStations.remove(tempStation);


        Vertex tempVertex = null;
        for (Vertex vertex : allVertexes) {
            if (vertex.checkPosition(x, y)) {
                tempVertex = vertex;
                break;
            }
        }
        allVertexes.remove(tempVertex);

        return true;

    }

    void saveMap() {
        URL mainURL = PRTServer.class.getResource("PRTServer.class");
        JFileChooser fileChooser = new JFileChooser(mainURL.getPath() + "../../../../../../maps");
        fileChooser.showSaveDialog(mapMakerView);
        File selectedFile = new File(fileChooser.getSelectedFile() + ".prt");
        if (selectedFile == null) {
            return;
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(selectedFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(allStations);
            objectOutputStream.writeObject(allVertexes);
            objectOutputStream.close();
            fileOutputStream.close();


        } catch (FileNotFoundException ex) {
            Logger.getLogger(MapMakerController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MapMakerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void selectTile(int x, int y) {
        selectedVertex = null;
        selectedStation = null;
        for (Vertex vertex : allVertexes) {
            if (vertex.checkPosition(x, y)) {
                selectedVertex = vertex;
                break;
            }

        }


        for (Station station : allStations) {
            if (station.checkPosition(x, y)) {
                selectedStation = station;
                break;
            }
        }


        if(selectedVertex != null){
            this.mapMakerView.getShowDataPanel().getNewPodcarButton().setEnabled(true);
        } else {
              this.mapMakerView.getShowDataPanel().getNewPodcarButton().setEnabled(false);
        }

    }

    public Vertex getSelectedVertex() {
        return selectedVertex;
    }

    public Station getSelectedStation() {
        return selectedStation;
    }

    synchronized void toggleAdjecent(int x, int y) {


        Vertex tempAdjecentVertex = null;
        for (Vertex adjecentVertex : selectedVertex.getAdjacencyList()) {
            if (adjecentVertex.getxPosition() == x && adjecentVertex.getyPosition() == y) {
                tempAdjecentVertex = adjecentVertex;
                break;
            }
        }

        if (tempAdjecentVertex != null) {
            selectedVertex.getAdjacencyList().remove(tempAdjecentVertex);
            return;
        }


        Vertex tempNewAdjecentVertex = null;
        for (Vertex vertex : allVertexes) {
            if (vertex.checkPosition(x, y)) {
                tempNewAdjecentVertex = vertex;
            }
        }
        selectedVertex.getAdjacencyList().add(tempNewAdjecentVertex);


    }

    public ArrayList<Podcar> getAllPodcars() {
        return allPodcars;
    }

    void newPodcarView() {
        Podcar podcar = new Podcar(allPodcars.size() + "", selectedVertex);
        allPodcars.add(podcar);
        this.mapMakerView.getShowDataPanel().getPodcarTableModel().fireTableDataChanged();
    }
}
