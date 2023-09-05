package com.clone.urlshortener.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Calendar;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Document("collection = url_pair")
public class URLPair {

    @Id
    private String longUrl;
    @Getter
    private String shortUrl;
    private Date expiration;

    public URLPair(String longUrl, String shortUrl) {
        this(longUrl, shortUrl, getDefaultExpiration());
    }

    private static Date getDefaultExpiration() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }
}
