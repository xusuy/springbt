package com.xusy.springbt.model.excel;

import com.xusy.springbt.annotation.ExcelColumn;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 承保数据导入实体类
 * 生鲜类
 **/
@Data
public class InsurancesheetModel {
    @ExcelColumn(value = "机构名称", col = 2)
    private String jigoumingchen;

    @ExcelColumn(value = "青龙业主id", col = 3)
    private String qinglongyezhuid;

    @ExcelColumn(value = "商家名称", col = 4)
    private String shangjiamingchen;

    @ExcelColumn(value = "商家类型", col = 5)
    private String shangjialeixing;

    @ExcelColumn(value = "商家主营行业", col = 6)
    private String shangjiazhuyingleixing;

    @ExcelColumn(value = "运单号", col = 7)
    private String yundanhao;

    @ExcelColumn(value = "揽收时间", col = 8)
    private String lanshoushijian;

    @ExcelColumn(value = "尺寸", col = 9)
    private String chichun;

    @ExcelColumn(value = "数量", col = 10)
    private long shuliang;

    @ExcelColumn(value = "始发城市", col = 11)
    private String shifachengshi;

    @ExcelColumn(value = "目的城市", col = 12)
    private String mudichengshi;

    @ExcelColumn(value = "声明价值", col = 13)
    private BigDecimal shengmingjiazhi;

    @ExcelColumn(value = "保费", col = 14)
    private BigDecimal baofei;

    @ExcelColumn(value = "保险外付费", col = 15)
    private BigDecimal baoxainewaifei;

    @ExcelColumn(value = "签约机构", col = 16)
    private String qianyuejigou;

    @ExcelColumn(value = "生鲜专送", col = 17)
    private String shengxianzhuansong;

    @ExcelColumn(value = "托寄物内容", col = 18)
    private String tuojineirong;
}
