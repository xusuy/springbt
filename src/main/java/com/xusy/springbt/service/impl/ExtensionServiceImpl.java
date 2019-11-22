package com.xusy.springbt.service.impl;

import com.xusy.springbt.service.ExtensionService;
import com.xusy.springbt.util.FileUtil;
import com.xusy.springbt.util.enums.EnumInsuranceMemberArchive;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import lombok.Cleanup;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xsy
 * @create 2019-11-15 15:22
 * @desc
 **/
@Service
public class ExtensionServiceImpl implements ExtensionService {
    @Override
    public void downLoadInsuranceMemberInfoWord(Map<String, Object> insuranceMap, HttpServletResponse response,
                                                HttpServletRequest request) throws IOException, TemplateException {
        Map<String, Object> extensionMap = new HashMap<>();
        String fileName = "";
        String ftlName = "";
        Object infoTypeObj = insuranceMap.get("infoType");
        //理赔信息采集表
        if ("1".equals(infoTypeObj)) {
            buildCalimInfoGather(extensionMap, insuranceMap);
            fileName = "立案信息采集表.doc";
            ftlName = "reportInfoGather.ftl";
        } else if ("2".equals(infoTypeObj)) {
            //立案审核建议书
            buildRegisterAuditProposal(extensionMap, insuranceMap);
            fileName = "审核建议书.doc";
            ftlName = "auditProposal.ftl";
        }

        Configuration configuration = new Configuration(new Version("2.3.0"));
        configuration.setDefaultEncoding("utf-8");
        configuration.setClassForTemplateLoading(this.getClass(), "/templates");
        Template template;
        template = configuration.getTemplate(ftlName, "utf-8");
        Resource classPathResource = new ClassPathResource("templates/tem/" + fileName);
        @Cleanup InputStream inputStream = classPathResource.getInputStream();
        File outFile = classPathResource.getFile();
//        File outFile = ResourceUtils.getFile("classpath:templates/tem/" + fileName);
        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf-8"));
        template.process(extensionMap, writer);
        //下载文件
        FileUtil.downLoadFile(fileName, inputStream, response);
        writer.close();
    }

    //构建立案审核建议书
    private void buildRegisterAuditProposal(Map<String, Object> extensionMap, Map<String, Object> insuranceMap) {
        //被保险人信息-姓名
        addMap(extensionMap, insuranceMap.get(EnumInsuranceMemberArchive.INSUREDNAME.getId()), "insuredName");
        //被保险人信息-性别
        addMap(extensionMap, insuranceMap.get(EnumInsuranceMemberArchive.INSUREDSEX.getId()), "insuredSex");
        //被保险人-投保年龄
        addMap(extensionMap, insuranceMap.get(EnumInsuranceMemberArchive.ISSUEAGE.getId()), "issueAge");
        //被保险人信息-出生日期
        addMap(extensionMap, insuranceMap.get(EnumInsuranceMemberArchive.INSUREDBIRTHDATE.getId()), "insuredBirthdate");
        //被保险人信息-证件号码
        addMap(extensionMap, insuranceMap.get(EnumInsuranceMemberArchive.INSUREDCARD.getId()), "insuredCard");
        //被保险人信息-联系电话
        addMap(extensionMap, insuranceMap.get(EnumInsuranceMemberArchive.INSUREDCONTACTNUMBER.getId()), "insuredContactNumber");
        //审核意见
        String auditreportidea = insuranceMap.get(EnumInsuranceMemberArchive.AUDITREPORTIDEA.getId()) + "";
        String ch1 = "00A3";
        String ch2 = "00A3";
        String ch3 = "00A3";
        String ch4 = "00A3";
        String ch5 = "00A3";
        if ("予以立案".equals(auditreportidea)) {
            ch1 = "0052";
        } else {
            ch2 = "0052";
        }
        Object policyNoState = insuranceMap.get(EnumInsuranceMemberArchive.AUDITNOAPPROVALREPORTREASON.getId());
        if ("保单等于处理期".equals(policyNoState)) {
            ch3 = "0052";
        } else if ("保单失效".equals(policyNoState)) {
            ch4 = "0052";
        } else if ("其他".equals(policyNoState)) {
            ch5 = "0052";
        }
        //立案审核意见-予以立案
        addMap(extensionMap, ch1, "ch1");
        //立案审核意见-预估立案金额
        addMap(extensionMap, insuranceMap.get(EnumInsuranceMemberArchive.AUDITPROSPECTREPORTAMOUNT.getId()), "auditProspectReportAmount");
        //立案审核意见-不予立案
        addMap(extensionMap, ch2, "ch2");
        //立案审核意见-不予立案理由
        addMap(extensionMap, ch3, "ch3");
        //立案审核意见-不予立案理由
        addMap(extensionMap, ch4, "ch4");
        //立案审核意见-不予立案理由
        addMap(extensionMap, ch5, "ch5");
        //立案审核意见-不予立案其他备注
        addMap(extensionMap, insuranceMap.get(EnumInsuranceMemberArchive.OTHERREMARK.getId()), "otherRemark");
        //初审人
        addMap(extensionMap, insuranceMap.get(EnumInsuranceMemberArchive.TRIALNAME.getId()), "trialName");
        //复审人
        addMap(extensionMap, insuranceMap.get(EnumInsuranceMemberArchive.REVIEWNAME.getId()), "reviewName");
        //电话
        addMap(extensionMap, insuranceMap.get(EnumInsuranceMemberArchive.PHONENO.getId()), "phoneNo");
        //邮箱
        addMap(extensionMap, insuranceMap.get(EnumInsuranceMemberArchive.EMAIL.getId()), "email");
    }

