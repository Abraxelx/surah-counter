package com.digiduty.qurancounter.service;

import com.digiduty.qurancounter.model.Counts;
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
        ApiFuture<WriteResult> collectionsApiFuture = firestore.collection("surah-counter").document("counts").set(currentCountValues);
        return "Updated Successfully. " + collectionsApiFuture.get().getUpdateTime();
    }

}
