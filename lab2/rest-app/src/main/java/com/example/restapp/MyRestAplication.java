package com.example.restapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyRestAplication {
    @Autowired
    WebWorker worker;
    @GetMapping(value = "/compare-temperatures")
    String cityAvc(@RequestParam String city1, String city2){
        try {
            System.out.println(city1+" "+ city2);
            return worker.compareCities(city1, city2);
        }
        catch (Exception e){
            System.out.println("Błąd: " + e.getMessage());
            return "Błąd: " + e.getMessage();
        }
    }
}
