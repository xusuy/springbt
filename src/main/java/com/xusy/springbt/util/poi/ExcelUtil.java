package com.xusy.springbt.util.poi;

import com.xusy.springbt.dto.Customer;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @Author： xusy
 * @Date 2020/9/28 13:42
 * @Version 1.0
 * @Describe
 */
public class ExcelUtil {

    //导出客户信息excel
    public static ResponseEntity<byte[]> exportExl() throws Exception {
        List<Customer> clist = Customer.getList();
        HSSFWorkbook book = new HSSFWorkbook();
        HSSFSheet sheet = book.createSheet("客户信息表");
        sheet.setDefaultColumnWidth(15);
        //表头行创建
        HSSFRow header = sheet.createRow(0);
        header.createCell(0).setCellValue("职工序号");
        header.createCell(1).setCellValue("联系人姓名");
        header.createCell(2).setCellValue("公司名称");
        header.createCell(3).setCellValue("添加时间");
        header.createCell(4).setCellValue("联系电话");
        //clist数据写入单元格
        for (int i = 0; i < clist.size(); i++) {
            Customer cus = clist.get(i);
            HSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(cus.getId());
            row.createCell(1).setCellValue(cus.getCompanyPerson());
            row.createCell(2).setCellValue(cus.getComName());
            row.createCell(3).setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(cus.getAddTime()));
            row.createCell(4).setCellValue(cus.getComPhone());
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        book.write(bos);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", new String("客户列表.xls".getBytes("GBK"), "ISO-8859-1"));
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(bos.toByteArray(), headers, HttpStatus.OK);
    }
}
