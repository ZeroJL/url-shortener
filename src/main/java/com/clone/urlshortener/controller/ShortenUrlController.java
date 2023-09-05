package com.clone.urlshortener.controller;

import com.clone.urlshortener.dto.URLShortenRequest;
import com.clone.urlshortener.dto.URLShortenResponse;
import com.clone.urlshortener.service.URLManager;
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