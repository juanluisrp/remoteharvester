package net.geocat.database.linkchecker.entities.helper;

public enum ServiceMetadataDocumentState {
    CREATED, // unprocessed
    LINKS_FOUND,  // operatesOn and outgoing links found

    ERROR
}
