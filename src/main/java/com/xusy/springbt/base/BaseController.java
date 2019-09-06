package com.xusy.springbt.base;

import com.xusy.springbt.result.CodeMsg;
import com.xusy.springbt.result.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import java.net.URI;

/**
 * @author xsy
 * @create 2019-09-06 10:44
 * @desc
 **/
public class BaseController {

    protected ResponseEntity<?> ok() {
        return ResponseEntity.ok().build();
    }

    protected <T> ResponseEntity<T> ok(T body) {
        return ResponseEntity.ok().body(body);
    }

    protected ResponseEntity ok(URI location) {
        return ResponseEntity.created(location).build();
    }

    protected ResponseEntity<?> noContent() {
        return ResponseEntity.noContent().build();
    }

    protected ResponseEntity<Result> badRequest(CodeMsg codeMsg) {
        return ResponseEntity.badRequest().body(Result.error(codeMsg));
    }

    protected ResponseEntity<?> notFound() {
        return ResponseEntity.notFound().build();
    }
}
