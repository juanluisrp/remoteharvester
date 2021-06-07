package geocat.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ProblematicResultsConfiguration extends HashMap<String,String>{


    public static String KEY_LAST_RECORDSET_NEXTRECORD_NOT_ZERO = "LAST_RECORDSET_NEXTRECORD_NOT_ZERO";

    public static List<String> ALL_KEYS = Arrays.asList( new String[] {KEY_LAST_RECORDSET_NEXTRECORD_NOT_ZERO} );
    public static List<String> ALL_KEYS_ERROR_IGNORE = Arrays.asList( new String[] {KEY_LAST_RECORDSET_NEXTRECORD_NOT_ZERO} );


    public static HashMap<String,String> DEFAULT_VALUES = new HashMap<String,String>() {
        {
            put(KEY_LAST_RECORDSET_NEXTRECORD_NOT_ZERO,"IGNORE");
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
}
