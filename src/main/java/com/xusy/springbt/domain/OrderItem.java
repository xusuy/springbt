package com.xusy.springbt.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xusy
 * @date 2022-07-13 11:36
 * @description 我的订代办单列表项返回参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 帮代办单号
     */
    private String orderNo;

    /**
     * 申请人姓名
     */
    private String name;

    /**
     * 联系方式
     */
    private String contact;

    /**
     * 状态（1待受理、2受理中、3不受理、4异常归档、5已归档）
     */
    private String status;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createTime;

    /**
     * 申请事项
     */
    private String matterName;
}
