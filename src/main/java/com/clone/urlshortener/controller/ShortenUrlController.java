package com.clone.urlshortener.controller;

import com.clone.urlshortener.dto.URLShortenRequest;
import com.clone.urlshortener.dto.URLShortenResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShortenUrlController {


    @PostMapping("/shorten-url")
    public URLShortenResponse makeShortUrl(URLShortenRequest urlShortenRequest) {
        return new URLShortenResponse("ok");
    }
}