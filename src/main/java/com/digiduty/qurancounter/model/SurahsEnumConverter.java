package com.digiduty.qurancounter.model;

import com.digiduty.qurancounter.exception.EnumConverterException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class SurahsEnumConverter implements Converter<String, SurahsEnum> {


    @Override
    public SurahsEnum convert(@NonNull String source) {
        try {
            return SurahsEnum.of(source);
        } catch (Exception e) {
            throw new EnumConverterException("Converter Error :" +e);
        }
    }
}
