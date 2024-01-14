package com.digiduty.qurancounter.config;

import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ApplicationContextHolder implements ApplicationContextAware {
    private ApplicationContext context;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
