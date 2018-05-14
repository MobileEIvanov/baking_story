package com.bakingstory.recipe_details.baking_steps;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bakingstory.R;
import com.bakingstory.databinding.ItemBakingStepsListContentBinding;
import com.bakingstory.entities.BakingStep;
import com.bakingstory.utils.UtilsImageLoader;

import java.util.List;

/**
 * Created by emil.ivanov on 3/30/18.
 * Recipes collection adapter.
 */

public class AdapterBakingSteps extends RecyclerView.Adapter<AdapterBakingSteps.ViewHolder> {

    private final List<BakingStep> mData;
    private final IBakingStepsInteraction mListenerItemInteraction;
    private final Context mContext;

    AdapterBakingSteps(IBakingStepsInteraction listenerItemInteraction, List<BakingStep> items, Context context) {
        mData = items;
        mListenerItemInteraction = listenerItemInteraction;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_baking_steps_list_content, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.size() > 0) {
            holder.bindData((BakingStep) payloads.get(0), position);
        } else {
            onBindViewHolder(holder, position);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.bindData(mData.get(position), position);

    }

    public void selectBakingStep(int position) {
        refreshListItemsSelectedState(position);
    }

    /**
     * Updates the UI and the data set with the selected item.
     * Stores {@link #mPreviousSelection} item in order to clear the UI on next selection.
     *
     * @param position - selected element position
     */
    private void refreshListItemsSelectedState(int position) {
        if (mPreviousSelection != -1) {
            mData.get(mPreviousSelection).setSelected(false);
            notifyItemChanged(mPreviousSelection, mData.get(mPreviousSelection));
        }


        mPreviousSelection = position;
        mData.get(position).setSelected(true);
        notifyItemChanged(position, mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private int mPreviousSelection = -1;

    class ViewHolder extends RecyclerView.ViewHolder {

        final ItemBakingStepsListContentBinding mBinding;

        private final View.OnClickListener mListenerItemSelected = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int position = (Integer) view.getTag();
                mListenerItemInteraction.onBakingStepSelection(position);
                refreshListItemsSelectedState(position);

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
            if (!TextUtils.isEmpty(bakingStep.getShortDescription())) {
                mBinding.tvStepShortDescription.setText(bakingStep.getShortDescription());
            }


            if (!TextUtils.isEmpty(bakingStep.getThumbnailURL())) {
                UtilsImageLoader.loadNetworkImage(mContext, mBinding.ivThumbImage, bakingStep.getThumbnailURL());
            }
        }


    }

    public interface IBakingStepsInteraction {
        void onBakingStepSelection(int position);
    }
}