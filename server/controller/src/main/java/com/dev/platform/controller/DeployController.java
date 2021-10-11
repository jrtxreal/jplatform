package com.dev.platform.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : This class is used to check Jenkins deployment results
 * @date : 2021/7/7 17:41
 */
@RestController
public class DeployController extends BasicController{
    @GetMapping(value = "/g/v")
    public Object getV(){
        return ok().setResult("张亚楠,修改5555");
    }
}
