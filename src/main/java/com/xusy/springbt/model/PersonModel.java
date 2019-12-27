package com.xusy.springbt.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName("xml")
@Accessors(chain = true)
public class PersonModel {
    @JsonProperty("name")
    private String name;
    @JsonProperty("nickname")
    //@JacksonXmlText
    private String nickname;
    @JsonProperty("age")
    private int age;
    @JsonProperty("identityCode")
    @JacksonXmlCData
    private String identityCode;
    @JsonProperty("birthday")
    //@JacksonXmlProperty(isAttribute = true)
    @JsonFormat(pattern = "yyyy-MM-DD")
    private Date birthday;
}
