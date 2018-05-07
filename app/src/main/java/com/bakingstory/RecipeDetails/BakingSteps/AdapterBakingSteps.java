package com.bakingstory.RecipeDetails.BakingSteps;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bakingstory.R;
import com.bakingstory.databinding.ItemBakingStepsListContentBinding;
import com.bakingstory.databinding.ItemRecipeListContentBinding;
import com.bakingstory.entities.BakingStep;
import com.bakingstory.entities.Recipe;
import com.bakingstory.utils.UtilsImageLoader;

import java.util.List;

/**
 * Created by emil.ivanov on 3/30/18.
 * Recipes collection adapter.
 */

public class AdapterBakingSteps extends RecyclerView.Adapter<AdapterBakingSteps.ViewHolder> {

    private final List<BakingStep> mValues;
    IBakingStepsInteraction mListenerItemInteraction;
    Context mContext;

    public AdapterBakingSteps(IBakingStepsInteraction listenerItemInteraction, List<BakingStep> items, Context context) {
        mValues = items;
        mListenerItemInteraction = listenerItemInteraction;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_baking_steps_list_content, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        if (payloads != null && payloads.size() > 0) {
            holder.bindData((BakingStep) payloads.get(0), position);
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

        ItemBakingStepsListContentBinding mBinding;

        private final View.OnClickListener mListenerItemSelected = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int position = (Integer) view.getTag();
                mListenerItemInteraction.onBakingStepSelection();
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

        void bindData(BakingStep bakingStep, int position) {
            if (bakingStep.isSelected()) {
                mPreviousSelection = position;
            }

            itemView.setSelected(bakingStep.isSelected());
            mBinding.tvStepShortDescription.setSelected(bakingStep.isSelected());
            if (bakingStep.getShortDescription() != null) {
                mBinding.tvStepShortDescription.setText(bakingStep.getShortDescription());
            }


            if (bakingStep.getThumbnailURL() != null && !bakingStep.getThumbnailURL().isEmpty()) {
                UtilsImageLoader.loadNetworkImage(mContext, mBinding.ivThumbImage, bakingStep.getThumbnailURL());
            }
        }


    }

    public interface IBakingStepsInteraction {
        void onBakingStepSelection();
    }
}