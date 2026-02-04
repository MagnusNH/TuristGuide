package com.example.turistguide.controller;


import com.example.turistguide.model.TouristAttraction;
import com.example.turistguide.service.TouristService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/add")
    public ResponseEntity<TouristAttraction> addAttraction(@RequestBody TouristAttraction touristAttraction) {
        TouristAttraction addedAttraction = service.addAttraction(touristAttraction);
        return ResponseEntity.ok(addedAttraction);
    }

    @PostMapping("/update")
    public ResponseEntity<TouristAttraction> updateAttraction(@RequestBody String name, TouristAttraction touristAttraction) {
        TouristAttraction updatedAttraction = service.updateAttraction(name, touristAttraction);
        return ResponseEntity.ok(updatedAttraction);
    }

    @PostMapping("/delete/{name}")
    public ResponseEntity<String> deleteAttraction(@PathVariable String name) {
        service.deleteAttraction(name);
        return ResponseEntity.ok(name);
    }
}
