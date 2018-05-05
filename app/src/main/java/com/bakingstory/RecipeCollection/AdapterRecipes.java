package com.bakingstory.RecipeCollection;

import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
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
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        if (payloads != null && payloads.size() > 0) {
            holder.bindData((Recipe) payloads.get(0), position);
        } else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.bindData(mValues.get(position), position);

    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    private int mPreviousSelection = -1;

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemRecipeListContentBinding mBinding;

        private final View.OnClickListener mListenerItemSelected = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPreviousSelection != -1) {
                    mValues.get(mPreviousSelection).setSelected(false);
                    notifyItemChanged(mPreviousSelection, mValues.get(mPreviousSelection));
                }

                int position = (Integer) view.getTag();
                mPreviousSelection = position;
                mValues.get(position).setSelected(true);
                notifyItemChanged(position, mValues.get(position));
                mListenerItemInteraction.onRecipeSelection(mValues.get(position));
            }
        };

        ViewHolder(View view) {
            super(view);
            mBinding = DataBindingUtil.bind(view);

            itemView.setOnClickListener(mListenerItemSelected);
        }

        void bindData(Recipe recipe, int position) {
            if (recipe.isSelected()) {
                mPreviousSelection = position;
            }

            itemView.setSelected(recipe.isSelected());
            mBinding.tvRecipeName.setSelected(recipe.isSelected());
            if (recipe.getName() != null) {
                mBinding.tvRecipeName.setText(recipe.getName());
            }

        }
    }

    public interface IRecipeInteraction {
        void onRecipeSelection(Recipe recipe);
    }
}