/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package com.geocat.ingester.index;

import net.sf.saxon.stax.ReceiverToXMLStreamWriter;
import net.sf.saxon.trans.XPathException;

import javax.xml.stream.XMLStreamWriter;

public class ReceiverToXmlStreamWriteInDocument extends ReceiverToXMLStreamWriter {
  public ReceiverToXmlStreamWriteInDocument(XMLStreamWriter writer) {
    super(writer);
  }

  @Override
  public void startDocument(int properties) throws XPathException {
    // Document exists.
  }

  @Override
  public void endDocument() throws XPathException {
    // Document exists.
  }

  @Override
  public void close() throws XPathException {
    //
  }
}
