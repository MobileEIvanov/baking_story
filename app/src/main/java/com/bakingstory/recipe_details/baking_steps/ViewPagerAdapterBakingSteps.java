package com.bakingstory.recipe_details.baking_steps;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.bakingstory.recipe_details.FragmentStepDescription;
import com.bakingstory.entities.BakingStep;

import java.util.List;

/**
 * Created by emil.ivanov on 5/6/18.
 * Fragment pager adapter which will return new instance for every step that we have in the baking process.
 */
public class ViewPagerAdapterBakingSteps extends FragmentStatePagerAdapter {


    private final SparseArray<Fragment> registeredFragments = new SparseArray<>();
    private final List<BakingStep> mData;


    public ViewPagerAdapterBakingSteps(FragmentManager fm, List<BakingStep> data) {
        super(fm);
        this.mData = data;
    }

    @Override
    public Fragment getItem(int position) {
        return FragmentStepDescription.newInstance(mData.get(position), position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

}
