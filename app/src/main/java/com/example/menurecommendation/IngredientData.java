package com.example.menurecommendation;

public class IngredientData {

    IngredientData(){}

    private int RecipeID;

    public int getRecipeID() {
        return RecipeID;
    }

    public void setRecipeID(int recipeID) {
        RecipeID = recipeID;
    }

    public String getIngredient() {
        return Ingredient;
    }

    public void setIngredient(String ingredient) {
        Ingredient = ingredient;
    }

    public float getQuatity() {
        return Quatity;
    }

    public void setQuatity(float quatity) {
        Quatity = quatity;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    private String Ingredient;
    private float Quatity;
    private String Unit;


}
