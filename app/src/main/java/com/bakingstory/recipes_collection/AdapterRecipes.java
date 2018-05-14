package com.bakingstory.recipes_collection;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bakingstory.R;
import com.bakingstory.databinding.ItemRecipeListContentBinding;
import com.bakingstory.entities.Recipe;
import com.bakingstory.utils.UtilsImageLoader;

import java.util.List;

/**
 * Created by emil.ivanov on 3/30/18.
 * Recipes collection adapter.
 */

public class AdapterRecipes extends RecyclerView.Adapter<AdapterRecipes.ViewHolder> {

    private final List<Recipe> mValues;
    private final IRecipeInteraction mListenerItemInteraction;
    private final Context mContext;

    public AdapterRecipes(IRecipeInteraction listenerItemInteraction, List<Recipe> items, Context context) {
        mValues = items;
        mListenerItemInteraction = listenerItemInteraction;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe_list_content, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.size() > 0) {
            holder.bindData((Recipe) payloads.get(0), position);
        } else {
            onBindViewHolder(holder, position);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.bindData(mValues.get(position), position);

    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    private int mPreviousSelection = -1;

    class ViewHolder extends RecyclerView.ViewHolder {

        final ItemRecipeListContentBinding mBinding;

        private final View.OnClickListener mListenerItemSelected = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int position = (Integer) view.getTag();
                mListenerItemInteraction.onRecipeSelection(mValues.get(position));
                if (mPreviousSelection != -1) {
                    mValues.get(mPreviousSelection).setSelected(false);
                    notifyItemChanged(mPreviousSelection, mValues.get(mPreviousSelection));
                }


                mPreviousSelection = position;
                mValues.get(position).setSelected(true);
                notifyItemChanged(position, mValues.get(position));

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
            if (!TextUtils.isEmpty(recipe.getName())) {
                mBinding.tvRecipeName.setText(recipe.getName());
            }


            if (recipe.getImage() != null && !recipe.getImage().isEmpty()) {
                UtilsImageLoader.loadNetworkImage(mContext, mBinding.ivRecipeImage, recipe.getImage());
            }
        }


    }

    public interface IRecipeInteraction {
        void onRecipeSelection(Recipe recipe);
    }
}