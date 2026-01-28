package com.example.turistguide.service;

import com.example.turistguide.model.TouristAttraction;
import com.example.turistguide.repository.TouristRepository;
import org.apache.logging.log4j.message.Message;
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
        TouristAttraction attraction = repository.getAttractionByName(name);
        if (caps != null && caps.equals("yes") && attraction != null) {
            return new TouristAttraction(attraction.getName(), attraction.getDescription().toUpperCase());
        }
        return attraction;
    }
}
