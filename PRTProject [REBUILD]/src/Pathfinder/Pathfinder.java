/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Pathfinder;

import java.util.ArrayList;
import model.PRTData;
import model.Path;
import model.Podcar;
import model.SortedArrayList;
import model.Vertex;

/**
 * @author Tony Abidi
 */
public class Pathfinder {

    private SortedArrayList<Path> priorityList;
    private static Pathfinder pf;
    private boolean addedFirst;

    private Pathfinder() {
        priorityList = new SortedArrayList<>();
        addedFirst = false;
    }
    public static Pathfinder getInstance() {
        if (pf == null) {
            pf = new Pathfinder();
        }
        return pf;
    }

    /**
     * De Astar functie zoekt de beste route van een Vertex naar een Vertex. De
     * beste route wordt bepaald door steeds bij de volgende vertex de adjenct
     * vertices op de te vragen. Dit herhaald zich totdat de eind
     * Vertex(bestemming) geëvalueerd moet worden.
     *
     * @param start
     * @param end
     * @param p
     * @return a Path object containing the best path.
     */
    public Path Astar(Vertex start, Vertex end, Path p) {

        if (!addedFirst) {
            p.getPath().add(start);
            addedFirst = true;
        }

        // Stop condition
        if (start.getxPosition() == end.getxPosition() && start.getyPosition() == end.getyPosition()) {

            priorityList = new SortedArrayList<>();
            addedFirst = false;

            return p;
        }


        Vertex neighbor;
        double dx, dy, distance, estimated;
        for (int i = 0; i < start.adjacencyList.size(); i++) {
            neighbor = start.adjacencyList.get(i);//we gaan alle buren langs om de priotritylist te updaten
            dx = start.getxPosition() - neighbor.getxPosition();//hoeveel je naar rechts/link moet 
            dy = start.getyPosition() - neighbor.getyPosition();//hoeveel je naar boven/beneden  moet 
            distance = p.distance + Math.sqrt((dx * dx) + (dy * dy));// afstand van start tot de buur
            dx = end.getxPosition() - neighbor.getxPosition();//hoeveel je naar rechts/link moet tot het eind
            dy = end.getyPosition() - neighbor.getyPosition();//hoeveel je naar boven/beneden  moet tot het eind 
            estimated = distance + Math.sqrt((dx * dx) + (dy * dy));// afstand van start tot  eind
            Path path = new Path(p);
            path.estimated = estimated;
            path.distance = distance;
            path.getPath().add(neighbor);


            priorityList.insertSorted(path);
        }

        Path bestPath = priorityList.get(0);
        Vertex vertexNext = priorityList.get(0).getPath().get(bestPath.getPath().size() - 1);//laatste positie waar je nu zit 
        priorityList.remove(0);
        return Astar(vertexNext, end, bestPath);
    }
}
