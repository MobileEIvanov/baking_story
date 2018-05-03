package com.bakingstory.RecipeDetails.Ingredients;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bakingstory.R;
import com.bakingstory.databinding.ItemIngredientBinding;
import com.bakingstory.entities.Ingredient;

import java.util.List;

/**
 * Created by emil.ivanov on 3/30/18.
 * Recipes collection adapter.
 */

public class AdapterIngredients extends RecyclerView.Adapter<AdapterIngredients.ViewHolder> {

    private final List<Ingredient> mValues;


    public AdapterIngredients(List<Ingredient> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient, parent, false);
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


        ItemIngredientBinding mBinding;

        ViewHolder(View view) {
            super(view);
            mBinding = DataBindingUtil.bind(view);


        }

        void bindData(Ingredient ingredient) {
            if (ingredient.getIngredient() != null) {
                mBinding.tvIngredient.setText(ingredient.getIngredient());
            }
            if (ingredient.getMeasurementAsPlainText() != null) {
                mBinding.tvMeasurement.setText(ingredient.getMeasurementAsPlainText());
            }
            if (ingredient.getQuantity() > 0) {
                mBinding.tvMeasurement.setText("" + ingredient.getQuantity());
            }
        }
    }


}