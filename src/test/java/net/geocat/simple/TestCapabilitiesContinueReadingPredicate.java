package net.geocat.simple;

import net.geocat.service.downloadhelpers.CapabilitiesContinueReadingPredicate;
import net.geocat.xml.helpers.CapabilityDeterminer;
import org.junit.Test;

import static junit.framework.TestCase.*;

public class TestCapabilitiesContinueReadingPredicate {

    @Test
    public void test_ns(){
        CapabilitiesContinueReadingPredicate pred = new CapabilitiesContinueReadingPredicate(new CapabilityDeterminer());
        String s= "<?xml version=\"1.0\" encoding=\"UTF-8\"?><gmd:MD_Metadata xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:gco=\"http://www.isotc211.org/2005/gco\" xmlns:gmd=\"http://www.isotc211.org/2005/gmd\" xmlns:gmx=\"http://www.isotc211.org/2005/gmx\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xsi:schemaLocation=\"http://www.isotc211.org/2005/gmd http://schemas.opengis.net/csw/2.0.2/profiles/apiso/1.0.0/apiso.xsd\">\n" +
                "  <gmd:fileIdentifier>\n" +
                "    <gco:CharacterString>bf87f212-3ec2-4fcc-9163-1cbdf5fe334c</gco:CharacterString>\n" +
                "  </gmd:fileIdentifier>\n" +
                "  <gmd:lan";

        String s_nodecl = s.substring(38);
        String s_rootOnly = s.substring(38,409);

       assertTrue(pred.isXML(s));

       assertEquals(s_nodecl,pred.replaceXMLDecl(s));

       assertEquals(s_rootOnly,pred.getRootTag(s_nodecl));

       assertEquals("MD_Metadata",pred.getTagName(s_nodecl));
        assertEquals("gmd",pred.getPrefix(s_nodecl));

        assertEquals("http://www.isotc211.org/2005/gmd", pred.getNS("gmd",s_rootOnly));
    }

    @Test
    public void test_no_ns(){
        CapabilitiesContinueReadingPredicate pred = new CapabilitiesContinueReadingPredicate(new CapabilityDeterminer());
        String s= "<?xml version=\"1.0\" encoding=\"UTF-8\"?><MD_Metadata xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:gco=\"http://www.isotc211.org/2005/gco\" xmlns=\"http://www.isotc211.org/2005/gmd\" xmlns:gmx=\"http://www.isotc211.org/2005/gmx\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xsi:schemaLocation=\"http://www.isotc211.org/2005/gmd http://schemas.opengis.net/csw/2.0.2/profiles/apiso/1.0.0/apiso.xsd\">\n" +
                "  <fileIdentifier>\n" +
                "    <gco:CharacterString>bf87f212-3ec2-4fcc-9163-1cbdf5fe334c</gco:CharacterString>\n" +
                "  </fileIdentifier>\n" +
                "  <lan";

        String s_nodecl = s.substring(38);
        String s_rootOnly = s.substring(38,401);

        assertTrue(pred.isXML(s));

        assertEquals(s_nodecl,pred.replaceXMLDecl(s));

        assertEquals(s_rootOnly,pred.getRootTag(s_nodecl));

        assertEquals("MD_Metadata",pred.getTagName(s_nodecl));
        assertEquals(null,pred.getPrefix(s_nodecl));

        assertEquals("http://www.isotc211.org/2005/gmd", pred.getNS(null,s_rootOnly));

    }
}
