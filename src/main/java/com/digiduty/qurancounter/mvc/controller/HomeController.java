package com.digiduty.qurancounter.mvc.controller;

import com.digiduty.qurancounter.form.SurahForm;
import com.digiduty.qurancounter.model.SurahsEnum;
import com.digiduty.qurancounter.service.CountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.concurrent.ExecutionException;

@Controller
public class HomeController {

    final CountService countService;

    public HomeController(CountService countService) {
        this.countService = countService;
    }


    @GetMapping("/")
    public String home1(ModelMap modelMap) {

        try {
            modelMap.addAttribute("surahForm",new SurahForm());
            modelMap.addAttribute("allCounts",countService.getAllCounts());
            modelMap.addAttribute("allCounts",countService.getAllCounts());
            //Max
            modelMap.addAttribute("allMaxCounts",countService.getAllMaxCounts());
            //Now
            modelMap.addAttribute("allReverseCounts",countService.getAllReverseCounts());
            modelMap.addAttribute("progressBarPerc",countService.progressBarValueCalculator());

            modelMap.addAttribute("dailyHadis",countService.getDailyHadis());


        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return "index";
    }

    @PostMapping("/updateSurahCount")
    public String updateSurahs(@ModelAttribute(value = "surahForm") SurahForm surahForm, ModelMap modelMap) {

        try {
            countService.updateCounts(SurahsEnum.of(surahForm.getSurahType()), surahForm.getCount());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/";
    }

}
