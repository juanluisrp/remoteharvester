package net.geocat.database.linkchecker.entities2;

public enum LinkCheckJobState {
    CREATING,

    FINDING_LINKS, LINKS_FOUND,

    CHECKING_LINKS, LINKS_CHECKED,

    COMPLETE,

    ERROR, USERABORT
}
