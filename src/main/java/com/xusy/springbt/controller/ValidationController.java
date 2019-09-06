package com.xusy.springbt.controller;

import com.xusy.springbt.dto.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @author xsy
 * @create 2019-09-05 17:55
 * @desc validation验证
 **/
@RestController
@Validated
public class ValidationController {
    @GetMapping("/valid/test/{id}")
    public ResponseEntity<String> handleTest(@PathVariable("id") @Max(10) int id,
                                             @RequestParam("count") @Min(5) int count) {
        return ResponseEntity.ok("validation is done");
    }

    @PostMapping("/test")
    public ResponseEntity<String> validateJsonBean(@Valid @RequestBody Product product) {
        return ResponseEntity.ok("valid id done");
    }
}
