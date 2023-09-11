package com.intuit.urlshortner;

import com.intuit.urlshortner.dto.response.UrlResponse;
import com.intuit.urlshortner.dto.resquest.CustomUrlRequest;
import com.intuit.urlshortner.dto.resquest.UrlRequest;
import com.intuit.urlshortner.entities.Url;
import com.intuit.urlshortner.exceptions.CustomShortUrlConflictException;
import com.intuit.urlshortner.exceptions.ShortUrlNotFoundException;
import com.intuit.urlshortner.repository.UrlRepository;
import com.intuit.urlshortner.service.UniqueIdGeneratorService;
import com.intuit.urlshortner.service.UrlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class UrlServiceTest {

    @Mock
    private UrlRepository urlRepository;

    @Mock
    private UniqueIdGeneratorService uniqueIdGeneratorService;

    @InjectMocks
    private UrlService urlService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testShortenUrl_ValidInput() {
        // Mocking
        UrlRequest request = new UrlRequest();
        request.setOriginalUrl("https://www.example.com");
        request.setUserId("user123");
        when(uniqueIdGeneratorService.getUniqueId()).thenReturn(123L);
        when(urlRepository.findByUserIdAndHashedUrl(anyString(), anyString())).thenReturn(new ArrayList<>());
        when(urlRepository.save(any(Url.class))).thenReturn(new Url().setShortUrl("abc"));

        // Execution
        UrlResponse response = urlService.shortenUrl(request);

        // Assertion
        assertNotNull(response);
        assertEquals("abc", response.getGeneratedShortUrl());
    }

    @Test
    public void testShortenUrl_ValidInput_CustomAndGeneratedUrlPresent() {
        // Mocking
        UrlRequest request = new UrlRequest();
        request.setOriginalUrl("https://www.example.com");
        request.setUserId("abcd123");
        when(urlRepository.findByUserIdAndHashedUrl(anyString(), anyString())).thenReturn(getCustomAndGeneratedShotUrlResponse());

        // Execution
        UrlResponse response = urlService.shortenUrl(request);

        // Assertion
        assertNotNull(response);
        assertEquals("abcd1234", response.getCustomUrl());
        assertEquals("abcd123", response.getGeneratedShortUrl());
    }

    @Test
    public void testShortenUrl_ValidInput_CustomUrlPresent() {
        // Mocking
        UrlRequest request = new UrlRequest();
        request.setOriginalUrl("https://www.example.com");
        request.setUserId("userId");
        when(urlRepository.findByUserIdAndHashedUrl(anyString(), anyString())).thenReturn(getGeneratedUrlResponse());
        when(urlRepository.save(any())).thenReturn(new Url().setShortUrl("abcd123"));
        // Execution
        UrlResponse response = urlService.shortenUrl(request);

        // Assertion
        assertNotNull(response);
        assertEquals("abcd123", response.getGeneratedShortUrl());
    }

    @Test
    public void testCustomUrl_ValidInput_CustomUrlPresent() {
        // Mocking
        CustomUrlRequest request = new CustomUrlRequest();
        request.setOriginalUrl("https://www.example.com");
        request.setUserId("userId");
        when(urlRepository.findByUserIdAndHashedUrl(anyString(), anyString())).thenReturn(getCustomUrlResponse());
        // Execution
        UrlResponse response = urlService.customUrl(request);

        // Assertion
        assertNotNull(response);
        assertEquals("abcd1234", response.getCustomUrl());
    }



    @Test
    public void testGetLongUrlByShortUrl_ExistingShortUrl() {
        // Mocking
        when(urlRepository.findByShortUrl("abc")).thenReturn(new Url().setOriginalUrl("https://www.example.com"));

        // Execution
        String longUrl = urlService.getLongUrlByShortUrl("abc");

        // Assertion
        assertNotNull(longUrl);
        assertEquals("https://www.example.com", longUrl);
    }

    @Test
    public void testGetLongUrlByShortUrl_NonExistingShortUrl() {
        // Mocking
        when(urlRepository.findByShortUrl("xyz")).thenReturn(null);

        // Assertion
        assertThrows(ShortUrlNotFoundException.class, () -> urlService.getLongUrlByShortUrl("xyz"));
    }

    @Test
    public void testCustomUrl_ValidInput() {
        // Mocking
        CustomUrlRequest request = new CustomUrlRequest();
        request.setOriginalUrl("https://www.example.com");
        request.setUserId("user123");
        request.setCustomUrl("custom123");
        when(urlRepository.findByUserIdAndHashedUrl(anyString(), anyString())).thenReturn(new ArrayList<>());
        when(urlRepository.save(any(Url.class))).thenReturn(new Url().setShortUrl("custom123").setCustom(true));



        // Execution
        UrlResponse response = urlService.customUrl(request);

        // Assertion
        assertNotNull(response);
        assertEquals("custom123", response.getCustomUrl());
    }

    @Test
    public void testCustomUrl_ValidInput_CustomAndGeneratedShortUrlPresent() {
        // Mocking
        CustomUrlRequest request = new CustomUrlRequest();
        request.setOriginalUrl("https://www.example.com");
        request.setUserId("user123");
        request.setCustomUrl("custom123");
        when(urlRepository.findByUserIdAndHashedUrl(anyString(), anyString())).thenReturn(getCustomAndGeneratedShotUrlResponse());

        // Execution
        UrlResponse response = urlService.customUrl(request);

        // Assertion
        assertNotNull(response);
        assertEquals("abcd1234", response.getCustomUrl());
        assertEquals("abcd123", response.getGeneratedShortUrl());
    }

    private List<Url> getCustomAndGeneratedShotUrlResponse() {
        List<Url> urlList = new ArrayList<>();
        urlList.add(new Url().setCustom(true).setShortUrl("abcd1234"));
        urlList.add(new Url().setShortUrl("abcd123"));
        return urlList;
    }

    private List<Url> getCustomUrlResponse() {
        List<Url> urlList = new ArrayList<>();
        urlList.add(new Url().setCustom(true).setShortUrl("abcd1234"));
        return urlList;
    }

    private List<Url> getGeneratedUrlResponse() {
        List<Url> urlList = new ArrayList<>();
        urlList.add(new Url().setShortUrl("abcd123"));
        return urlList;
    }

    @Test
    public void testCustomUrl_DuplicateEntryException() {
        // Mocking
        CustomUrlRequest request = new CustomUrlRequest();
        request.setOriginalUrl("https://www.example.com");
        request.setUserId("user123");
        request.setCustomUrl("custom");
        when(urlRepository.findByUserIdAndHashedUrl(anyString(), anyString())).thenReturn(new ArrayList<>());
        when(urlRepository.save(any(Url.class))).thenThrow(new DataIntegrityViolationException("Duplicate entry"));

        // Assertion
        assertThrows(CustomShortUrlConflictException.class, () -> urlService.customUrl(request));
    }

    @Test
    public void testGeneratedUrl_DuplicateEntryException() {
        // Mocking
        UrlRequest request = new UrlRequest();
        request.setOriginalUrl("https://www.example.com");
        request.setUserId("user123");
        when(urlRepository.findByUserIdAndHashedUrl(anyString(), anyString())).thenReturn(new ArrayList<>());
        when(urlRepository.save(any(Url.class))).thenThrow(new DataIntegrityViolationException("Duplicate entry"));

        // Assertion
        assertThrows(CustomShortUrlConflictException.class, () -> urlService.shortenUrl(request));
    }
}
