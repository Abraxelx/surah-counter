package com.digiduty.qurancounter.service;

import com.digiduty.qurancounter.model.Counts;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class CountService {
    private DocumentReference documentReference;
    private ApiFuture<DocumentSnapshot> apiFuture;
    private DocumentSnapshot snapshot;


    public List<Counts> getAllCounts() throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> query = dbFirestore.collection("surah-counter").get();
        QuerySnapshot querySnapshot = query.get();
        List<QueryDocumentSnapshot> documentSnapshotList  = querySnapshot.getDocuments();
        List<Counts> countsList = new ArrayList<>();
        for (DocumentSnapshot elements : documentSnapshotList) {
            if (elements.exists()) {
                countsList.add(elements.toObject(Counts.class));
            }
        }
        return countsList;
    }


    public String updateCounts(Counts counts) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = firestore.collection("surah-counter").document("counts").set(counts);
        return "Updated Successfully. " + collectionsApiFuture.get().getUpdateTime();
    }

}
