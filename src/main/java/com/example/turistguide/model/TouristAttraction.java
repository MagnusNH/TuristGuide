package com.example.turistguide.model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class TouristAttraction {
    private int id;
    private String name;
    private String description;
    private String city;
    private List<String> tags;

    public TouristAttraction(){
        this.tags = new ArrayList<>();
    }

    public TouristAttraction (int id, String name, String description, String city, List<String> tags) {
        this.id=id;
        this.name = name;
        this.description = description;
        this.city = city;
        this.tags = (tags != null) ? new ArrayList<>(tags) : new ArrayList<>();
    }

    public TouristAttraction (String name, String description, String city, List<String> tags) {
        this.name = name;
        this.description = description;
        this.city = city;
        this.tags = (tags != null) ? new ArrayList<>(tags) : new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        if (tags == null) {
            this.tags = new ArrayList<>();
        } else {
            this.tags = new ArrayList<>(new LinkedHashSet<>(tags));
        }
    }

    public String getCity(){
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
