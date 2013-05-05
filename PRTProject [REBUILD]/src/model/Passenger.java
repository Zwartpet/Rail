/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Bennet
 */
public class Passenger {
    private String id;
    private boolean hasMobilePhone;

    public Passenger(String id, boolean hasMobilePhone) {
        this.id = id;
        this.hasMobilePhone = hasMobilePhone;
    }

    public String getId() {
        return id;
    }

    public boolean hasMobilePhone() {
        return hasMobilePhone;
    }
    
    
}
