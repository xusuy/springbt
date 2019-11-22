package com.xusy.springbt.service;

import freemarker.template.TemplateException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author xsy
 * @create 2019-11-22 13:22
 * @desc
 **/
public interface ExtensionService {
    void downLoadInsuranceMemberInfoWord(Map<String, Object> insuranceMap, HttpServletResponse response, HttpServletRequest request) throws IOException, TemplateException;
}
