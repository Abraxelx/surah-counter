package com.digiduty.qurancounter.controller;

import com.digiduty.qurancounter.service.CountService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class SitemapController {

    private final CountService service;

    public SitemapController(CountService service) {
        this.service = service;
    }

    @GetMapping("sitemap.xml")
    public void showSitemapAsAPage(HttpServletResponse response) throws IOException {
        byte[] siteMap = service.getSiteMap();
        response.setContentType("application/xml");
        response.getOutputStream().write(siteMap);
        response.flushBuffer();
    }
}
