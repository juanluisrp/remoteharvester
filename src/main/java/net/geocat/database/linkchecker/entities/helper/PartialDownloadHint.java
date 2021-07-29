package net.geocat.database.linkchecker.entities.helper;

//null = always download
public enum PartialDownloadHint {
    ALWAYS_DOWNLOAD,
    XML_ONLY,
    CAPABILITIES_ONLY,
    METADATA_ONLY
}
