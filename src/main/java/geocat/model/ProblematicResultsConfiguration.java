package geocat.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ProblematicResultsConfiguration extends HashMap<String, String> {

    /**
     * According to the CSW spec, when you request the last set of records (GetRecords), the response should have nextRecord=0.
     * Some servers return a number (i.e. if you request 100-111, it will return 112, even through there is no record 112).
     * ERROR - throw error if this occurs
     * IGNORE - do not throw
     */
    public static String KEY_LAST_RECORDSET_NEXTRECORD_NOT_ZERO = "LAST_RECORDSET_NEXTRECORD_NOT_ZERO";

    /**
     * Say you request records 100-111.  The response should have nextRecord=112.
     * If the server doesn't return 112, then this indicates a problem.
     * ERROR - throw error if this occurs
     * IGNORE - do not throw
     */
    public static String KEY_NEXTRECORD_BAD_VALUE = "NEXTRECORD_BAD_VALUE";

    /**
     * Say you request records 100-111 (i.e. startRecord=100, numberOfRecords=10).  The response should contain 10 records.
     * If the response contains fewer records than requested, this indicates an issue.
     * ERROR - throw error if this occurs
     * IGNORE - do not throw
     */
    public static String KEY_RESPONSE_CONTAINS_FEWER_RECORDS_THAN_REQUESTED = "RESPONSE_CONTAINS_FEWER_RECORDS_THAN_REQUESTED";

    /**
     * During a harvest, the server may add/delete records.  In this case, the GetRecord responses will have a different
     * totalNumberOfRecords than earlier (i.e. during DetermineWork).
     * <p>
     * ERROR - throw error if this occurs.  You will likely be missing records or have duplicate records.
     * IGNORE - (not recommended).  If the % amount of the number of records changed is greater than KEY_MAX_PERCENT_TOTAL_RECORDS_ALLOWED
     * then throw, otherwise do not.
     * For example, if total number of records=100 at the start of harvesting.  Later it becomes 105.  That's a 5% change.
     * If KEY_MAX_PERCENT_TOTAL_RECORDS_ALLOWED >=5 then its "ok" otherwise ERROR.
     */
    public static String KEY_TOTAL_RECORDS_CHANGED = "TOTAL_RECORDS_CHANGED";
    public static String KEY_MAX_PERCENT_TOTAL_RECORDS_ALLOWED = "MAX_PERCENT_TOTAL_RECORDS_ALLOWED";

    /**
     * After harvesting, look at the UUID for the records.  If there are duplicates, then something major has occurred.  The ingest will likely have issues with this.
     * ERROR - (recommended) throw error if this occurs
     * IGNORE - do not throw
     */
    public static String KEY_DUPLICATE_UUIDS = "DUPLICATE_UUIDS";


    public static List<String> ALL_KEYS = Arrays.asList(new String[]{
            KEY_LAST_RECORDSET_NEXTRECORD_NOT_ZERO,
            KEY_NEXTRECORD_BAD_VALUE,
            KEY_RESPONSE_CONTAINS_FEWER_RECORDS_THAN_REQUESTED,
            KEY_TOTAL_RECORDS_CHANGED,
            KEY_MAX_PERCENT_TOTAL_RECORDS_ALLOWED,
            KEY_DUPLICATE_UUIDS
    });

    public static List<String> ALL_KEYS_ERROR_IGNORE = Arrays.asList(new String[]{
            KEY_LAST_RECORDSET_NEXTRECORD_NOT_ZERO,
            KEY_NEXTRECORD_BAD_VALUE,
            KEY_RESPONSE_CONTAINS_FEWER_RECORDS_THAN_REQUESTED,
            KEY_TOTAL_RECORDS_CHANGED,

    });

    public static List<String> ALL_KEYS_INTEGER = Arrays.asList(new String[]{
            KEY_MAX_PERCENT_TOTAL_RECORDS_ALLOWED
    });

    public static HashMap<String, String> DEFAULT_VALUES = new HashMap<String, String>() {
        {
            put(KEY_LAST_RECORDSET_NEXTRECORD_NOT_ZERO, "IGNORE"); //some servers do this always...
            put(KEY_NEXTRECORD_BAD_VALUE, "ERROR");
            put(KEY_RESPONSE_CONTAINS_FEWER_RECORDS_THAN_REQUESTED, "ERROR");
            put(KEY_TOTAL_RECORDS_CHANGED, "ERROR");
            put(KEY_DUPLICATE_UUIDS, "ERROR");

            put(KEY_MAX_PERCENT_TOTAL_RECORDS_ALLOWED, "1");
        }
    };
    static ObjectMapper mapper = new ObjectMapper();


    public ProblematicResultsConfiguration() {

    }

    public static ProblematicResultsConfiguration parse(String problematicResultsConfigurationJSON) throws Exception {
        return mapper.readValue(problematicResultsConfigurationJSON, ProblematicResultsConfiguration.class);
    }

    public void validate() throws Exception {
        //only contains known keys
        for (String key : this.keySet()) {
            if (!ALL_KEYS.contains(key))
                throw new Exception("ProblematicResultsConfiguration: has unknown key " + key);
        }

        //missing key - set to default
        for (String key : DEFAULT_VALUES.keySet()) {
            if (!containsKey(key))
                put(key, DEFAULT_VALUES.get(key));
        }

        //validate that values are "ERROR" or "IGNORE"
        for (String key : ALL_KEYS_ERROR_IGNORE) {
            if ((!get(key).equals("IGNORE")) && (!get(key).equals("ERROR")))
                throw new Exception("ProblematicResultsConfiguration: key " + key + " should be either 'ERROR' or 'IGNORE' but was " + get(key));
        }
        //validate that values are a valid integer
        for (String key : ALL_KEYS_INTEGER) {
            Integer.parseInt(get(key)); // will throw if a problem
        }
    }

    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "{}";
    }

    public boolean errorIfDuplicateUUIDs() {
        return get(ProblematicResultsConfiguration.KEY_DUPLICATE_UUIDS).equals("ERROR");
    }


    public boolean errorIfLastRecordIsNotZero() {
        return get(ProblematicResultsConfiguration.KEY_LAST_RECORDSET_NEXTRECORD_NOT_ZERO).equals("ERROR");
    }

    public boolean errorIfNextRecordComputedWrong() {
        return get(ProblematicResultsConfiguration.KEY_NEXTRECORD_BAD_VALUE).equals("ERROR");

    }

    public boolean errorIfTooFewRecordsReturnedInResponse() {
        return get(ProblematicResultsConfiguration.KEY_RESPONSE_CONTAINS_FEWER_RECORDS_THAN_REQUESTED).equals("ERROR");
    }

    public boolean errorIfTotalRecordsChanges() {
        return get(ProblematicResultsConfiguration.KEY_TOTAL_RECORDS_CHANGED).equals("ERROR");
    }

    public int getMaxPercentChangeTotalRecords() {
        if (errorIfTotalRecordsChanges())
            return 0;
        return Integer.parseInt(get(KEY_MAX_PERCENT_TOTAL_RECORDS_ALLOWED));
    }
}
