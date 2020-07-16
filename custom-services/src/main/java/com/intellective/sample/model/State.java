package com.intellective.sample.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class State {

    private final String stateCode;
    private final String displayValue;
    private final Map<String,City> cities;

    public State(String stateCode, String displayValue) {
        this.stateCode = stateCode;
        this.displayValue = displayValue;
        this.cities = new HashMap<>();
    }

    public String getStateCode() {
        return stateCode;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public Map<String, City> cities() {
        return cities;
    }

    @Override
    public String toString() {
        return "State{" +
                "stateCode='" + stateCode + '\'' +
                ", displayValue='" + displayValue + '\'' +
                '}';
    }

}
