package cn.vic.manager.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一错误处理（不仅仅用来进行错误处理）
 * controller 中出现的异常会首先被 ControllerAdvice 捕获并返回，故而全局异常捕获不到这个异常
 */
@ControllerAdvice(basePackages = {"cn.vic.manager.controller"})
public class ErrorControllerAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseBody // 让其以 json 的方式返回
    public ResponseEntity handleException(Exception e) {
        Map<String, Object> attrs = new HashMap<>();
        String errorCode = e.getMessage();
        ErrorEnum errorEnum = ErrorEnum.getByCode(errorCode);
        attrs.put("message", errorEnum.getMessage());
        attrs.put("code", errorEnum.getCode());
        attrs.put("canRetry", errorEnum.isCanRetry());
        attrs.put("type", "advice"); // 对两种异常处理进行区别
        return new ResponseEntity(attrs, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
