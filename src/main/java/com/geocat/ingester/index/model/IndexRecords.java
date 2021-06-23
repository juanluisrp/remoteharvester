/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package com.geocat.ingester.index.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@XmlRootElement(name = "indexRecords")
@XmlAccessorType(XmlAccessType.FIELD)
public class IndexRecords {

  @XmlElement(name = "indexRecord")
  private List<IndexRecord> indexRecord;
}
