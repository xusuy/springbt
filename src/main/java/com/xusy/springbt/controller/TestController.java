package com.xusy.springbt.controller;

import com.xusy.springbt.service.TestService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author xsy
 * @create 2019-12-27 18:04
 * @desc 测试
 **/
@AllArgsConstructor
@RequestMapping
@RestController
public class TestController {
    private TestService testService;

    @GetMapping(value = "testXml")
    public void testXml() throws IOException {
        testService.testXml();
    }
}
