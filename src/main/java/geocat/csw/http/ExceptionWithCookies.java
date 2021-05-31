package geocat.csw.http;

import java.io.IOException;

public class ExceptionWithCookies extends Exception {

    String cookie;

    public ExceptionWithCookies(String message, String cookie, Throwable cause){
        super(message,cause);
        this.cookie = cookie;
    }
}
