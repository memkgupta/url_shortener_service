package org.url_shortener_mp.analytics_service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.url_shortener_mp.analytics_service.dtos.DailyClicks;
import org.url_shortener_mp.analytics_service.dtos.DashboardPayload;
import org.url_shortener_mp.analytics_service.dtos.RangeAnalyticData;
import org.url_shortener_mp.analytics_service.services.LogService;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/analytics")
public class Controller {


    private final LogService logService;

    public Controller(LogService logService) {
        this.logService = logService;
    }

    @GetMapping("/range/{id}")
    public List<DailyClicks> get(@RequestParam Map<String,String> allParams, @PathVariable String id) {

       return logService.getAnalytics(allParams);
    }
    @GetMapping("/dashboard/{id}")
    public ResponseEntity<DashboardPayload> getDashboard(@PathVariable String id) {

        return ResponseEntity.ok( logService.getDashboard(id));
    }
}
