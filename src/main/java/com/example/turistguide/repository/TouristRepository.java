package com.example.turistguide.repository;


import com.example.turistguide.model.TouristAttraction;

import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Repository
public class TouristRepository {
    List<TouristAttraction> attractions = new ArrayList<TouristAttraction>();
    private String name;

    public void addAttraction (TouristAttraction attraction) {
        attractions.add(attraction);
    }

    public TouristRepository() {
        attractions.add(new TouristAttraction("Tivoli", "København"));
        attractions.add(new TouristAttraction("Den lille havfrue", "Statue ved havnen"));
        attractions.add(new TouristAttraction("Kronborg slot", "Hamlets slot"));
    }

    public List<TouristAttraction> getAllAttractions() {
        return attractions;
    }

    public TouristAttraction getAttractionByName(String name) {
        for (TouristAttraction attraction : attractions) {
            if (attraction.getName().equalsIgnoreCase(name)) {
                return attraction;
            }
        }
        return null;
    }

    // READ - tags for specifik attraktion
    public List<String> getTagsByAttractionName(String name) {
        TouristAttraction attraction = getAttractionByName(name);
        if (attraction != null && attraction.getTags() != null) {
            return attraction.getTags();
        }
        return new ArrayList<>();
    }

    public boolean updateAttraction(String name, TouristAttraction updatedAttraction) {
        for (int i = 0; i < attractions.size(); i++) {
            if (attractions.get(i).getName().equalsIgnoreCase(updatedAttraction.getName())) {
                attractions.set(i, updatedAttraction);
                return true;
            }
        }
        return false;
    }

    public boolean deleteAttraction (String name) {
        return attractions.removeIf(attraction -> attraction.getName().equalsIgnoreCase(name));
    }

    public boolean attractionExists(String name) {
        return getAttractionByName(name) != null;
    }

    public int getAttractionCount() {
        return attractions.size();
    }
}