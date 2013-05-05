package model;

import java.util.ArrayList;

/**
 *
 * @author Tony
 */
public class Path implements Comparable {

    public double distance;//tot nu toe
    public double estimated;
    private ArrayList<Vertex> path;

    public Path() {
        path = new ArrayList<Vertex>();
        distance = 0;
        estimated = 0;
    }

    public Path(Path p) {
        distance = p.distance;
        estimated = p.estimated;
        path = new ArrayList<Vertex>();
        for (int i = 0; i < p.path.size(); i++) {
            path.add(p.path.get(i));//kopie maken van path 
        }
    }

    public boolean compareTo(Path p) {
        return p.estimated < this.estimated;

    }

    public boolean equales(Path p) {
        return p.estimated == this.estimated;
    }

    @Override
    public int compareTo(Object t) {
        Path p = (Path) t;
        if (p.estimated < this.estimated) {
            return 1;
        } else {
            return -1;
        }
    }

    public ArrayList<Vertex> getPath() {
        return path;
    }

    public void setPath(ArrayList<Vertex> path) {
        this.path = path;
    }

    public Vertex removeFirstVertex() {
        return this.path.remove(0);
    }

    public Vertex getLast() {
        return path.get(path.size() - 1);
    }

    Vertex peekFirstVertex() {
        return path.get(0);
    }
}
