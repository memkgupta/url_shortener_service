package org.url_shortener_mp.analytics_service;

import org.springframework.web.bind.annotation.*;
import org.url_shortener_mp.analytics_service.dtos.RangeAnalyticData;
import org.url_shortener_mp.analytics_service.services.InfluxServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/analytics")
public class Controller {
    private final InfluxServices influxServices;

    public Controller(InfluxServices influxServices) {
        this.influxServices = influxServices;
    }

    @GetMapping("/range/{id}")
    public List<RangeAnalyticData> get(@RequestParam Map<String,String> allParams, @PathVariable String id) {
        System.out.println(id);
       return influxServices.readDataOfRange(allParams,id);
    }

}
