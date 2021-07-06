package net.geocat.http;

public class RedirectException extends Exception {

    String newLocation;

    public RedirectException(String message, String newLocation) {
        super(message);
        this.newLocation = newLocation;
    }

    public String getNewLocation() {
        return newLocation;
    }

    public void setNewLocation(String newLocation) {
        this.newLocation = newLocation;
    }

}
