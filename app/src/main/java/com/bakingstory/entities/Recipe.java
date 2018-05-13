package com.bakingstory.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by emil.ivanov on 3/28/18.
 * <p>
 * <p>
 * "id":1,
 * "name":"Nutella Pie",
 * "ingredients":[{},{}],
 * "steps":[{},{}]
 * "servings":8,
 * "image":""
 */

@SuppressWarnings({"unused", "CanBeFinal"})
public class Recipe implements Parcelable {
    public static final String RECIPE_DATA = "recipe_data";

    private long id;
    private String name;
    private List<Ingredient> ingredients;
    private List<BakingStep> steps;
    private int servings;
    private String image;
    private boolean isSelected;


    protected Recipe(Parcel in) {
        id = in.readLong();
        name = in.readString();
        ingredients = in.createTypedArrayList(Ingredient.CREATOR);
        steps = in.createTypedArrayList(BakingStep.CREATOR);
        servings = in.readInt();
        image = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public String getName() {
        return name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<BakingStep> getSteps() {
        return steps;
    }

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeTypedList(ingredients);
        dest.writeTypedList(steps);
        dest.writeInt(servings);
        dest.writeString(image);
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
