/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.subservers;


import java.util.ArrayList;
import model.PRTData;
import model.Podcar;

/**
 *
 * @author Casper
 */
public interface PodcarServer {

    public abstract void connectToPodcars(ArrayList<Podcar> podcars);

    public abstract void sendData(Podcar podcar, String data);
}
