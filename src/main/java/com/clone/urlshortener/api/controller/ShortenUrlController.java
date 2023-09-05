package com.clone.urlshortener.api.controller;

import com.clone.urlshortener.api.dto.URLShortenRequest;
import com.clone.urlshortener.api.dto.URLShortenResponse;
import com.clone.urlshortener.domain.service.URLManager;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ShortenUrlController {

    private final URLManager urlManager;

    @PostMapping("/shorten-url")
    public URLShortenResponse makeShortUrl(@RequestBody URLShortenRequest urlShortenRequest) {
        return new URLShortenResponse(urlManager.getShortUrl(urlShortenRequest.getLongUrl()));
    }
}