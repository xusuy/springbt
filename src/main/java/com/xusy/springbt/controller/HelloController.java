package com.xusy.springbt.controller;

import com.xusy.springbt.model.GoodsModel;
import com.xusy.springbt.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 2016-10-30 23:36
 */
@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping(value = "/result")
    public Result result() {
        GoodsModel goodsModel = new GoodsModel();
        goodsModel.setGoodsId(1l).setGoodsPrice(100000.00).setStock(1);
        return Result.success(goodsModel);
    }

    @GetMapping(value = "/say")
    public String say(@RequestParam(value = "message", required = false, defaultValue = "0") String message) {
        String result = String.format("message: %s,number: %d %n", message, 1);
        System.out.print(result);
        return result;
    }
}
