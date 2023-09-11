package com.intuit.urlshortner.service;

import com.intuit.urlshortner.dto.response.UrlResponse;
import com.intuit.urlshortner.dto.resquest.CustomUrlRequest;
import com.intuit.urlshortner.dto.resquest.UrlRequest;
import com.intuit.urlshortner.entities.Url;
import com.intuit.urlshortner.exceptions.CustomShortUrlConflictException;
import com.intuit.urlshortner.exceptions.ShortUrlNotFoundException;
import com.intuit.urlshortner.repository.UrlRepository;
import com.intuit.urlshortner.utils.Base62Utils;
import com.intuit.urlshortner.utils.HashingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Objects.isNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class UrlService {
    private final UrlRepository urlRepository;
    private final UniqueIdGeneratorService uniqueIdGeneratorService;

    @Transactional
    public UrlResponse shortenUrl(UrlRequest request) {
        String originalUrl = request.getOriginalUrl();
        String hashForLongUrl = HashingUtils.generateHash(originalUrl);

        List<Url> urlList = urlRepository.findByUserIdAndHashedUrl(request.getUserId(), hashForLongUrl);

        if (urlList.size() == 2) {
            return createUrlResponseFromUrlsList(urlList);
        }

        if (urlList.size() == 1 && !urlList.get(0).isCustom()) {
            return createUrlResponseFromUrlsList(urlList);
        }

        Url url = new Url().setHashedUrl(hashForLongUrl)
                .setOriginalUrl(originalUrl)
                .setUserId(request.getUserId())
                .setShortUrl(generateShortKey());
        try {
            url = urlRepository.save(url);
            urlList.add(url);
            return createUrlResponseFromUrlsList(urlList);
        } catch (DataIntegrityViolationException ex) {
            log.error("Duplicate entry for the given url");
            throw new CustomShortUrlConflictException("Duplicate entry for the given long url.", ex);
        }
    }


    private String generateShortKey() {
        long uniqueId = uniqueIdGeneratorService.getUniqueId();
        return Base62Utils.toBase62(uniqueId);
    }

    public String getLongUrlByShortUrl(String shortUrl) {
        Url url = urlRepository.findByShortUrl(shortUrl);
        if (isNull(url)) {
            throw new ShortUrlNotFoundException("Short URL not found.");
        }
        return url.getOriginalUrl();
    }

    private UrlResponse createUrlResponseFromUrlsList(List<Url> urlList) {
        UrlResponse urlResponse = new UrlResponse();
        for (Url url : urlList) {
            if (url.isCustom()) {
                urlResponse.setCustomUrl(url.getShortUrl());
            } else {
                urlResponse.setGeneratedShortUrl(url.getShortUrl());
            }
        }
        return urlResponse;
    }

    @Transactional
    public UrlResponse customUrl(CustomUrlRequest request) {
        String originalUrl = request.getOriginalUrl();
        String hashForLongUrl = HashingUtils.generateHash(originalUrl);

        List<Url> urlList = urlRepository.findByUserIdAndHashedUrl(request.getUserId(), hashForLongUrl);

        if (urlList.size() == 2) {
            return createUrlResponseFromUrlsList(urlList);
        }

        if (urlList.size() == 1 && urlList.get(0).isCustom()) {
            return createUrlResponseFromUrlsList(urlList);
        }

        Url url = new Url().setHashedUrl(hashForLongUrl)
                .setOriginalUrl(originalUrl)
                .setUserId(request.getUserId())
                .setCustom(true)
                .setShortUrl(request.getCustomUrl());
        try {
            url = urlRepository.save(url);
            urlList.add(url);
            return createUrlResponseFromUrlsList(urlList);
        } catch (DataIntegrityViolationException ex) {
            log.error("Duplicate entry for the given url");
            throw new CustomShortUrlConflictException("Duplicate entry for the given long url.", ex);
        }
    }
}
