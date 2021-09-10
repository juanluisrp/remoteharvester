package geocat.database.entities;

public enum HarvestJobState {
    CREATING,
    DETERMINING_WORK, WORK_DETERMINED, GETTING_RECORDS, RECORDS_RECEIVED,
    ERROR, USERABORT,

    COMPLETE
}
