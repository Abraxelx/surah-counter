package com.digiduty.qurancounter.service;

import java.text.Normalizer;

public class TextNormalizer {

    public String normalizeTextForForm() {
        String text = "سُبْحَانَكَ لاَعِلْمَ لَنَاۤ اِلاَّ مَاعَلَّمْتَنَاۤ اِنَّكَ اَنْتَ الْعَلِيمُ الْحَكِيمُ";
        return Normalizer.normalize(text, Normalizer.Form.NFC);
    }
}
