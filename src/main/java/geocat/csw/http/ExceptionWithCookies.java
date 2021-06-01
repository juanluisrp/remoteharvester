package geocat.csw.http;

public class ExceptionWithCookies extends Exception {

    String cookie;

    public ExceptionWithCookies(String message, String cookie, Throwable cause) {
        super(message, cause);
        this.cookie = cookie;
    }
}
