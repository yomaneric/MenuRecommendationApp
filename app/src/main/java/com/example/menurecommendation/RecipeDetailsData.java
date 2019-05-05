package com.example.menurecommendation;

import android.os.Parcelable;

import java.io.Serializable;

public class RecipeDetailsData implements Serializable {

    private int ID;
    private String recipeName;
    private int cookingTime;
    private String Ingredients;
    private String Steps;
    private int Difficulty;
    private String Tags;

    RecipeDetailsData(){}

    int getID() {
        return ID;
    }

    void setID(int ID) {
        this.ID = ID;
    }

    String getRecipeName() {
        return recipeName;
    }

    void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    void setCookingTime(int cookingTime) {
        this.cookingTime = cookingTime;
    }

    public String getIngredients() {
        return Ingredients;
    }

    void setIngredients(String ingredients) {
        Ingredients = ingredients;
    }

    public String getSteps() {
        return Steps;
    }

    void setSteps(String steps) {
        Steps = steps;
    }

    public int getDifficulty() {
        return Difficulty;
    }

    void setDifficulty(int difficulty) {
        Difficulty = difficulty;
    }

    public String getTags() {
        return Tags;
    }

    void setTags(String tags) {
        Tags = tags;
    }

}
