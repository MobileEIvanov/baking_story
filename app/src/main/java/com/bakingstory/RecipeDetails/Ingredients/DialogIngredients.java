package com.bakingstory.RecipeDetails.Ingredients;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.bakingstory.R;

import com.bakingstory.entities.Ingredient;

import java.util.List;

import static com.bakingstory.entities.Ingredient.INGREDIENT_DATA;

/**
 * Created by emil.ivanov on 4/18/18.
 */
public class DialogIngredients extends BottomSheetDialogFragment {

    List<Ingredient> mIngredientsList;


    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();

        if (window != null) {
            WindowManager.LayoutParams windowParams = window.getAttributes();
            windowParams.dimAmount = 0f;
            windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(windowParams);
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getDialog().getWindow() != null) {
            getDialog().getWindow()
                    .getAttributes().windowAnimations = R.style.DialogAnimation;
        }
    }


    @Override
    public void onResume() {

//        if (getDialog() != null && getDialog().getWindow() != null) {
//            //sets full screen dialog
//            ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
//
//            params.width = WindowManager.LayoutParams.MATCH_PARENT;
//            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//            getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
//        }
        super.onResume();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

            if (getArguments().containsKey(INGREDIENT_DATA)) {

                mIngredientsList = getArguments().getParcelableArrayList(INGREDIENT_DATA);
            }
        } else if (savedInstanceState != null) {
             mIngredientsList = savedInstanceState.getParcelableArrayList(INGREDIENT_DATA);

        }
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        return null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    private void initViews() {

    }


}
