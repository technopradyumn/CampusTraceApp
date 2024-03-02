package com.technopradyumn.campustrace.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.technopradyumn.campustrace.R;
import com.technopradyumn.campustrace.data.LostItem;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> implements Filterable {

    private List<LostItem> itemList;
    private List<LostItem> filteredItemList;
    private Context context;
    private OnItemClickListener listener;

    // Constructor
    public ItemAdapter(Context context, List<LostItem> itemList) {
        this.context = context;
        this.itemList = itemList;
        this.filteredItemList = new ArrayList<>(itemList); // Initialize filteredItemList
    }

    // Method to set click listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // View holder class
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textName;
        public TextView textCategory;
        public TextView textTime;
        public TextView textLocation;
        public TextView textStatus;
        public TextView description;

        public ItemViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textName = itemView.findViewById(R.id.textName);
            textCategory = itemView.findViewById(R.id.textCategory);
            textTime = itemView.findViewById(R.id.textTime);
            textLocation = itemView.findViewById(R.id.textLocation);
            textStatus = itemView.findViewById(R.id.textStatus);
            description = itemView.findViewById(R.id.description);

            // Set click listener for item
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ItemViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        LostItem currentItem = itemList.get(position);

        // Load image using Glide
        Glide.with(context)
                .load(currentItem.getImage())
                .placeholder(R.drawable.baseline_image_24)
                .into(holder.imageView);

        holder.textName.setText(currentItem.getName());
        holder.textCategory.setText(currentItem.getCategory());
        holder.textTime.setText(currentItem.getTime());
        holder.textLocation.setText(currentItem.getLocationLastSeen());
        holder.textStatus.setText(currentItem.getStatus());
        holder.description.setText(currentItem.getAdditionalDetail());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    private Filter itemFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<LostItem> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                // If the search text is empty, return the original list
                filteredList.addAll(filteredItemList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (LostItem item : itemList) {
                    if (item.getName().toLowerCase().contains(filterPattern) ||
                            item.getCategory().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // Update itemList with filtered data
            itemList.clear();
            itemList.addAll((List<LostItem>) results.values);
            notifyDataSetChanged();
        }
    };
}