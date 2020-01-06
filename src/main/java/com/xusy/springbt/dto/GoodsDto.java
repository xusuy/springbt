package com.xusy.springbt.dto;

import lombok.Data;

/**
 * @author xsy
 * @create 2019-09-06 9:43
 * @desc 商品dto
 **/
@Data
public class GoodsDto {
    private Long id;
    private String goodsName;
    private String goodsTitle;
    private String goodsImg;
    private String goodsDetail;
    private Double goodsPrice;
    private Integer goodsStock;
}
