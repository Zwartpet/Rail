/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.views;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import model.Station;
import model.Vertex;
import processing.core.PApplet;

/**
 *
 * @author Administrator
 */
public class MapPreviewView extends JFrame {

    public MapPreviewView(ArrayList<Station> allStations, ArrayList allVertexes) {
        this.setSize(1024, 768);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        //set layout
        JPanel sketchPanel = new JPanel();
        sketchPanel.setBounds(0, 0, 1024, 768);
        PApplet mapPreviewSketch = new MapPreviewSketch(allStations, allVertexes);
        sketchPanel.add(mapPreviewSketch);
        this.add(mapPreviewSketch);
        mapPreviewSketch.init();
    }

   

  

   

}
