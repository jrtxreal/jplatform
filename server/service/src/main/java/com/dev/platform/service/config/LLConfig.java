package com.dev.platform.service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class LLConfig implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    Environment env;
    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        try{
            String license = env.getProperty("com.dev.platform.registry.license");
            LCmpt lCmpt = new LCmpt();
            lCmpt.analyse(license);
            lCmpt.checkHardware();
            lCmpt.checkDate();
        }catch (Exception e){
            Throwable t = e;
            while (t.getCause()!=null){
                t = t.getCause();
            }
            System.out.println(t.getMessage());
            System.exit(1);
        }
    }
}
