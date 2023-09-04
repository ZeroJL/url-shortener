package com.clone.urlshortener.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(ShortenUrlController.class)
class ShortenUrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void makeShortUrl() throws Exception {
        String expect = "{\"shortUrl\":\"ok\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/shorten-url"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expect));
    }

}