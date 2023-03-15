package com.digiduty.qurancounter.service;

import com.digiduty.qurancounter.model.*;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class CountService {
    private DocumentReference documentReference;
    private ApiFuture<DocumentSnapshot> apiFuture;
    private DocumentSnapshot snapshot;


    public Counts getAllCounts() throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> query = dbFirestore.collection("surah-counter").get();
        QuerySnapshot querySnapshot = query.get();
        List<QueryDocumentSnapshot> documentSnapshotList = querySnapshot.getDocuments();
        for (DocumentSnapshot elements : documentSnapshotList) {
            if (elements.exists()) {
                return elements.toObject(Counts.class);
            }
        }
        return new Counts();
    }

    public MaxCounts getAllMaxCounts() throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        QuerySnapshot querySnapshot = dbFirestore.collection("max-counts").get().get();
        List<QueryDocumentSnapshot> documentSnapshotList = querySnapshot.getDocuments();
        for (DocumentSnapshot elements : documentSnapshotList) {
            if (elements.exists()) {
                return elements.toObject(MaxCounts.class);
            }
        }
        return new MaxCounts();
    }

    public ReverseCounter getAllReverseCounts() throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> query = dbFirestore.collection("reverse-counter").get();
        QuerySnapshot querySnapshot = query.get();
        List<QueryDocumentSnapshot> documentSnapshotList = querySnapshot.getDocuments();
        for (DocumentSnapshot elements : documentSnapshotList) {
            if (elements.exists()) {
                return elements.toObject(ReverseCounter.class);
            }
        }
        return new ReverseCounter();
    }


    public String updateCounts(SurahsEnum surahs, int decreaseValue) throws ExecutionException, InterruptedException {
        Counts currentCountValues = getAllCounts();
        ReverseCounter reverseCounter = getAllReverseCounts();
        if (surahs.equals(SurahsEnum.CEVSEN)) {
            int minus = currentCountValues.getCevsen() - decreaseValue;
            if(minus < 0 ) {
            return "Okuduğunuz Cevşen Kalan Cevşenden Çok Olamaz!. Okuyabileceğiniz miktar : " + currentCountValues;
            }
            currentCountValues.setCevsen(minus);
            reverseCounter.setReverseCevsen(reverseCounter.getReverseCevsen() + decreaseValue);
        } else if (surahs.equals(SurahsEnum.FETIH)) {
            int minus = currentCountValues.getFetih() - decreaseValue;
            if(minus < 0 ) {
                return "Okuduğunuz Fetih Kalan Fetih'ten Çok Olamaz!. Okuyabileceğiniz miktar : " + currentCountValues;
            }
            currentCountValues.setFetih(currentCountValues.getFetih() - decreaseValue);
            reverseCounter.setReverseFetih(reverseCounter.getReverseFetih() + decreaseValue);
        } else {
            int minus = currentCountValues.getYasin() - decreaseValue;
            if(minus < 0 ) {
                return "Okuduğunuz Yasin Kalan Yasin'ten Çok Olamaz!. Okuyabileceğiniz miktar : " + currentCountValues;
            }
            currentCountValues.setYasin(currentCountValues.getYasin() - decreaseValue);
            reverseCounter.setReverseYasin(reverseCounter.getReverseYasin() + decreaseValue);
        }

        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = firestore
                .collection("surah-counter")
                .document("counts")
                .set(currentCountValues);

        ApiFuture<WriteResult> reverseApiFuture = firestore
                .collection("reverse-counter")
                .document("reverse-counter")
                .set(reverseCounter);
        return "Updated Successfully. " + collectionsApiFuture.get().getUpdateTime();
    }

    public Progress progressBarValueCalculator() throws ExecutionException, InterruptedException {
        MaxCounts maxCounts = getAllMaxCounts();
        Counts currentCounts = getAllCounts();

        Progress progress = new Progress();

        progress.setYasinProgress((100 * currentCounts.getYasin()) / maxCounts.getYasinMaxCount());
        progress.setFetihProgress((100 * currentCounts.getFetih()) / maxCounts.getFetihMaxCount());
        progress.setCevsenProgress((100 * currentCounts.getCevsen()) / maxCounts.getCevsenMaxCount());
        return progress;
    }


}
