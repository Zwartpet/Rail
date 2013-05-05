/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.subservers;

import server.handler.AndroidMobileHandler;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class AndroidMobileServer implements Runnable {

    @Override
    public void run() {
        try {


            ServerSocket serverSocket = new ServerSocket(5555);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                Thread connectionThread = new Thread(new AndroidMobileHandler(clientSocket), "Handle Client Android Mobile Thread");
                connectionThread.start();







            }
        } catch (IOException ex) {
            Logger.getLogger(AndroidMobileServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
