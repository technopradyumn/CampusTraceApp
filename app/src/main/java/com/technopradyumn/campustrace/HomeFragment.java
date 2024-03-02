package com.technopradyumn.campustrace;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.technopradyumn.campustrace.adapter.ItemAdapter;
import com.technopradyumn.campustrace.data.LostItem;
import java.util.ArrayList;
import com.technopradyumn.campustrace.adapter.ItemAdapter.OnItemClickListener;
import java.util.List;

public class HomeFragment extends Fragment implements ItemAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private List<LostItem> itemList;
    private FirebaseFirestore db;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        itemList = new ArrayList<>();
        adapter = new ItemAdapter(getContext(), itemList);
        adapter.setOnItemClickListener(this);
        fetchLostItemsFromFirestore();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void fetchLostItemsFromFirestore() {
        db.collection("lostItems")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        LostItem lostItem = documentSnapshot.toObject(LostItem.class);
                        itemList.add(lostItem);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to fetch lost items", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onItemClick(int position) {
        LostItem selectedItem = itemList.get(position);
        String itemId = String.valueOf(selectedItem.getItemId());

        // Start the new activity and pass the itemId as an extra
        Intent intent = new Intent(getContext(), ItemDetailActivity.class);
        intent.putExtra("itemId", itemId);
        startActivity(intent);
    }



}

