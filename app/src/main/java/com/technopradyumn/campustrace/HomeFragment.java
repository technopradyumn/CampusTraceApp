package com.technopradyumn.campustrace;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.technopradyumn.campustrace.adapter.ItemAdapter;
import com.technopradyumn.campustrace.data.LostItem;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private List<LostItem> itemList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize RecyclerView and adapter
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        itemList = new ArrayList<>();
        adapter = new ItemAdapter(getActivity(), itemList);
        recyclerView.setAdapter(adapter);

        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        // Setup SearchView
        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Perform Firestore query based on the search query
                if (newText.isEmpty()) {
                    loadAllItems(); // Load all items when search query is empty
                } else {
                    searchItems(newText);
                }
                return true;
            }
        });

        // Load all items initially
        loadAllItems();

        return view;
    }

    private void loadAllItems() {
        db.collection("lostItems")
                .orderBy("time", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    itemList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        LostItem item = document.toObject(LostItem.class);
                        itemList.add(item);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error loading items", e);
                });
    }

    private void searchItems(String query) {
        db.collection("lostItems")
                .whereGreaterThanOrEqualTo("category", query)
                .whereLessThan("category", query + "\uf8ff")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    itemList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        LostItem item = document.toObject(LostItem.class);
                        itemList.add(item);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error searching items", e);
                });
    }
}