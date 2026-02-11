package com.example.turistguide.model;

public class TouristAttraction {
    private String name;
    private String description;

    public TouristAttraction(){
    }

    public TouristAttraction(String name, String description) {
        this.name = name;
        this.description = description;
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
}
