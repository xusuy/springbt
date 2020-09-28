package com.xusy.springbt.dto;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author： xusy
 * @Date 2020/9/28 13:44
 * @Version 1.0
 * @Describe
 */
@Data
@Builder
public class Customer {
    private Integer id;
    private String companyPerson;
    private String comName;
    private Date addTime;
    private String comPhone;

    public static List<Customer> getList() {
        Customer customer1 = Customer.builder().id(1001).companyPerson("马化腾").comName("腾讯科技")
                .addTime(new Date()).comPhone("19178901234").build();
        Customer customer2 = Customer.builder().id(1002).companyPerson("马云").comName("阿里巴巴")
                .addTime(new Date()).comPhone("19178901235").build();
        Customer customer3 = Customer.builder().id(1003).companyPerson("任正非").comName("华为")
                .addTime(new Date()).comPhone("19178901236").build();
        List<Customer> customerList = new ArrayList<Customer>() {{
            add(customer1);
            add(customer2);
            add(customer3);
        }};
        return customerList;
    }
}

