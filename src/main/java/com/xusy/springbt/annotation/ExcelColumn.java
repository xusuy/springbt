package com.xusy.springbt.annotation;

import java.lang.annotation.*;

/**
 * @version 1.0
 * @date 2020/2/25 14:14
 * 自定义实体类所需要的bean(Excel属性标题、位置等)
 **/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelColumn {
    /**
     * Excel标题
     *
     * @return
     */
    String value() default "";

    /**
     * Excel从左往右排列位置
     *
     * @return
     */
    int col() default 0;
}