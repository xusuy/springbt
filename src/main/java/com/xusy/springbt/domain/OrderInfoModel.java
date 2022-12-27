package com.xusy.springbt.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author xusy
 * @date 2022-07-13 11:33
 * @description 我的代办单列表返回参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderInfoModel implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;

    /**
     * 订单项
     */
    private List<OrderItem> orderItems;
}
