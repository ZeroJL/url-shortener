package com.clone.urlshortener.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class URLDeleteResponse {
    String longUrl;
    String shortUrl;
}
