/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.subservers;

import server.handler.AndroidStationHandler;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class AndroidStationServer implements Runnable {

    

    @Override
    public void run() {
               try {


            ServerSocket serverSocket = new ServerSocket(3333);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                Thread connectionThread = new Thread(new AndroidStationHandler(clientSocket),"Handle Client Android Station Thread");
                connectionThread.start();







            }
        } catch (IOException ex) {
            Logger.getLogger(AndroidMobileServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
