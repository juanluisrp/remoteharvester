package net.geocat.database.linkchecker.entities;

public enum LinkCheckJobState {
    CREATING,

    FINDING_LINKS, LINKS_FOUND,

    CHECKING_LINKS, LINKS_CHECKED,

    ERROR, USERABORT
}
