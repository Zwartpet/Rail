/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.handler.model;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import listeners.LocationListener;
import model.Podcar;

/**
 *
 * @author Bennet
 */
public class OnBoardApp implements LocationListener {

    private String podcarIp;
    private Podcar podcar;

    public OnBoardApp() {
    }

    public void setIpAdress(String inetAddress) {
        podcarIp = inetAddress;
    }

    public String getIpAdress() {

        return podcarIp;
    }

    public Podcar getPodcar() {
        return podcar;
    }

    public void setPodcar(Podcar podcar) {
        this.podcar = podcar;
        this.podcar.registerLocationListener(this);
    }

    @Override
    public void updateLocation(String location) {
        Socket socket = null;
        DataOutputStream dos = null;
        
        try {
            socket = new Socket(this.podcarIp, 6666);
            dos = new DataOutputStream(socket.getOutputStream());
            dos.writeBytes("L" + location + "\n");
            dos.flush();
        } catch (IOException exc) {
            exc.printStackTrace();
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                    socket.close();
                } catch (IOException exc) {
                    exc.printStackTrace();
                }
            }
        }
    }

    public void updateDestination(String destination) {
        Socket socket = null;
        DataOutputStream dos = null;
        try {
            socket = new Socket(podcarIp, 6666);
            dos = new DataOutputStream(socket.getOutputStream());
            dos.writeBytes("D" + destination + "\n");
            dos.flush();
        } catch (IOException exc) {
            exc.printStackTrace();
        } finally {
            try {
                dos.close();
                socket.close();
            } catch (IOException exc) {
                exc.printStackTrace();
            }
        }
    }
}
