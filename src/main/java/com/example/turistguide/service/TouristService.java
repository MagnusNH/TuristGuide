package com.example.turistguide.service;

import com.example.turistguide.model.TouristAttraction;
import com.example.turistguide.repository.TouristRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TouristService {
    private final TouristRepository repository;

    public TouristService(TouristRepository repository) {
        this.repository = repository;
    }

    public List<TouristAttraction> getAttractions() {
        return repository.getAllAttractions();
    }

    public TouristAttraction findAttractionByName(String name, String caps) {
        String normalizedInput = normalize(name);
        TouristAttraction attraction = null;

        for (TouristAttraction a : repository.getAllAttractions()) {
            if (normalize(a.getName()).equals(normalizedInput)) {
                attraction = a;
                break;
            }
        }
        if (caps != null && caps.equals("yes") && attraction != null) {
            return new TouristAttraction(attraction.getName(), attraction.getDescription().toUpperCase());
        }
        return attraction;
    }

    private String normalize (String name){
        return name.toLowerCase().replaceAll("\\s+", "");
    }
}
