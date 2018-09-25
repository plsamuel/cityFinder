package com.plsamuel.cityfinder;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void givenQueryString_whenGettingSuggestions_returnJSON() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/suggestions?q=NotSomethingReal").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"suggestions\":[]}")));
    }
    @Test
    public void givenQueryWithOnlyLatitude_whenGettingSuggestions_returnBadRequest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/suggestions?q=NotSomethingReal&latitude=30").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
    @Test
    public void givenQueryWithOnlyLongitude_whenGettingSuggestions_returnBadRequest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/suggestions?q=NotSomethingReal&longitude=30").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
    @Test
    public void givenQueryWithLatLong_whenGettingSuggestions_returnJSON() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/suggestions?q=NotSomethingReal&latitude=30&longitude=30").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"suggestions\":[]}")));
    }
}