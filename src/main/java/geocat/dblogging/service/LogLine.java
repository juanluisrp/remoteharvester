package geocat.dblogging.service;

public class LogLine {

    public String processID;
    public String when;
    public String level;
    public String message;
    public boolean isException;
    public String threadName;

    public String[] stackTraces;
}
