package net.geocat.service.capabilities;

import net.geocat.xml.XmlCapabilitiesDocument;

import javax.xml.xpath.XPathExpressionException;
import java.util.List;

public interface ICapabilitiesDatasetLinkExtractor {

    List<DatasetLink>  findLinks(XmlCapabilitiesDocument doc) throws  Exception;
}
