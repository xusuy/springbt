package com.xusy.springbt.handle;

import com.xusy.springbt.domain.Result;
import com.xusy.springbt.enums.ResultEnum;
import com.xusy.springbt.exception.ApplicationException;
import com.xusy.springbt.util.ResultUtil;
import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by 廖师兄
 * 2017-01-21 13:59
 * 全局异常处理1
 */
//@ControllerAdvice
public class ExceptionHandle {

    private final static Logger logger = Logger.getLogger(ExceptionHandle.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result handle(Exception e) {
        if (e instanceof ApplicationException) {
            ApplicationException applicationException = (ApplicationException) e;
            return ResultUtil.error(applicationException.getCode(), applicationException.getMessage());
        } else if (e instanceof BindException) {
            BindException bindException = (BindException) e;
            BindingResult bindingResult = bindException.getBindingResult();
            if (bindingResult.hasErrors()) {
                return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
            }
        }
        logger.error("【系统异常】:", e);
        return ResultUtil.error(-1, ResultEnum.UNKONW_ERROR.getMsg());
    }
}
