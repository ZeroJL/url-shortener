package com.clone.urlshortener.api.controller;

import com.clone.urlshortener.domain.exception.ExpiredShortUrlException;
import com.clone.urlshortener.domain.model.URLPair;
import com.clone.urlshortener.domain.service.URLManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class ShortenUrlControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private URLManager urlManager;

    @Test
    void makeShortUrl() throws Exception {
        String longUrl = "http://example.com";
        String shortUrl = "hello";
        when(urlManager.getShortUrl(anyString())).thenReturn(shortUrl);

        String requestBody = "{\"longUrl\":\"" + longUrl + "\"}";

        String expect = "{\"shortUrl\":\"hello\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/shorten-url")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expect));
    }

    @Test
    void redirectUrl_success() throws Exception {
        String shortUrl = "hello";
        when(urlManager.getLongUrl(shortUrl)).thenReturn("https://github.com/ZeroJL");

        mockMvc.perform(MockMvcRequestBuilders.get("/shorten-url/" + shortUrl))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("https://github.com/ZeroJL"));
    }

    @Test
    void redirectUrl_fail() throws Exception {
        String shortUrl = "hello";
        when(urlManager.getLongUrl(shortUrl)).thenThrow(new ExpiredShortUrlException("Url Expired"));

        mockMvc.perform(MockMvcRequestBuilders.get("/shorten-url/" + shortUrl))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/errorPage"));
    }

    @Test
    void deleteShortUrl() throws Exception {
        String shortUrl = "Hello";
        String expect = "{\"longUrl\":\"https://github.com/ZeroJL\",\"shortUrl\":\"Hello\"}";

        when(urlManager.deleteUrl(shortUrl)).thenReturn(new URLPair("https://github.com/ZeroJL", shortUrl));
        mockMvc.perform(MockMvcRequestBuilders.delete("/shorten-url/" + shortUrl))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expect));

    }
}