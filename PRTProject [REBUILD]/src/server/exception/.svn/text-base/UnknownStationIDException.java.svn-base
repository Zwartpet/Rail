/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.exception;

/**
 *
 * @author John
 */
public class UnknownStationIDException extends Exception{
    private String message;
    private char id;
    
    public UnknownStationIDException(char id) {
        this.id = id;
        this.message = "Error handling data: Unknown station ID " + id;
    }
    
    @Override
    public String getMessage(){
        return message;
    }
    
}
