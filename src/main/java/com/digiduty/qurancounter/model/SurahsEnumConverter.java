package com.digiduty.qurancounter.model;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SurahsEnumConverter implements Converter<String, SurahsEnum> {


    @Override
    public SurahsEnum convert(String source) {
        try {
            return SurahsEnum.of(source);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
