package com.example.project02.ui.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project02.Database.Entities.Food;
import com.example.project02.R;
import java.util.List;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.FoodViewHolder> {

    private List<Food> mFoodList;

    public FoodListAdapter(List<Food> foodList) {
        mFoodList = foodList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_list_item, parent, false);
        return new FoodViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food currentFood = mFoodList.get(position);
        holder.nameTextView.setText(currentFood.getName());
        holder.familyTextView.setText(currentFood.getFamily());
        holder.descriptionTextView.setText(currentFood.getDescription());
    }

    @Override
    public int getItemCount() {
        if (mFoodList == null) {
            return 0;
        }
        return mFoodList.size();
    }

    public void setFoodList(List<Food> foods) {
        mFoodList = foods;
        notifyDataSetChanged();
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView familyTextView;
        private final TextView descriptionTextView;
        private final Button addButton;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.food_name_textview);
            familyTextView = itemView.findViewById(R.id.food_family_textview);
            descriptionTextView = itemView.findViewById(R.id.food_description_textview);
            addButton = itemView.findViewById(R.id.add_food_button);
        }
    }
}
