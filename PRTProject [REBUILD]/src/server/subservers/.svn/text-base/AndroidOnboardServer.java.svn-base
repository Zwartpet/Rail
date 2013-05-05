/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.subservers;

import server.handler.AndroidMobileHandler;
import server.handler.AndroidOnboardHandler;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class AndroidOnboardServer implements Runnable {

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(4444);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                Thread connectionThread = new Thread(new AndroidOnboardHandler(clientSocket),"Handle Client Android Onboard App Thread");
                connectionThread.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(AndroidOnboardServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
