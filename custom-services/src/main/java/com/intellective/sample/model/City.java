package com.intellective.sample.model;

import java.util.LinkedList;
import java.util.List;

public class City {

    private String name;
    private List<String> areaCodes = new LinkedList<>();

    public City(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<String> areaCodes() {
        return areaCodes;
    }

    @Override
    public String toString() {
        return "City{" +
                "name='" + name + '\'' +
                ", areaCodes='" + areaCodes.toString() + '\'' +
                '}';
    }

}
