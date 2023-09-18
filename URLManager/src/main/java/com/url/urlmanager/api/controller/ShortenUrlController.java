package com.url.urlmanager.api.controller;

import com.url.urlmanager.domain.model.URLPair;
import com.url.urlmanager.domain.service.URLManager;
import com.url.urlmanager.api.dto.URLDeleteResponse;
import com.url.urlmanager.api.dto.URLShortenRequest;
import com.url.urlmanager.api.dto.URLShortenResponse;
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