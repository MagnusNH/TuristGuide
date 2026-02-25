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
    private static final List <String> ALL_TAGS = List.of("forlystelsespark", "familievenlig","kultur", "historie", "havet", "slot");

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
        model.addAttribute("allTags", ALL_TAGS);
        return "addAttraction";
    }

    @PostMapping("/save")
    public String addAttraction(@ModelAttribute("attraction") TouristAttraction touristAttraction) {
        service.addAttraction(touristAttraction);
        return "redirect:/attractions";
    }

    @GetMapping ("/update")
    public String showUpdateForm(Model model){
        model.addAttribute("attractions", service.getAttractions());
        model.addAttribute("attraction", new TouristAttraction());
        return "updateAttraction";
    }
    

    @PostMapping("/delete/{name}")
    public String deleteAttraction(@PathVariable String name) {
        TouristAttraction deletedAttraction = service.deleteAttraction(name);
        if(deletedAttraction==null){
            throw new IllegalArgumentException("No attraction with that name");
        }
        return "redirect:/attractions";
    }

    @GetMapping ("/{name}/edit")
    public String editAttraction(@PathVariable String name, Model model){
        TouristAttraction attraction = service.findAttractionByName(name);

        if (attraction==null){
            throw new IllegalArgumentException("No attraction with that name");
            }
        model.addAttribute("attraction", attraction);
        model.addAttribute("oldName", name);
        model.addAttribute("allTags", ALL_TAGS);
        return "editAttraction";
        }

    @PostMapping("/update")
    public String updateAttraction(@RequestParam String oldName, @ModelAttribute TouristAttraction touristAttraction) {
        TouristAttraction updatedAttraction = service.updateAttraction(oldName, touristAttraction);
        if (updatedAttraction == null) {
            throw new IllegalArgumentException("Could not find the attraction");
        }
        return "redirect:/attractions/" + updatedAttraction.getName();
    }

    @GetMapping("/{name}/tags")
    public String showTags(@PathVariable String name, Model model){
            TouristAttraction attraction = service.findAttractionByName(name);
            if (attraction == null) {
                throw new IllegalArgumentException("No attraction with that name");
            }

            attraction.setTags(attraction.getTags());

            model.addAttribute("attraction", attraction);
            return "tags";
    }
    }
