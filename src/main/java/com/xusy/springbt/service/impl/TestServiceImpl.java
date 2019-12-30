package com.xusy.springbt.service.impl;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.xusy.springbt.model.PersonModel;
import com.xusy.springbt.service.TestService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

/**
 * @author xsy
 * @create 2019-12-27 18:05
 * @desc
 **/
@Service
@AllArgsConstructor
public class TestServiceImpl implements TestService {
    private XmlMapper xmlMapper;

    @Override
    public void testXml() throws IOException {
        PersonModel personModel = new PersonModel()
                .setAge(18).setBirthday(new Date())
                .setIdentityCode("xml-xml").setName("小美")
                .setNickname("冷萌");
        String xmlStr = xmlMapper.writeValueAsString(personModel);
        System.out.println("bean to xml===\n" + xmlStr);
        PersonModel new_personModel = xmlMapper.readValue(xmlStr, PersonModel.class);
        System.out.println("xml to bean===\n" + new_personModel);
    }
}
