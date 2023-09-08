package com.clone.urlshortener.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Calendar;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Document("collection = url_pair")
public class URLPair {

    @Id @Getter
    private String longUrl;
    @Getter @Setter
    private String shortUrl;
    private Date expiration;
    @Version
    private Long version;

    public URLPair(String longUrl) {
        this(longUrl, "", getDefaultExpiration(), null);
    }

    public URLPair(String longUrl, String shortUrl) {
        this(longUrl, shortUrl, getDefaultExpiration(), null);
    }

    private static Date getDefaultExpiration() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }


}
