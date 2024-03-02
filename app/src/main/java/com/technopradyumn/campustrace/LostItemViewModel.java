package com.technopradyumn.campustrace;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.technopradyumn.campustrace.data.LostItem;
import java.util.ArrayList;
import java.util.List;

public class LostItemViewModel extends ViewModel {

    private MutableLiveData<List<LostItem>> lostItemsLiveData;

    public LiveData<List<LostItem>> getLostItems() {
        if (lostItemsLiveData == null) {
            lostItemsLiveData = new MutableLiveData<>();
            loadLostItemsFromFirestore();
        }
        return lostItemsLiveData;
    }

    private void loadLostItemsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("lostItems")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<LostItem> lostItems = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        LostItem lostItem = document.toObject(LostItem.class);
                        lostItems.add(lostItem);
                    }
                    lostItemsLiveData.setValue(lostItems);
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                });
    }
}