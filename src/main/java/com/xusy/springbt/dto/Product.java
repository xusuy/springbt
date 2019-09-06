package com.xusy.springbt.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


/**
 * @author xsy
 * @create 2019-09-05 17:45
 * @desc
 **/
@Data
public class Product {
    //限购：1～5
    @Min(value = 1, message = "min Value is above zero")
    @Max(value = 10, message = "最大值不超过10")
    private int count;

    @Pattern(regexp = "^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$", message = "不符合IP规则")
    private String ipAddr;

    @Email(message = "email format error")
    @NotBlank
    private String email;

    @NotBlank(message = "name 不能为空")
    @Length(min=5, max = 10, message = "name长度在5到10个字符之间")
    private String name;
}
