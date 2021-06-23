package com.geocat.ingester.index.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WrappedCodelistList {
    @JsonValue
    private List<Codelist> wrapped = new ArrayList<>();
}
