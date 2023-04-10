package com.digiduty.qurancounter.model;

import com.digiduty.qurancounter.exception.UnknownSurahException;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SurahsEnum {
    FETIH("FETIH"),
    YASIN("YASIN"),
    CEVSEN("CEVSEN");

    private final String surahNames;
    SurahsEnum(String surahNames) {
        this.surahNames = surahNames;
    }

    @JsonValue
    public String getSurahNames() {
        return surahNames;
    }

    public static SurahsEnum of(String value) throws UnknownSurahException {
        if (value == null) {
            return null;
        }

        for (SurahsEnum item : SurahsEnum.values()) {
            if (value.equals(item.getSurahNames())) {
                return item;
            }
        }
        throw new UnknownSurahException("Surah Not Found :" + value);
    }
}
