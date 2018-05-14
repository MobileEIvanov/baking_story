package com.bakingstory.recipe_details.ingredients;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setTag(mValues.get(position));
        holder.bindData(mValues.get(position));

    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        final ItemIngredientBinding mBinding;

        ViewHolder(View view) {
            super(view);
            mBinding = DataBindingUtil.bind(view);
        }

        void bindData(Ingredient ingredient) {
            if (!TextUtils.isEmpty(ingredient.getIngredient())) {
                mBinding.tvIngredient.setText(ingredient.getIngredient());
            }
            if (!TextUtils.isEmpty(ingredient.getMeasurementAsPlainText())) {
                mBinding.tvMeasurement.setText(ingredient.getMeasurementAsPlainText());
            }
            if (ingredient.getQuantity() > 0) {
                mBinding.tvQuantity.setText(ingredient.getFormattedQuantity());
            }
        }
    }


}