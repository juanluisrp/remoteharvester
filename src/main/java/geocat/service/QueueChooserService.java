package geocat.service;

import geocat.routes.queuebased.MultiGetRecordQueues;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Scope("prototype")
public class QueueChooserService {

    static Map<String, Integer>  currentQueueOffsets = new HashMap<String,Integer>();

    Pattern parallelPattern = Pattern.compile("^PARALLEL(\\d)$");


    public String chooseQueue(String hint, int expectedNumberOfRecords) throws Exception {
        if ( (hint==null) || (hint.isEmpty()) || (!parallelPattern.matcher(hint).matches()) ) {
            //we choose
            if (expectedNumberOfRecords < 2000){
                return chooseQueueByGroup(MultiGetRecordQueues.allQueueGroupNames.get(0)); // always choose the first one
            }
            return chooseQueueByGroup(MultiGetRecordQueues.allQueueGroupNames.get(1)); // always choose the 2nd one
        }
        //user specified something - we should choose something
        Matcher m = parallelPattern.matcher(hint);
        m.find();
        int parallellism = Integer.parseInt(m.group(0));
        if (parallellism <=1)
            throw new Exception("requested parallelism of 0 or 1 - must be 2+!");
        if  (MultiGetRecordQueues.allQueueGroupNames.size() <= 1+parallellism)
            throw new Exception("requested parallelism too high!");
        return chooseQueueByGroup(MultiGetRecordQueues.allQueueGroupNames.get(parallellism));
    }


    public synchronized String chooseQueueByGroup(String groupName){
        if (!currentQueueOffsets.containsKey(groupName))
            currentQueueOffsets.put(groupName,0);
        String result = groupName+currentQueueOffsets.get(groupName);
        if (currentQueueOffsets.get(groupName) >=9)
            currentQueueOffsets.put(groupName,0);
        else
            currentQueueOffsets.put(groupName,currentQueueOffsets.get(groupName) +1 );

        return result;
    }
}
