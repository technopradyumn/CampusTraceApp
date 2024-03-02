package com.technopradyumn.campustrace;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.RemoteViews;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.technopradyumn.campustrace.data.LostItem;

public class NewAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        // Create RemoteViews
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Query to get the latest LostItem document from Firestore
        db.collection("lostItems")
                .orderBy("time", Query.Direction.DESCENDING)  // Assuming timestamp is the field representing the time of addition
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        LostItem item = document.toObject(LostItem.class);

                        // Set text and image views
                        views.setTextViewText(R.id.textView, item.getName());

                        // Load image using Glide
                        Glide.with(context)
                                .asBitmap()
                                .load(item.getImage())
                                .into(new CustomTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        // Set the bitmap resource into the ImageView using RemoteViews
                                        views.setImageViewBitmap(R.id.imageView, resource);

                                        // Update the widget
                                        appWidgetManager.updateAppWidget(appWidgetId, views);
                                    }

                                    @Override
                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                        // Handle the case where the image load is cleared
                                    }
                                });

                        // Update the widget
                        appWidgetManager.updateAppWidget(appWidgetId, views);
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure to fetch data from Firestore
                });


        appWidgetManager.updateAppWidget(appWidgetId, views);


    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}
