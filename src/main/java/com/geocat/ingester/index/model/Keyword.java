package com.geocat.ingester.index.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Keyword {

    HashMap<String, String> value = new HashMap<>();

    @JsonAnyGetter
    public Map<String, String> getValue(){
        return value;
    }

    @JsonAnySetter
    public void add(String property, String value){
        this.value.put(property, value);
    }
}
