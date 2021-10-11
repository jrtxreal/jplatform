package com.dev.platform.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import javax.servlet.http.HttpServletResponse;

import com.dev.platform.service.config.ExceptionCmpt;
import com.dev.platform.service.config.ResultCmpt;

/**
 * @author : Ken
 * @version : 1.0
 * @description : Global exception handler
 * @date : 2021/5/31 17:45
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;//单文件最大值
    @Value("${spring.servlet.multipart.max-request-size}")
    private String maxRequestSize;//单次请求最大值

    @Autowired
    ExceptionCmpt exceptioncmpt;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    public Object allExceptionHandler(Exception e) throws Exception {
       return exceptioncmpt.handle(e);
    }

    /**
     * 处理文件上传大小超限制
     * @param exception
     * @return
     */
    @ExceptionHandler(value = MultipartException.class)
    public Object fileUploadExceptionHandler(MultipartException exception, HttpServletResponse response) {
        String msg;
        Throwable e = exception;
        while (e.getCause() != null){
            e = e.getCause();
        }
        if (e instanceof org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException) {
            msg="上传文件过大[单个文件大小不得超过" + maxFileSize + "]";
        }else if(e instanceof org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException){
            msg="上传文件过大[总上传大小不得超过" + maxRequestSize + "]";
        }else {
            msg="文件上传失败[服务器异常]";
        }
        System.out.println(exception.toString());
        ResultCmpt resultCmpt = new ResultCmpt();
        return  resultCmpt.setCode(3).setMsg(msg);
    }
}
