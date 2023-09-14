package com.clone.urlshortener.api.controller;

import com.clone.urlshortener.api.dto.URLDeleteResponse;
import com.clone.urlshortener.api.dto.URLShortenRequest;
import com.clone.urlshortener.api.dto.URLShortenResponse;
import com.clone.urlshortener.domain.model.URLPair;
import com.clone.urlshortener.domain.service.URLManager;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
public class ShortenUrlController {

    private final URLManager urlManager;

    @PostMapping("/urls")
    public URLShortenResponse makeShortUrl(@RequestBody URLShortenRequest urlShortenRequest) {
        return new URLShortenResponse(urlManager.getShortUrl(urlShortenRequest.getLongUrl()));
    }

    @GetMapping("/urls/{key}")
    public RedirectView redirectUrl(@PathVariable String key) {
        try {
            return new RedirectView(urlManager.getLongUrl(key));
        } catch (Exception e) {
            return new RedirectView("/errorPage");
        }
    }

    @DeleteMapping("/urls/{key}")
    public URLDeleteResponse deleteShortUrl(@PathVariable String key) {
        URLPair urlPair = urlManager.deleteUrl(key);
        return new URLDeleteResponse(urlPair.getLongUrl(), urlPair.getShortUrl());
    }
}