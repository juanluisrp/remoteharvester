package com.geocat.ingester.model.metadata;

import lombok.Data;

@Data
public class HarvesterConfiguration {
    private String uuid;
    private String name;
    private String url;
    private String userOwner;
    private String groupOwner;
    private String cswFilter;

}
