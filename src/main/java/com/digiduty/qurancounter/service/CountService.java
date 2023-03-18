package com.digiduty.qurancounter.service;

import com.digiduty.qurancounter.constants.Constants;
import com.digiduty.qurancounter.model.*;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class CountService {


    public Counts getAllCounts() throws ExecutionException, InterruptedException {
        return fillModel(Constants.SURAH_COUNTER_DB, Counts.class);
    }

    public MaxCounts getAllMaxCounts() throws ExecutionException, InterruptedException {
        return fillModel(Constants.MAX_COUNTS_DB, MaxCounts.class);
    }

    public ReverseCounter getAllReverseCounts() throws ExecutionException, InterruptedException {
        return fillModel(Constants.REVERSE_COUNTER_DB, ReverseCounter.class);
    }

    public DailyHadis getDailyHadis() throws ExecutionException, InterruptedException {
        return fillModel(Constants.DAILY_HADIS_DB, DailyHadis.class);
    }


    public String updateCounts(SurahsEnum surahsEnum, int decreaseValue) throws ExecutionException, InterruptedException {
        Counts currentCountValues = getAllCounts();
        ReverseCounter reverseCounter = getAllReverseCounts();
        if (surahsEnum.equals(SurahsEnum.CEVSEN)) {
            int minus = currentCountValues.getCevsen() - decreaseValue;
            if (minus < 0) {
                return Constants.CEVSEN_OVERFLOW_MESSAGE + currentCountValues;
            }
            currentCountValues.setCevsen(minus);
            reverseCounter.setReverseCevsen(reverseCounter.getReverseCevsen() + decreaseValue);
        } else if (surahsEnum.equals(SurahsEnum.FETIH)) {
            int minus = currentCountValues.getFetih() - decreaseValue;
            if (minus < 0) {
                return Constants.FETIH_OVERFLOW_MESSAGE + currentCountValues;
            }
            currentCountValues.setFetih(currentCountValues.getFetih() - decreaseValue);
            reverseCounter.setReverseFetih(reverseCounter.getReverseFetih() + decreaseValue);
        } else {
            int minus = currentCountValues.getYasin() - decreaseValue;
            if (minus < 0) {
                return Constants.YASIN_OVERFLOW_MESSAGE + currentCountValues;
            }
            currentCountValues.setYasin(currentCountValues.getYasin() - decreaseValue);
            reverseCounter.setReverseYasin(reverseCounter.getReverseYasin() + decreaseValue);
        }

        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = firestore
                .collection(Constants.SURAH_COUNTER_DB)
                .document(Constants.COUNTS_TABLE_NAME)
                .set(currentCountValues);
        firestore.collection(Constants.REVERSE_COUNTER_DB)
                .document(Constants.REVERSE_COUNTER_DB)
                .set(reverseCounter);
        return "Updated Successfully. " + collectionsApiFuture.get().getUpdateTime();
    }

    public Progress progressBarValueCalculator() throws ExecutionException, InterruptedException {
        MaxCounts maxCounts = getAllMaxCounts();
        ReverseCounter currentCounts = getAllReverseCounts();

        Progress progress = new Progress();

        progress.setYasinProgress(((float) currentCounts.getReverseYasin() / (float) maxCounts.getYasinMaxCount()) * 100f);
        progress.setFetihProgress(((float) currentCounts.getReverseFetih() / (float) maxCounts.getFetihMaxCount()) * 100f);
        progress.setCevsenProgress(((float) currentCounts.getReverseCevsen() / (float) maxCounts.getCevsenMaxCount()) * 100f);
        return progress;
    }

    public <T> T fillModel(String collectionName, Class<T> clazz) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> query = dbFirestore.collection(collectionName).get();
        QuerySnapshot querySnapshot = query.get();
        List<QueryDocumentSnapshot> documentSnapshotList = querySnapshot.getDocuments();
        for (DocumentSnapshot elements : documentSnapshotList) {
            if (elements.exists()) {
                return elements.toObject(clazz);
            }
        }
        return null;
    }

}
