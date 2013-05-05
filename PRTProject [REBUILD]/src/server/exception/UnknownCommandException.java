/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.exception;

/**
 *
 * @author John
 */
public class UnknownCommandException extends Exception{
    private String message;
    private char id;
   
    public UnknownCommandException(char id) {
        this.id = id;
        this.message = "Error handling data: Unknown command " + id;
    }
    
    @Override
    public String getMessage(){
        return message;
    }
    
}
