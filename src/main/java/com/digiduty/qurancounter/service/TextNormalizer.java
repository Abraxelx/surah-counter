package com.digiduty.qurancounter.service;

import java.text.Normalizer;

public class TextNormalizer {

    public String normalizeTextForForm(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFC);
    }
}
