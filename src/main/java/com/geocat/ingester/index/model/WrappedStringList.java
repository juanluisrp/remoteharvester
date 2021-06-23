package com.geocat.ingester.index.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WrappedStringList {
    @JsonValue
    private List<Object> wrapped = new ArrayList<>();
}
