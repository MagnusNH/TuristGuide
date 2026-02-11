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

    public TouristAttraction findAttractionByName(String name) {
        String normalizedInput = normalize(name);
        //Sætter tourist Attraction til null, da vi ikke endnu ved, om vi finder noget.
        TouristAttraction attraction = null;

        //For Loop til at få navnet på attraktion og fjerne mellemrum
        for (TouristAttraction a : repository.getAllAttractions()) {
            if (normalize(a.getName()).equals(normalizedInput)) {
                attraction = a;
                break;
            }
        }
        return attraction;
    }

    private String normalize (String name){
        return name.toLowerCase().replaceAll("\\s+", "");
    }

    public TouristAttraction addAttraction(TouristAttraction touristAttraction) {
        repository.addAttraction(touristAttraction);
        return touristAttraction;
    }

    public TouristAttraction updateAttraction(String oldName, TouristAttraction newValues) {
        TouristAttraction existing = findAttractionByName(oldName);
        if(existing==null) return null;

        existing.setName(newValues.getName());
        existing.setDescription(newValues.getDescription());

        boolean updated = repository.updateAttraction(existing);
        if(!updated) return null;

        return existing;
    }

    public TouristAttraction deleteAttraction(String name) {
        TouristAttraction attraction = findAttractionByName(name);
        if (attraction==null) return null;

        boolean deleted = repository.deleteAttraction(name);
        if(!deleted){
            return null;
        }

        return attraction;
    }

    public List<String> getTagsByAttractionName(String name) {
        return repository.getTagsByAttractionName(name);
    }

    public boolean attractionExists(String name) {
        return repository.attractionExists(name);
    }

    public int getAttractionCount() {
        return repository.getAttractionCount();
    }
}
