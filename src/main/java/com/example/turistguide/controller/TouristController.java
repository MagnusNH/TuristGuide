package com.example.turistguide.controller;


import com.example.turistguide.model.TouristAttraction;
import com.example.turistguide.service.TouristService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String getAllAttractions(Model model) {
        List<TouristAttraction> attractions = service.getAttractions();
        model.addAttribute("attractions", attractions);

        return "attractions";
    }

    @GetMapping("/{name}")
    public String getAttractionByName(@PathVariable String name, Model model) {
        TouristAttraction attraction = service.findAttractionByName(name);

        if (attraction == null) {
            throw new IllegalArgumentException("no attraction with that name");
        }
        model.addAttribute("attraction", attraction);
        return "attraction";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("attraction", new TouristAttraction());
        return "addAttraction";
    }

    @PostMapping("/add")
    public String addAttraction(@ModelAttribute TouristAttraction touristAttraction) {
        service.addAttraction(touristAttraction);
        return "redirect:/attractions";
    }

    @GetMapping ("/update")
    public String showUpdateForm(Model model){
        model.addAttribute("attractions", service.getAttractions());
        model.addAttribute("attraction", new TouristAttraction());
        return "updateAttraction";
    }

    @PostMapping("/update")
    public String updateAttraction(@RequestParam String oldName, @ModelAttribute TouristAttraction touristAttraction, Model model) {
        TouristAttraction updatedAttraction = service.updateAttraction(oldName, touristAttraction);
        if (updatedAttraction == null) {
            throw new IllegalArgumentException("Could not find the attraction");
        }
        model.addAttribute("attraction", updatedAttraction);
        return "attraction";
    }
    

    @PostMapping("/delete/{name}")
    public String deleteAttraction(@PathVariable String name) {
        TouristAttraction deletedAttraction = service.deleteAttraction(name);
        if(deletedAttraction==null){
            throw new IllegalArgumentException("No attraction with that name");
        }
        return "redirect:/attractions";
    }
}
