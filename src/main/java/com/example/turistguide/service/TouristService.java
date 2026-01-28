package com.example.turistguide.service;

import org.springframework.stereotype.Service;

@Service
public class TouristService {
    private final TouristRepository repository;

    public TouristService(TouristRepository repository) {
        this.repository = repository;
    }

    public List<TouristAttraction> getAttractions() {return repository.getAllAttractions();}
}
