package com.digiduty.qurancounter.mvc.controller;

import com.digiduty.qurancounter.form.SurahForm;
import com.digiduty.qurancounter.model.SurahsEnum;
import com.digiduty.qurancounter.service.CountService;
import com.digiduty.qurancounter.service.TextNormalizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.digiduty.qurancounter.constants.Constants.SUBHANEKE_LA_ILME_LENA;

@Controller
public class HomeController {
    private static final Logger logger = Logger.getLogger(HomeController.class.getName());

    private final CountService countService;

    @Autowired
    public HomeController(CountService countService) {
        this.countService = countService;
    }


    @GetMapping("/")
    public String home1(ModelMap modelMap) {

        try {
            modelMap.addAttribute("surahForm", new SurahForm());
            modelMap.addAttribute("allCounts", countService.getAllCounts());
            modelMap.addAttribute("allCounts", countService.getAllCounts());
            //Max
            modelMap.addAttribute("allMaxCounts", countService.getAllMaxCounts());
            //Now
            modelMap.addAttribute("allReverseCounts", countService.getAllReverseCounts());
            modelMap.addAttribute("progressBarPercentage", countService.progressBarValueCalculator());

            modelMap.addAttribute("dailyHadis", countService.getDailyHadis());
            modelMap.addAttribute("dailyHadis", countService.getDailyHadis());
            modelMap.addAttribute("subhanekeText",  TextNormalizer.normalizeTextForForm(SUBHANEKE_LA_ILME_LENA));



        } catch (ExecutionException | InterruptedException e) {
            logger.log(Level.WARNING, "Interrupted", e);
            Thread.currentThread().interrupt();
        }

        return "index";
    }

    @PostMapping("/updateSurahCount")
    public String updateSurahs(@ModelAttribute(value = "surahForm") SurahForm surahForm) {

        try {
            countService.updateCounts(SurahsEnum.of(surahForm.getSurahType()), surahForm.getCount());

        } catch (Exception e) {
            logger.log(Level.WARNING, "Updating Interrupted", e);
            Thread.currentThread().interrupt();
        }

        return "redirect:/";
    }

}
