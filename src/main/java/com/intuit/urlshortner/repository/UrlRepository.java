package com.intuit.urlshortner.repository;


import com.intuit.urlshortner.entities.Url;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UrlRepository extends JpaRepository<Url, Long> {
    Url findByHashedUrl(String hashedUrl);

    List<Url> findByUserIdAndHashedUrl(String userId, String hashedUrl);

    Url findByShortUrl(String shortUrl);
}

