package com.xusy.springbt.exception;


import com.xusy.springbt.enums.ResultEnum;

/**
 * Created by 廖师兄
 * 2017-01-21 14:05
 */
public class ApplicationException extends RuntimeException{

    private Integer code;

    public ApplicationException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
