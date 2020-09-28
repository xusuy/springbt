package com.xusy.springbt.controller;

import com.xusy.springbt.util.poi.ExcelUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author： xusy
 * @Date 2020/9/28 14:17
 * @Version 1.0
 * @Describe
 */
@RestController
@RequestMapping("excel")
public class ExcelController {
    @GetMapping
    public ResponseEntity<byte[]> exportExl() throws Exception {
        return ExcelUtil.exportExl();
    }
}
