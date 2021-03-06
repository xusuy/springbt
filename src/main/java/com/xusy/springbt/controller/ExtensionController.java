package com.xusy.springbt.controller;

import com.xusy.springbt.service.ExtensionService;
import freemarker.template.TemplateException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xsy
 * @create 2019-11-14 16:53
 * @desc 扩展功能
 **/
@RestController
@RequestMapping("/api/qxy/extens")
public class ExtensionController {
    private ExtensionService extensionService;

    public ExtensionController(ExtensionService extensionService) {
        this.extensionService = extensionService;
    }

    /**
     * 下载保险合同信息word(审核建议书.doc/立案信息采集表.doc)
     *
     * @param //insuranceMap 立案审核建议书/理赔信息采集表表单数据
     * @param response       HttpServletResponse
     * @param request        HttpServletRequest
     */
    @GetMapping("downLoadInsuranceMemberInfoWord")
    public void downLoadInsuranceMemberInfoWord(HttpServletResponse response,
                                                HttpServletRequest request) throws IOException, TemplateException {
        //便于测试,创建测试map
        Map<String, Object> insuranceMap = new HashMap<>();
        insuranceMap.put("infoType", "2");
        extensionService.downLoadInsuranceMemberInfoWord(insuranceMap, response, request);
    }
}
