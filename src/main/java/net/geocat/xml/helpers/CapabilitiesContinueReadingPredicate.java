package net.geocat.xml.helpers;

import net.geocat.http.IContinueReadingPredicate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CapabilitiesContinueReadingPredicate implements IContinueReadingPredicate {

    Pattern tagWithNS = Pattern.compile("^<([^ :<>]+):([^ >]+)[^>]+>",Pattern.MULTILINE);
    Pattern tagWithoutNS = Pattern.compile("^<([^ >]+)[^>]+>",Pattern.MULTILINE);



    CapabilityDeterminer capabilityDeterminer;
    public CapabilitiesContinueReadingPredicate(CapabilityDeterminer capabilityDeterminer)
    {
        this.capabilityDeterminer = capabilityDeterminer;
    }

    public boolean isXML(String doc){
        if (!doc.startsWith("<?xml"))
            return false; //not XML
        return true;
    }

    public String replaceXMLDecl(String doc){
        doc = doc.replaceFirst("<\\?xml[^\\?>]+\\?>","");
        return doc;
    }

    public String getRootTag(String doc){
        Matcher matcher = tagWithNS.matcher(doc);
        boolean find = matcher.find();
        if (find)
            return   matcher.group(0);
        matcher = tagWithoutNS.matcher(doc);
        find = matcher.find();
        if (find)
            return   matcher.group(0);
        return null;
    }

    public String getPrefix(String doc){
        Matcher matcher = tagWithNS.matcher(doc);
        boolean find = matcher.find();
        if (!find)
            return null;
        return matcher.group(1);
    }

    public String getTagName(String doc){
        Matcher matcher = tagWithNS.matcher(doc);
        boolean find = matcher.find();
        if (find)
            return   matcher.group(2);
        matcher = tagWithoutNS.matcher(doc);
         find = matcher.find();
        if (find)
            return   matcher.group(1);
        return null;
    }

    public String getNS(String prefix,String tag) {
        String pattern = "xmlns=\"([^\"]+)\"";
        if (prefix !=null)
            pattern = "xmlns:"+prefix+"=\"([^\"]+)\"";
        Pattern ns = Pattern.compile(pattern,Pattern.MULTILINE);
        Matcher matcher = ns.matcher(tag);
        boolean find = matcher.find();
        if (find)
            return   matcher.group(1);
        return null;
    }


    @Override
    public boolean continueReading(byte[] head) {
        String doc = new String(head).trim();
        if (!isXML(doc))
            return false; //not XML

        doc = replaceXMLDecl(doc);

        return false;
    }


}
