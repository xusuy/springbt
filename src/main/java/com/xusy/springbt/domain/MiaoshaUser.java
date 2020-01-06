package com.xusy.springbt.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author xsy
 * @create 2020-01-06 15:24
 * @desc
 **/
@Data
@Accessors(chain = true)
public class MiaoshaUser {
    private Long id;
    private String nickname;
    private String password;
    private String salt;
    private String head;
    private Date registerDate;
    private Date lastLoginDate;
    private Integer loginCount;
}
