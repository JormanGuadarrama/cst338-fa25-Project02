package com.example.project02.ui.pantry;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project02.Database.PantryItemWithFood;
import com.example.project02.R;
import java.util.List;

public class PantryListAdapter extends RecyclerView.Adapter<PantryListAdapter.PantryViewHolder> {

    public interface OnItemRemoveListener {
        void onItemRemove(PantryItemWithFood item);
    }

    private List<PantryItemWithFood> mPantryList;
    private final OnItemRemoveListener mListener;

    public PantryListAdapter(List<PantryItemWithFood> pantryList, OnItemRemoveListener listener) {
        mPantryList = pantryList;
        mListener = listener;
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pantry_list_item, parent, false);
        return new PantryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        PantryItemWithFood currentItem = mPantryList.get(position);
        holder.bind(currentItem, mListener);
    }

    @Override
    public int getItemCount() {
        return mPantryList == null ? 0 : mPantryList.size();
    }

    public void setPantryList(List<PantryItemWithFood> items) {
        mPantryList = items;
        notifyDataSetChanged();
    }

    static class PantryViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView familyTextView;
        private final TextView quantityTextView;
        private final Button removeButton;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.pantry_food_name_textview);
            familyTextView = itemView.findViewById(R.id.pantry_food_family_textview);
            quantityTextView = itemView.findViewById(R.id.pantry_food_quantity_textview);
            removeButton = itemView.findViewById(R.id.remove_food_button);
        }

        public void bind(final PantryItemWithFood item, final OnItemRemoveListener listener) {
            if (item.food != null) {
                nameTextView.setText(item.food.getName());
                familyTextView.setText(item.food.getFamily());
            }
            quantityTextView.setText("Quantity: " + item.pantryItem.getQuantity());
            removeButton.setOnClickListener(v -> listener.onItemRemove(item));
        }
    }
}
