package com.xusy.springbt.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {ValidationController.class})
@Slf4j
public class ValidationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void testValidParams() throws Exception {
        mockMvc.perform(get("/valid/test/{id}?count={count}", 11, 6))
                .andExpect(status().isOk());
    }

    @Test
    public void testInValidParams() throws Exception {
        mockMvc.perform(get("/valid/test/{id}?count={count}", 123, 4))
                .andExpect(status().isBadRequest());
    }
}