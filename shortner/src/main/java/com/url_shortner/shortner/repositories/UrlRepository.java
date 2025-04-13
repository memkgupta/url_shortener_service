package com.url_shortner.shortner.repositories;

import com.url_shortner.shortner.enitities.URL;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlRepository extends JpaRepository<URL,String> {
    Optional<URL> findByShortCode(String code);
}
