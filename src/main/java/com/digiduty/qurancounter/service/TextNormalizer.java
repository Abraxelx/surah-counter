package com.digiduty.qurancounter.service;

import java.text.Normalizer;

public class TextNormalizer {

    public static String normalizeTextForForm(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFC);
    }
}
