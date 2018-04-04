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

public class Recipe implements Parcelable {
    public static final String RECIPE_DATA = "recipe_data";

    private long id;
    private String name;
    private List<Ingredient> ingredients;
    private List<BakingStep> steps;
    private double servings;
    private String image;

    protected Recipe(Parcel in) {
        id = in.readLong();
        name = in.readString();
        ingredients = in.createTypedArrayList(Ingredient.CREATOR);
        steps = in.createTypedArrayList(BakingStep.CREATOR);
        servings = in.readDouble();
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<BakingStep> getSteps() {
        return steps;
    }

    public void setSteps(List<BakingStep> steps) {
        this.steps = steps;
    }

    public double getServings() {
        return servings;
    }

    public void setServings(double servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
        dest.writeDouble(servings);
        dest.writeString(image);
    }
}
