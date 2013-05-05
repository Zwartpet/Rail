/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bennet
 */
public class TestClient {

    public static void main(String[] args) {
        try {

            Socket connection = new Socket("localhost", 3333);
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
          
                    
            
            int bennet = 1337;
            out.writeInt(bennet);
            out.writeInt(000);
            
            
            out.flush();



        } catch (UnknownHostException ex) {
            Logger.getLogger(TestClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
