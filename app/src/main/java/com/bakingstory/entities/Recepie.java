package com.bakingstory.entities;

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

public class Recepie {

    private long id;
    private String name;
    private List<Ingredient> ingredients;
    private List<BakingStep> steps;
    private double servings;
    private String image;

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
}
