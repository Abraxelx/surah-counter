package com.digiduty.qurancounter.service;

import com.digiduty.qurancounter.model.*;

import java.util.concurrent.ExecutionException;

public interface CountService {
     Counts getAllCounts() throws ExecutionException, InterruptedException;
     MaxCounts getAllMaxCounts() throws ExecutionException, InterruptedException;
     ReverseCounter getAllReverseCounts() throws ExecutionException, InterruptedException;
     DailyHadis getDailyHadis() throws ExecutionException, InterruptedException;
     String updateCounts(SurahsEnum surahsEnum, int decreaseValue) throws ExecutionException, InterruptedException;
    Progress progressBarValueCalculator() throws ExecutionException, InterruptedException;
    <T> T fillModel(String collectionName, Class<T> clazz) throws ExecutionException, InterruptedException;
}
