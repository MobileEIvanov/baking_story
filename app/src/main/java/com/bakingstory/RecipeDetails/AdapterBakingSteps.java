package com.bakingstory.RecipeDetails;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bakingstory.R;
import com.bakingstory.databinding.ItemBakingStepsListContentBinding;
import com.bakingstory.entities.BakingStep;

import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * Created by emil.ivanov on 4/4/18.
 * https://github.com/google/ExoPlayer/blob/release-v2/demos/main/src/main/java/com/google/android/exoplayer2/demo/PlayerActivity.java
 *
 */
public class AdapterBakingSteps extends Adapter<AdapterBakingSteps.ViewHolder> {

    private List<BakingStep> mData;
    private IStepsInteraction mListener;

    public AdapterBakingSteps(@NonNull List<BakingStep> stepList, IStepsInteraction listener) {
        mData = stepList;
        mListener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_baking_steps_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(mData.get(position));
        holder.bindData(mData.get(position));
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface IStepsInteraction {
        void onBakingStepSelected(BakingStep bakingStep);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemBakingStepsListContentBinding mBinding;

        View.OnClickListener mStepClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onBakingStepSelected((BakingStep) v.getTag());
            }
        };

        public ViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
            itemView.setOnClickListener(mStepClickListener);
        }

        void bindData(BakingStep bakingStep) {
            if (bakingStep.getShortDescription() != null && !bakingStep.getShortDescription().isEmpty()) {
                mBinding.tvStepDescription.setText(bakingStep.getShortDescription());
            }
        }
    }
}
