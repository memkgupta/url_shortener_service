package com.url_shortner.shortner.controllers;

import com.url_shortner.shortner.dtos.CreateURLRequest;
import com.url_shortner.shortner.dtos.CreateURLResponse;
import com.url_shortner.shortner.services.URLService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/url")
@RequiredArgsConstructor
public class URLController {
    private final URLService urlService;
    @PostMapping("/create")
    public CreateURLResponse createURL(@RequestBody CreateURLRequest request) {
        System.out.println(request);
       return urlService.createURL(request);
    }

}
