package com.intuit.urlshortner.controller;

import com.intuit.urlshortner.dto.response.UrlResponse;
import com.intuit.urlshortner.dto.resquest.CustomUrlRequest;
import com.intuit.urlshortner.dto.resquest.UrlRequest;
import com.intuit.urlshortner.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
public class UrlController {
    private final UrlService urlService;

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return new ResponseEntity<>("Service is up and running", HttpStatus.OK);
    }

    @PostMapping("/shorten")
    public ResponseEntity<UrlResponse> shortenUrl(@RequestBody @Validated UrlRequest request) {
        return new ResponseEntity<>(urlService.shortenUrl(request), HttpStatus.CREATED);
    }

    @PostMapping("/custom-shorten")
    public ResponseEntity<UrlResponse> customShortenUrl(@RequestBody @Validated CustomUrlRequest request) {
        return new ResponseEntity<>(urlService.customUrl(request), HttpStatus.CREATED);
    }

    @GetMapping("/{shortUrl}")
    public RedirectView getLongUrlByShortUrl(@PathVariable String shortUrl) {
        String longUrl = urlService.getLongUrlByShortUrl(shortUrl);
        RedirectView redirectView = new RedirectView(longUrl);
        redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        return new RedirectView(longUrl);
    }
}
