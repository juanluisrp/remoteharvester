/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package com.geocat.ingester.index.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.locationtech.jts.geom.Coordinate;

import java.io.IOException;
import java.util.List;

public class LocationSerializer extends JsonSerializer<List<Coordinate>> {

  @Override
  public void serialize(
      List<Coordinate> coordinate,
      JsonGenerator gen,
      SerializerProvider serializers)
      throws IOException {

    gen.writeStartArray();
    coordinate.forEach(c -> {
      try {
        gen.writeStartArray();
        gen.writeRawValue(c.getX() + "," + c.getY());
        gen.writeEndArray();
      } catch (IOException ioException) {
        ioException.printStackTrace();
      }
    });
    gen.writeEndArray();
  }
}
