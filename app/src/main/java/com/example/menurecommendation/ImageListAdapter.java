package com.example.menurecommendation;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ImageViewHolder> {

    private LayoutInflater mInflater;
    private Context context;
    private final List<RecipeDetailsData> recipes;

    class ImageViewHolder extends RecyclerView.ViewHolder {

        public final ImageView imageItemView;
        public final TextView textItemView;
        public final TextView IDView;
        final ImageListAdapter mAdapter;

        public ImageViewHolder(View itemView, ImageListAdapter adapter) {
            super(itemView);
            imageItemView = itemView.findViewById(R.id.cuisineImage);
            textItemView = itemView.findViewById(R.id.cuisineName);
            IDView = itemView.findViewById(R.id.ID_image_list_item);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = Integer.parseInt(IDView.getText().toString());
                    ((MainActivity) view.getContext()).DetailRecipeFragment(id);
                }
            });
            this.mAdapter = adapter;
        }
    }

    public ImageListAdapter(Context context, List<RecipeDetailsData> recipes) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.recipes = recipes;
    }

    @Override public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.imagelist_item, parent, false);
        return new ImageViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(ImageListAdapter.ImageViewHolder holder, int position) {
        RecipeDetailsData recipe = recipes.get(position);
        Resources resource = context.getResources();
        Log.d("Photo", "recipe_"+recipe.getID()+".jpg");
        int resourceId = resource.getIdentifier("recipe_"+recipe.getID(), "drawable",
                context.getPackageName());
        holder.IDView.setText(String.format(""+recipe.getID()));
        holder.imageItemView.setImageResource(resourceId);
        holder.textItemView.setText(recipe.getRecipeName());
        Log.d("recipe name", ""+recipe.getRecipeName());
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }
}
