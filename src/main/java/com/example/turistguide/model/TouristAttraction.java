package com.example.turistguide.model;

import java.util.ArrayList;
import java.util.List;

public class TouristAttraction {
    private String name;
    private String description;
    private List<String> tags;

    public TouristAttraction(){
    }

    public TouristAttraction(String name, String description) {
        this.name = name;
        this.description = description;
        this.tags = new ArrayList<>();
    }

    public TouristAttraction (String name, String description, List<String> tags) {
        this.name = name;
        this.description = description;
        this.tags = tags != null ? tags : new ArrayList<>();
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
        this.tags = tags != null ? tags : new ArrayList<>();
    }

    public void addTag(String tag) {
        if (!tags.contains(tag)) {
            tags.add(tag);
        }
    }

    public void removeTag(String tag) {
        tags.remove(tag);
    }
}
