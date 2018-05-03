package com.bakingstory.RecipeCollection;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bakingstory.R;
import com.bakingstory.databinding.ItemRecipeListContentBinding;
import com.bakingstory.entities.Recipe;

import java.util.List;

/**
 * Created by emil.ivanov on 3/30/18.
 * Recipes collection adapter.
 */

public class AdapterRecipes extends RecyclerView.Adapter<AdapterRecipes.ViewHolder> {

    private final List<Recipe> mValues;
    IRecipeInteraction mListenerItemInteraction;

    public AdapterRecipes(IRecipeInteraction listenerItemInteraction, List<Recipe> items) {
        mValues = items;
        mListenerItemInteraction = listenerItemInteraction;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(mValues.get(position));
        holder.bindData(mValues.get(position));

    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemRecipeListContentBinding mBinding;

        private final View.OnClickListener mListenerItemSelected = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListenerItemInteraction.onRecipeSelection((Recipe) view.getTag(), view);
            }
        };

        ViewHolder(View view) {
            super(view);
            mBinding = DataBindingUtil.bind(view);

            itemView.setOnClickListener(mListenerItemSelected);
        }

        void bindData(Recipe recepie) {
            if (recepie.getName() != null) {
                mBinding.idRecepieName.setText(recepie.getName());
            }

        }
    }

    public interface IRecipeInteraction {
        void onRecipeSelection(Recipe recipe, View view);
    }
}