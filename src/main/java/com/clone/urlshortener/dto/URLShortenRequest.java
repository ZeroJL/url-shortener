package com.clone.urlshortener.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class URLShortenRequest {

    private String longUrl;
}
