package com.example.menurecommendation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.CardViewHolder> {
    private ArrayList<RecipeDetailsData> recipeDetailsData;
    private Context context;

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView recipeRame;
        public TextView tag;
        public TextView difficulty;
        public TextView cookingTime;
        public TextView ID;

        public CardViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.cardImage);
            recipeRame = itemView.findViewById(R.id.recipe_name_mg);
            tag = itemView.findViewById(R.id.tag_mg);
            difficulty = itemView.findViewById(R.id.difficulty_mg);
            cookingTime = itemView.findViewById(R.id.cooking_time_mg);
            ID = itemView.findViewById(R.id.recipe_id_mg);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = Integer.parseInt(ID.getText().toString());
                    ((MainActivity) view.getContext()).DetailRecipeFragment(id);
                }
            });
        }
    }

    public CardViewAdapter(ArrayList<RecipeDetailsData> recipeDetailsData) {
        this.recipeDetailsData = recipeDetailsData;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.card_item, parent, false);
        return new CardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        RecipeDetailsData currentItem = recipeDetailsData.get(position);
        int resourceId = context.getResources().getIdentifier("recipe_"+currentItem.getID(), "drawable",
                context.getPackageName());
        holder.ID.setText(String.format(""+currentItem.getID()));
        holder.mImageView.setImageResource(resourceId);
        holder.recipeRame.setText(currentItem.getRecipeName());
        holder.tag.setText(String.format("#"+currentItem.getTags()));
        holder.difficulty.setText(String.format("Difficulty: " + currentItem.getDifficulty()+ " / 5"));
        holder.cookingTime.setText(String.format("Cooking Time: " + currentItem.getCookingTime()+ " mins"));
    }

    @Override
    public int getItemCount() {
        return recipeDetailsData.size();
    }
}