    //构建理赔信息采集
    private void buildCalimInfoGather(Map<String, Object> extensionMap, Map<String, Object> insuranceMap) {
        //保单号
        addMap(extensionMap, insuranceMap.get(EnumInsuranceMemberArchive.POLICYNO.getId()), "policyNo");
        //中国人寿客户号
        addMap(extensionMap, insuranceMap.get(EnumInsuranceMemberArchive.LFCCUSTOMNO.getId()), "lfcCustomNo");
        //起保日期
        addMap(extensionMap, insuranceMap.get(EnumInsuranceMemberArchive.BEGININSURANCEDATE.getId()), "beginInsuranceDate");
        //终保日期
        addMap(extensionMap, insuranceMap.get(EnumInsuranceMemberArchive.ENDINSURANCEDATE.getId()), "endInsuranceDate");
        //产品名称
        addMap(extensionMap, insuranceMap.get(EnumInsuranceMemberArchive.PRODUCTNAME.getId()), "productName");
        //权益是否升级
        addMap(extensionMap, insuranceMap.get(EnumInsuranceMemberArchive.EQUITWHETHERUPGRADES.getId()), "equitWhetherUpgrades");
        //业务归属保险公司
        addMap(extensionMap, insuranceMap.get(EnumInsuranceMemberArchive.OWNEDBUSINESSCOMPANY.getId()), "ownedBusCompany");
        //业务归属分公司
        addMap(extensionMap, insuranceMap.get(EnumInsuranceMemberArchive.OWNEDBUSINESSBRANCH.getId()), "ownedBusBranch");
        //被保险人信息-姓名
        addMap(extensionMap, insuranceMap.get(EnumInsuranceMemberArchive.INSUREDNAME.getId()), "insuredName");
        //被保险人信息-性别
        addMap(extensionMap, insuranceMap.get(EnumInsuranceMemberArchive.INSUREDSEX.getId()), "insuredSex");
        //被保险人信息-投保年龄
        addMap(extensionMap, insuranceMap.get(EnumInsuranceMemberArchive.ISSUEAGE.getId()), "issueAge");
        //被保险人信息-出生日期
        addMap(extensionMap, insuranceMap.get(EnumInsuranceMemberArchive.INSUREDBIRTHDATE.getId()), "insuredBirthdate");
        //被保险人信息-证件号码
        addMap(extensionMap, insuranceMap.get(EnumInsuranceMemberArchive.INSUREDCARD.getId()), "insuredCard");
        //被保险人信息-省份
        addMap(extensionMap, insuranceMap.get(EnumInsuranceMemberArchive.PROVINCE.getId()), "province");
        //被保险人信息-总保费
        addMap(extensionMap, insuranceMap.get(EnumInsuranceMemberArchive.INSUREDTOTALPREMIUM.getId()), "insuredTotalPremium");
        //被保险人信息-疾病描述
        addMap(extensionMap, insuranceMap.get(EnumInsuranceMemberArchive.INSUREDDISEASEDESCRIPTION.getId()), "insuredDiseaseDescription");
        //被保险人信息-疾病确诊日期
        addMap(extensionMap, insuranceMap.get(EnumInsuranceMemberArchive.INSUREDDISEASEDIAGNOSEDDATE.getId()), "insuredDisDiadDate");
        //被保险人信息-报案日期
        addMap(extensionMap, insuranceMap.get(EnumInsuranceMemberArchive.INSUREDREPORTDATE.getId()), "insuredReportDate");
        //被保险人信息-结果
        addMap(extensionMap, insuranceMap.get(EnumInsuranceMemberArchive.INSUREDRESULT.getId()), "insuredResult");
        //联系人信息-姓名
        addMap(extensionMap, insuranceMap.get(EnumInsuranceMemberArchive.CONTACTNAME.getId()), "contactName");
        //联系人信息-手机
        addMap(extensionMap, insuranceMap.get(EnumInsuranceMemberArchive.CONTACTPHONENO.getId()), "contactPhoneNo");
        //联系人信息-邮箱地址
        addMap(extensionMap, insuranceMap.get(EnumInsuranceMemberArchive.CONTACTEMAIL.getId()), "contactEmail");
        //联系人信息-与被保险人关系
        addMap(extensionMap, insuranceMap.get(EnumInsuranceMemberArchive.CONTACTRELATIONSHIP.getId()), "contactRelationship");
        //投保信息-姓名
        addMap(extensionMap, insuranceMap.get(EnumInsuranceMemberArchive.INSURENAME.getId()), "insureName");
        //投保信息-年龄
        addMap(extensionMap, insuranceMap.get(EnumInsuranceMemberArchive.INSUREAGE.getId()), "insureAge");
        //投保信息-与被保险人关系
        addMap(extensionMap, insuranceMap.get(EnumInsuranceMemberArchive.INSURERELATIONSHIP.getId()), "insureRelationship");
        //投保信息-千信雅客户号
        addMap(extensionMap, insuranceMap.get(EnumInsuranceMemberArchive.INSUREQXYCUSTOMNO.getId()), "insureQxyCustomNo");
    }

    private void addMap(Map<String, Object> extensionMap, Object value, String key) {
        if (StringUtils.isEmpty(value)) {
            extensionMap.put(key, "");
        } else {
            extensionMap.put(key, value);
        }
    }
}
