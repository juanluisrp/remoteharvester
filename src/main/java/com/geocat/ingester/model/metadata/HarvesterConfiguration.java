package com.geocat.ingester.model.metadata;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class HarvesterConfiguration {
    private String uuid;
    private String name;
    private String url;
    private String userOwner;
    private String groupOwner;
    private String cswFilter;
    private Map<Integer, List<Integer>> privileges;
}
