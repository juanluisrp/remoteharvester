package geocat.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ProblematicResultsConfiguration extends HashMap<String,String>{


    public static String KEY_LAST_RECORDSET_NEXTRECORD_NOT_ZERO = "LAST_RECORDSET_NEXTRECORD_NOT_ZERO";
    public static String KEY_NEXTRECORD_BAD_VALUE = "NEXTRECORD_BAD_VALUE";

    public static String KEY_RESPONSE_CONTAINS_FEWER_RECORDS_THAN_REQUESTED = "RESPONSE_CONTAINS_FEWER_RECORDS_THAN_REQUESTED";

    public static String KEY_TOTAL_RECORDS_CHANGED = "TOTAL_RECORDS_CHANGED";
    public static String KEY_MAX_PERCENT_TOTAL_RECORDS_ALLOWED = "MAX_PERCENT_TOTAL_RECORDS_ALLOWED";



    public static List<String> ALL_KEYS = Arrays.asList( new String[]  {
            KEY_LAST_RECORDSET_NEXTRECORD_NOT_ZERO,
            KEY_NEXTRECORD_BAD_VALUE,
            KEY_RESPONSE_CONTAINS_FEWER_RECORDS_THAN_REQUESTED,
            KEY_TOTAL_RECORDS_CHANGED,
            KEY_MAX_PERCENT_TOTAL_RECORDS_ALLOWED
    } );

    public static List<String> ALL_KEYS_ERROR_IGNORE = Arrays.asList( new String[] {
            KEY_LAST_RECORDSET_NEXTRECORD_NOT_ZERO,
            KEY_NEXTRECORD_BAD_VALUE,
            KEY_RESPONSE_CONTAINS_FEWER_RECORDS_THAN_REQUESTED,
            KEY_TOTAL_RECORDS_CHANGED
    } );

    public static List<String> ALL_KEYS_INTEGER = Arrays.asList( new String[] {
            KEY_MAX_PERCENT_TOTAL_RECORDS_ALLOWED
    } );

    public static HashMap<String,String> DEFAULT_VALUES = new HashMap<String,String>() {
        {
            put(KEY_LAST_RECORDSET_NEXTRECORD_NOT_ZERO,"IGNORE"); //some servers do this always...
            put(KEY_NEXTRECORD_BAD_VALUE,"ERROR");
            put(KEY_RESPONSE_CONTAINS_FEWER_RECORDS_THAN_REQUESTED,"ERROR");
            put(KEY_TOTAL_RECORDS_CHANGED,"ERROR");
            put(KEY_MAX_PERCENT_TOTAL_RECORDS_ALLOWED,"1");
        }
    };

    public ProblematicResultsConfiguration() {

    }



    public void validate() throws Exception {
       //only contains known keys
        for(String key : this.keySet()){
            if (!ALL_KEYS.contains(key))
                throw new Exception("ProblematicResultsConfiguration: has unknown key "+key);
        }

        //missing key - set to default
        for(String key : DEFAULT_VALUES.keySet()) {
            if (!containsKey(key))
                put(key,DEFAULT_VALUES.get(key));
        }

        //validate that values are "ERROR" or "IGNORE"
        for(String key : ALL_KEYS_ERROR_IGNORE) {
                if (  (!get(key).equals("IGNORE")) && (!get(key).equals("ERROR")) )
                    throw new Exception ("ProblematicResultsConfiguration: key "+key+" should be either 'ERROR' or 'IGNORE' but was "+get(key));
        }
        //validate that values are a valid integer
        for(String key : ALL_KEYS_INTEGER) {
            Integer.parseInt(get(key)); // will throw if a problem
        }
    }

    static  ObjectMapper mapper = new ObjectMapper();
    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString( this );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "{}";
    }

    public static ProblematicResultsConfiguration parse(String problematicResultsConfigurationJSON) throws  Exception {
        return mapper.readValue(problematicResultsConfigurationJSON, ProblematicResultsConfiguration.class);
    }



    public boolean errorIfLastRecordIsNotZero() {
        return get(ProblematicResultsConfiguration.KEY_LAST_RECORDSET_NEXTRECORD_NOT_ZERO).equals("ERROR");
    }

    public boolean errorIfNextRecordComputedWrong(){
        return get(ProblematicResultsConfiguration.KEY_NEXTRECORD_BAD_VALUE).equals("ERROR");

    }

    public boolean errorIfTooFewRecordsReturnedInResponse(){
        return get(ProblematicResultsConfiguration.KEY_RESPONSE_CONTAINS_FEWER_RECORDS_THAN_REQUESTED).equals("ERROR");
    }

    public boolean errorIfTotalRecordsChanges() {
        return get(ProblematicResultsConfiguration.KEY_TOTAL_RECORDS_CHANGED).equals("ERROR");
    }

    public int getMaxPercentChangeTotalRecords() {
        if (errorIfTotalRecordsChanges())
            return 0;
        return  Integer.parseInt(get(KEY_MAX_PERCENT_TOTAL_RECORDS_ALLOWED));
    }
}
