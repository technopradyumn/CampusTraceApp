package com.technopradyumn.campustrace;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.technopradyumn.campustrace.data.LostItem;
import com.technopradyumn.campustrace.databinding.FragmentFoundBinding;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FoundFragment extends Fragment {

    private static final int REQUEST_IMAGE_PICKER = 100;
    private FragmentFoundBinding binding;
    private String imUrl;
    private FirebaseFirestore db;
    private StorageReference storageRef;

    public FoundFragment() {
        // Required empty public constructor
    }

    public static FoundFragment newInstance() {
        return new FoundFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        // Initialize Storage
        storageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFoundBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.imageView.setOnClickListener(v -> openImagePicker());

        binding.saveBtn.setOnClickListener(v -> {
            if (imUrl != null) {
                addFoundItemToFirestore(imUrl);
                clearInputFields();
            } else {
                Toast.makeText(requireContext(), "Please upload an image first", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_PICKER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICKER && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                // Get the URI of the selected image
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    // Upload the selected image to Firebase Storage
                    Glide.with(this)
                            .load(selectedImageUri)
                            .into(binding.imageView);
                    uploadImageToFirebaseStorage(selectedImageUri);
                } else {
                    Toast.makeText(requireContext(), "Failed to retrieve image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void uploadImageToFirebaseStorage(Uri imageUri) {
        // Create a reference to the location where the image will be saved in Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/" + System.currentTimeMillis() + ".jpg");

        // Upload the image file to Firebase Storage
        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image uploaded successfully
                    Toast.makeText(requireContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();

                    // Get the download URL of the uploaded image
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        imUrl = uri.toString();
                        // Now you can use the imageUrl for further processing, such as adding it to Firestore

                    });
                })
                .addOnFailureListener(e -> {
                    // Error uploading image
                    Log.e("FoundFragment", "Error uploading image: " + e.getMessage());
                    Toast.makeText(requireContext(), "Error uploading image. Please try again.", Toast.LENGTH_SHORT).show();
                });
    }

    private void addFoundItemToFirestore(String imageUrl) {
        String itemName = binding.name.getText().toString();
        String category = binding.category.getText().toString();
        String locationLastSeen = binding.location.getText().toString();
        String contactNo = binding.contactNumber.getText().toString();
        String additionalDetail = binding.description.getText().toString();
        String time = getTime();
        String status = "Lost";

        LostItem foundItem = new LostItem("",imageUrl, itemName, category, locationLastSeen, contactNo, additionalDetail, time, status);

        db.collection("lostItems")
                .add(foundItem)
                .addOnSuccessListener(documentReference -> {
                    // Retrieve the document ID and update the item ID
                    String itemId = documentReference.getId();
                    foundItem.setItemId(itemId);

                    // Update the item with the new item ID
                    db.collection("lostItems").document(itemId)
                            .set(foundItem)
                            .addOnSuccessListener(aVoid -> {
                                Log.d("FoundFragment", "Item updated with ID: " + itemId);
                                Toast.makeText(requireContext(), "Item reported successfully!", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Log.e("FoundFragment", "Error updating item: " + e.getMessage());
                                Toast.makeText(requireContext(), "Failed to report item. Please try again.", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Log.e("FoundFragment", "Error adding document: " + e.getMessage());
                    Toast.makeText(requireContext(), "Failed to report item. Please try again.", Toast.LENGTH_SHORT).show();
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private String getTime() {
        // Get the current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Define a format for the date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Format the current date and time using the defined format
        String formattedDateTime = currentDateTime.format(formatter);

        return formattedDateTime;
    }

    private void clearInputFields() {
        binding.name.setText("");
        binding.category.setText("");
        binding.location.setText("");
        binding.contactNumber.setText("");
        binding.description.setText("");
    }

}