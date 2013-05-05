/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.exception;

/**
 *
 * @author John
 */
public class UnknownStatusMessageException extends Exception{
    private String message;
    private char id;

    public UnknownStatusMessageException(char id) {
        this.id = id;
        this.message = "Error handling data: Unknown status message " + id;
    }
    
    @Override
    public String getMessage(){
        return message;
    }
}
