package com.clone.urlshortener.api.controller;

import com.clone.urlshortener.api.controller.ShortenUrlController;
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

}