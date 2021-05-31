package geocat.csw.http;

public class RedirectException extends Exception {

    public RedirectException(String message , String newLocation) {
        super(message);
        this.newLocation = newLocation;
    }

    public String getNewLocation() {
        return newLocation;
    }

    public void setNewLocation(String newLocation) {
        this.newLocation = newLocation;
    }

    String newLocation;

}
