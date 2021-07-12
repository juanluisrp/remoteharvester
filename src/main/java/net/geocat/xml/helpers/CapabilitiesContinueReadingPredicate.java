package net.geocat.xml.helpers;

import net.geocat.http.IContinueReadingPredicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CapabilitiesContinueReadingPredicate implements IContinueReadingPredicate {

    Pattern tagWithNS = Pattern.compile("^<([^ :<>]+):([^ >]+)[^>]+>",Pattern.MULTILINE);
    Pattern tagWithoutNS = Pattern.compile("^<([^ >]+)[^>]+>",Pattern.MULTILINE);



    CapabilityDeterminer capabilityDeterminer;
    public CapabilitiesContinueReadingPredicate(CapabilityDeterminer capabilityDeterminer)
    {
        this.capabilityDeterminer = capabilityDeterminer;
    }

    public boolean isXML(String doc){
        try {
            if (!doc.startsWith("<?xml"))
                return false; //not XML
            return true;
        }
        catch (Exception e){
            return false;
        }
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
        return matcher.group(1).trim();
    }

    public String getTagName(String doc){
        Matcher matcher = tagWithNS.matcher(doc);
        boolean find = matcher.find();
        if (find)
            return   matcher.group(2).trim();
        matcher = tagWithoutNS.matcher(doc);
         find = matcher.find();
        if (find)
            return   matcher.group(1).trim();
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
    public boolean continueReading(byte[] head)   {
        try {
            String doc = new String(head).trim();
            if (!isXML(doc))
                return false; //not XML

            doc = replaceXMLDecl(doc).trim();
            doc = getRootTag(doc).trim();

            String prefix = getPrefix(doc);
            String tag = getTagName(doc);
            String ns = getNS( prefix, doc);

            CapabilitiesType type = capabilityDeterminer.determineType(ns, tag);
            return true;
        }
        catch (Exception e){
            int t=0;
        }
        return false;
    }


}
