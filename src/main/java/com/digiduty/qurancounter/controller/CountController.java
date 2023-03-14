package com.digiduty.qurancounter.controller;

import com.digiduty.qurancounter.model.Counts;
import com.digiduty.qurancounter.service.CountService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class CountController {
     final CountService countService;


    public CountController(CountService countService) {
        this.countService = countService;
    }

    @PutMapping("/updateCount")
    public String updateCount(@RequestBody Counts counts) throws ExecutionException, InterruptedException {
        return countService.updateCounts(counts);
    }
}
