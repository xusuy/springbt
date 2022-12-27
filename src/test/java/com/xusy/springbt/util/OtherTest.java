package com.xusy.springbt.util;

import cn.hutool.json.JSONUtil;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author xusy
 * @date 2022-07-14 9:13
 * @description TODO
 */
public class OtherTest {

    @Test
    public void test1() {
        System.out.println("======================");
        String json1 = "[1,2,3]";
        //hutool josnutil
        List<String> string1List = JSONUtil.toList(json1, String.class);
        string1List.forEach(System.out::println);
    }
}
