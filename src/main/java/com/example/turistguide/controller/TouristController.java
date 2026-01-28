package com.example.turistguide.controller;


import com.example.turistguide.model.TouristAttraction;
import com.example.turistguide.service.TouristService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("attractions")
public class TouristController {
    private final TouristService service;

    public TouristController(TouristService touristService) {
        this.service = touristService;
    }

    @GetMapping()
    public ResponseEntity<List<TouristAttraction>> getAllAttractions() {
        List<TouristAttraction> attraction = service.getAttractions();
        if (attraction == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(attraction, HttpStatus.OK);
    }

    @GetMapping("/{name}")
    public ResponseEntity<TouristAttraction> getAttractionByName(@PathVariable String name, @RequestParam(required = false) String caps) {
        TouristAttraction attraction = service.findAttractionByName(name, caps);

        if (attraction == null) {
            return ResponseEntity.notFound().build();
        }

        if (caps != null && caps.equals("yes")) {
            attraction = new TouristAttraction(attraction.getName(), attraction.getDescription());
        }
        return new ResponseEntity<>(attraction, HttpStatus.OK);
    }
}
