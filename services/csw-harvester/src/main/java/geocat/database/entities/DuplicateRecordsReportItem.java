package geocat.database.entities;

public interface DuplicateRecordsReportItem {

    int getCount();

    String getRecordIdentifier();

    String getCswRecordNumbers();

}
