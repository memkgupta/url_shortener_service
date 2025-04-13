package com.url_shortner.shortner.services;

import com.url_shortner.shortner.config.Base62Encoder;
import com.url_shortner.shortner.config.Counter;
import com.url_shortner.shortner.config.MD5HashGenerator;
import com.url_shortner.shortner.dtos.CreateURLRequest;
import com.url_shortner.shortner.dtos.CreateURLResponse;
import com.url_shortner.shortner.enitities.URL;
import com.url_shortner.shortner.repositories.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class URLService {
    private final UrlRepository urlRepository;
private final  Counter counter;
    public CreateURLResponse createURL(CreateURLRequest request) {
        URL newURL = new URL();
        newURL.setCreated_at(request.getCreated_at()!=null ? request.getCreated_at():new Timestamp(System.currentTimeMillis()));
        if(request.getExpiryTime()!=null)
        {
            newURL.setExpired_at(request.getExpiryTime());
        }
        String token = Base62Encoder.encode(counter.getCounter());
        newURL.setShortCode(token);
        newURL.setUrl(request.getLong_url());
        urlRepository.save(newURL);
        return CreateURLResponse.builder()
                .url("http://localhost:8080/"+newURL.getShortCode())
                .id(newURL.getId())
                .timestamp(newURL.getCreated_at())
                .build();

    }
    @Cacheable(value = "urls", key = "#shortCode", unless = "#result == null")
    public URL fetchURL(String shortCode) {
        System.out.println("DB CALLED");
        URL url = urlRepository.findByShortCode(shortCode).orElse(null);
        if(url==null){
            throw new RuntimeException("URL not found");
        }
        return url;
    }
}
