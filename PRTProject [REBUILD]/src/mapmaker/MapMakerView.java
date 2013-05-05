/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mapmaker;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;

/**
 *
 * @author Razzy
 */
public class MapMakerView extends JFrame {

    private ShowDataPanel showDataPanel;

    MapMakerView(MapMakerController mapMakerController) {




        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setExtendedState(Frame.MAXIMIZED_BOTH);
        this.setMinimumSize(new Dimension(300, 200));
        this.setLayout(new BorderLayout());
        JPanel mapMakerPanel = new JPanel();
        MapMakerSketch mapMakerSketch = new MapMakerSketch(mapMakerController);
        mapMakerSketch.init();
        mapMakerPanel.add(mapMakerSketch);
 
        showDataPanel = new ShowDataPanel(mapMakerController);




        this.add(showDataPanel, BorderLayout.EAST);
        this.add(mapMakerPanel, BorderLayout.WEST);


    }

    public ShowDataPanel getShowDataPanel() {
        return showDataPanel;
    }
}
