package com.digiduty.qurancounter.controller;

import com.digiduty.qurancounter.model.Counts;
import com.digiduty.qurancounter.model.MaxCounts;
import com.digiduty.qurancounter.model.SurahsEnum;
import com.digiduty.qurancounter.service.CountService;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class CountController {
    private final CountService countService;

    public CountController(CountService countService) {
        this.countService = countService;
    }

    @PutMapping("/updateCount")
    public String updateCount(@RequestParam SurahsEnum surahName, @RequestParam int decreaseVal) throws ExecutionException, InterruptedException {
        return countService.updateCounts(surahName, decreaseVal);
    }

    @GetMapping("/getAll")
    public Counts getAllCounts() throws ExecutionException, InterruptedException {
        return countService.getAllCounts();
    }

    @GetMapping("/getMaxCounts")
    public MaxCounts getAllMaxCounts() throws ExecutionException, InterruptedException {
        return countService.getAllMaxCounts();
    }
}
