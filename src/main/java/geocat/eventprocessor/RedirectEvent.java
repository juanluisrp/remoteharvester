package geocat.eventprocessor;

public class RedirectEvent {

    private Class eventType;
    private String endpoint;

    public RedirectEvent(Class eventType, String endpoint) {
        this.eventType = eventType;
        this.endpoint = endpoint;
    }

    public Class getEventType() {
        return eventType;
    }

    public void setEventType(Class eventType) {
        this.eventType = eventType;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
}
