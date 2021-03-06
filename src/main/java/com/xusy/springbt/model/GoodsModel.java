package com.xusy.springbt.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xsy
 * @create 2019-09-06 9:44
 * @desc 商品model
 **/
@Data
@Accessors(chain = true)//支持链式构建
@JsonInclude(JsonInclude.Include.NON_NULL)//去除null的字段转换json
public class GoodsModel {
    /**
     * id
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品库存
     */
    private Integer stock;

    /**
     * 商品价格
     */
    private Double goodsPrice;


}
