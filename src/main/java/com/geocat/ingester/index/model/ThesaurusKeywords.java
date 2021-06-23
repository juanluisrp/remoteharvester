package com.geocat.ingester.index.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ThesaurusKeywords {
    private String id;
    private String title;
    private String theme;
    private String link;

    @JsonProperty("keywords")
    List<Keyword> keywords = new ArrayList<>();
}
