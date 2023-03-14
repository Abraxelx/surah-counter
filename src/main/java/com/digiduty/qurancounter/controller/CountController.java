package com.digiduty.qurancounter.controller;

import com.digiduty.qurancounter.model.Counts;
import com.digiduty.qurancounter.service.CountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/getAll")
    public List<Counts> getAllCounts() throws ExecutionException, InterruptedException {
        return countService.getAllCounts();
    }
}
