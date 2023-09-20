package com.url.urlmanager.api.controller;

import com.url.urlmanager.domain.model.URLPair;
import com.url.urlmanager.domain.service.URLShortener;
import com.url.urlmanager.api.dto.URLDeleteResponse;
import com.url.urlmanager.api.dto.URLShortenRequest;
import com.url.urlmanager.api.dto.URLShortenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
public class ShortenUrlController {

    private final URLShortener urlShortener;

    @PostMapping("/urls")
    public URLShortenResponse makeShortUrl(@RequestBody URLShortenRequest urlShortenRequest) {
        return new URLShortenResponse(urlShortener.getShortUrl(urlShortenRequest.getLongUrl()));
    }

    @GetMapping("/urls/{key}")
    public RedirectView redirectUrl(@PathVariable String key) {
        try {
            return new RedirectView(urlShortener.getLongUrl(key));
        } catch (Exception e) {
            return new RedirectView("/errorPage");
        }
    }

    @DeleteMapping("/urls/{key}")
    public URLDeleteResponse deleteShortUrl(@PathVariable String key) {
        URLPair urlPair = urlShortener.deleteUrl(key);
        return new URLDeleteResponse(urlPair.getLongUrl(), urlPair.getShortUrl());
    }
}