package com.url.urlmanager.api.controller;

import com.url.urlmanager.domain.exception.ExpiredShortUrlException;
import com.url.urlmanager.domain.model.URLPair;
import com.url.urlmanager.domain.service.URLShortener;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class ShortenUrlControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private URLShortener urlShortener;

    @Test
    void makeShortUrl() throws Exception {
        String longUrl = "http://example.com";
        String shortUrl = "hello";
        when(urlShortener.getShortUrl(anyString())).thenReturn(shortUrl);

        String requestBody = "{\"longUrl\":\"" + longUrl + "\"}";

        String expect = "{\"shortUrl\":\"hello\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expect));
    }

    @Test
    void redirectUrl_success() throws Exception {
        String shortUrl = "hello";
        when(urlShortener.getLongUrl(shortUrl)).thenReturn("https://github.com/ZeroJL");

        mockMvc.perform(MockMvcRequestBuilders.get("/urls/" + shortUrl))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("https://github.com/ZeroJL"));
    }

    @Test
    void redirectUrl_fail() throws Exception {
        String shortUrl = "hello";
        when(urlShortener.getLongUrl(shortUrl)).thenThrow(new ExpiredShortUrlException("Url Expired"));

        mockMvc.perform(MockMvcRequestBuilders.get("/urls/" + shortUrl))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/errorPage"));
    }

    @Test
    void deleteShortUrl() throws Exception {
        String shortUrl = "Hello";
        String expect = "{\"longUrl\":\"https://github.com/ZeroJL\",\"shortUrl\":\"Hello\"}";

        when(urlShortener.deleteUrl(shortUrl)).thenReturn(new URLPair("https://github.com/ZeroJL", shortUrl));
        mockMvc.perform(MockMvcRequestBuilders.delete("/urls/" + shortUrl))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expect));

    }
}