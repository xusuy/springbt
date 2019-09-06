package com.xusy.springbt.controller;

import com.xusy.springbt.base.BaseController;
import com.xusy.springbt.dto.Product;
import com.xusy.springbt.result.Result;
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
public class ValidationController extends BaseController {
    @GetMapping("/valid/test/{id}")
    public ResponseEntity<Result> handleTest(@PathVariable("id") @Max(10) int id,
                                             @RequestParam("count") @Min(5) int count) {
        return ok(Result.success("validation is done"));
    }

    @PostMapping("/test")
    public ResponseEntity<Result> validateJsonBean(@Valid @RequestBody Product product) {
        return ok(Result.success("validation is done"));
    }
}
