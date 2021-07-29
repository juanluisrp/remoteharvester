package net.geocat.service.capabilities;

import java.util.Objects;

public class DatasetLink {

    String identifier;
    String rawUrl;

    //---------------------------------------------------------------------------

    public DatasetLink(String identifier, String rawUrl) {
        this.identifier = identifier;
        this.rawUrl = rawUrl;
    }


    //---------------------------------------------------------------------------

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getRawUrl() {
        return rawUrl;
    }

    public void setRawUrl(String rawUrl) {
        this.rawUrl = rawUrl;
    }

    //---------------------------------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DatasetLink that = (DatasetLink) o;
        return Objects.equals(identifier, that.identifier) && Objects.equals(rawUrl, that.rawUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, rawUrl);
    }

    //---------------------------------------------------------------------------

    @Override
    public String toString() {
        return "DatasetLink {" +
                "identifier='" + identifier + '\'' +
                ", rawUrl='" + rawUrl + '\'' +
                '}';
    }
}
