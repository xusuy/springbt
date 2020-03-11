package com.xusy.springbt.controller;

import com.xusy.springbt.model.excel.InsurancesheetModel;
import com.xusy.springbt.result.CodeMsg;
import com.xusy.springbt.result.Result;
import com.xusy.springbt.service.BigDataTestService;
import com.xusy.springbt.util.ExcelService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 大数据量测试
 */
@RequestMapping("/bigData")
@RestController
@AllArgsConstructor
public class BigDataTestController {
    private BigDataTestService bigDataTestService;
    private ExcelService excelService;

    /**
     * 百万级excel数据导入
     * 测试：10万条数据1分35秒；20万条7分9秒。。。，文件超过10m 构建poi Workbook对象时内存溢出
     * @param multipartFile 文件
     * @return
     * @throws Exception
     */
    @PostMapping("millionsExcelImport")
    public Result millionsExcelImport(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        Map<String, Object> excelData = new HashMap<>();
        String fileName = multipartFile.getOriginalFilename();
        InputStream inputStream = multipartFile.getInputStream();
        if (fileName.contains("生鲜")) {
            excelData = excelService.readExcelInputStream(InsurancesheetModel.class, inputStream, fileName);
        }
        if (excelData != null) {
            List<Map<String, Object>> dataList = (List<Map<String, Object>>) excelData.get("listMap");
            System.out.println("dataList===" + dataList.size());
            bigDataTestService.storageMillionsData(dataList);
            return Result.success();
        } else {
            return Result.error(CodeMsg.DATA_IS_NULL);
        }
    }
}
