package com.digiduty.qurancounter.service;

import com.digiduty.qurancounter.model.Counts;
import com.digiduty.qurancounter.model.MaxCounts;
import com.digiduty.qurancounter.model.Progress;
import com.digiduty.qurancounter.model.SurahsEnum;
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


    public String updateCounts(SurahsEnum surahs, int decreaseValue) throws ExecutionException, InterruptedException {
        Counts currentCountValues = getAllCounts();
        if (surahs.equals(SurahsEnum.CEVSEN)) {
            currentCountValues.setCevsen(currentCountValues.getCevsen() - decreaseValue);
        } else if (surahs.equals(SurahsEnum.FETIH)) {
            currentCountValues.setFetih(currentCountValues.getFetih() - decreaseValue);
        } else {
            currentCountValues.setYasin(currentCountValues.getYasin() - decreaseValue);
        }

        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = firestore
                .collection("surah-counter")
                .document("counts")
                .set(currentCountValues);
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
