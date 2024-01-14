package com.digiduty.qurancounter.service.impl;

import com.digiduty.qurancounter.constants.Constants;
import com.digiduty.qurancounter.model.*;
import com.digiduty.qurancounter.service.CountService;
import com.digiduty.qurancounter.service.FirebaseInitializer;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class CountServiceImpl implements CountService {

    private static volatile byte[] cachedContent = null;

    @Autowired
    public CountServiceImpl(FirebaseInitializer initializer) {
    }

    @PostConstruct
    private void init() throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream("/static/xml/sitemap.xml")) {
            assert inputStream != null;
            cachedContent = IOUtils.toByteArray(inputStream);
        }
    }


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
        Firestore firestore = FirestoreClient.getFirestore();

        ApiFuture<String> transactionResult = firestore.runTransaction(transaction -> {
            DocumentReference countsRef = firestore.collection(Constants.SURAH_COUNTER_DB).document(Constants.COUNTS_TABLE_NAME);
            DocumentReference reverseCounterRef = firestore.collection(Constants.REVERSE_COUNTER_DB).document(Constants.REVERSE_COUNTER_DB);

            Counts currentCountValues = transaction.get(countsRef).get().toObject(Counts.class);
            ReverseCounter reverseCounter = transaction.get(reverseCounterRef).get().toObject(ReverseCounter.class);

            int newValue;
            switch (surahsEnum) {
                case CEVSEN:
                    assert currentCountValues != null;
                    newValue = currentCountValues.getCevsen() - decreaseValue;
                    if (newValue < 0) {
                        throw new IllegalArgumentException(Constants.CEVSEN_OVERFLOW_MESSAGE + currentCountValues);
                    }
                    currentCountValues.setCevsen(newValue);
                    assert reverseCounter != null;
                    reverseCounter.setReverseCevsen(reverseCounter.getReverseCevsen() + decreaseValue);
                    break;
                case FETIH:
                    assert currentCountValues != null;
                    newValue = currentCountValues.getFetih() - decreaseValue;
                    if (newValue < 0) {
                        throw new IllegalArgumentException(Constants.FETIH_OVERFLOW_MESSAGE + currentCountValues);
                    }
                    currentCountValues.setFetih(newValue);
                    assert reverseCounter != null;
                    reverseCounter.setReverseFetih(reverseCounter.getReverseFetih() + decreaseValue);
                    break;
                case YASIN:
                    assert currentCountValues != null;
                    newValue = currentCountValues.getYasin() - decreaseValue;
                    if (newValue < 0) {
                        throw new IllegalArgumentException(Constants.YASIN_OVERFLOW_MESSAGE + currentCountValues);
                    }
                    currentCountValues.setYasin(newValue);
                    assert reverseCounter != null;
                    reverseCounter.setReverseYasin(reverseCounter.getReverseYasin() + decreaseValue);
                    break;
            }

            transaction.set(countsRef, currentCountValues);
            transaction.set(reverseCounterRef, reverseCounter);
            return "Updated Successfully.";
        });

        return transactionResult.get();
    }

    public Progress progressBarValueCalculator() throws ExecutionException, InterruptedException {
        MaxCounts maxCounts = getAllMaxCounts();
        ReverseCounter currentCounts = getAllReverseCounts();
        if (maxCounts == null || currentCounts == null) {
            return new Progress();
        }

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

    @Override
    public byte[] getSiteMap() {
        return cachedContent;
    }
}
