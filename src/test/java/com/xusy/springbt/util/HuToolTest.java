package com.xusy.springbt.util;

import cn.hutool.core.util.NumberUtil;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author xsy
 * @date 2022-12-27 9:30
 * @description hutool 工具类测试
 */
public class HuToolTest {
    // 计算相关
    @Test
    public void calculate() {
        double eachIntegral = NumberUtil.div(50, 5, 1, RoundingMode.UP);// 相除：取一位小数 四舍五入
        System.out.println(eachIntegral);
    }

    // 数字相关
    // double 去除末尾.0
    private String convertDoubleToString(double num) {
        BigDecimal bd = new BigDecimal(String.valueOf(num));
        return bd.stripTrailingZeros().toPlainString();
    }

    @Test
    public void removeEndZero() {
        System.out.println(convertDoubleToString(0.0));
        System.out.println(convertDoubleToString(1.0));
        System.out.println(convertDoubleToString(1.10));
    }
}
