package com.xusy.springbt.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.xusy.springbt.entity.User;
import com.xusy.springbt.model.UserModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @author xsy
 * @create 2019-12-27 17:58
 * @desc 配置
 **/
@Configuration
public class BtConfig {

    //model直接转换
    @Bean
    public ModelMapper modelMapperBean() {
        ModelMapper modelMapper = new ModelMapper();

        configure(modelMapper);

        return modelMapper;
    }

    private void configure(ModelMapper modelMapper) {
        modelMapper.createTypeMap(UserModel.class, User.class)
                .addMapping(UserModel::getAccount, User::setName);

    }

    /**
     * 为了使post方法，String参数使用的StringHttpMessageConverter，从默认的ISO-8859-1转为utf-8编码
     * @return
     */
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> httpMessageConverterList = restTemplate.getMessageConverters();
        for (HttpMessageConverter<?> httpMessageConverter : httpMessageConverterList) {
            if (httpMessageConverter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) httpMessageConverter).setDefaultCharset(Charset.forName("utf-8"));
                break;
            }
        }
        return restTemplate;
    }

    @Bean
    @Primary
    @Qualifier("xml")
    public XmlMapper xmlMapper(Jackson2ObjectMapperBuilder builder) {
        XmlMapper mapper = builder.createXmlMapper(true)
                .build();
        //美工输出
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @Bean
    @Qualifier("json")
    public ObjectMapper jsonMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper mapper = builder.createXmlMapper(false)
                .build();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}
