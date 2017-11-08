package com.nanodegree.android.bakingapp.utilities;

import com.nanodegree.android.bakingapp.model.Ingredient;
import com.nanodegree.android.bakingapp.model.Recipe;
import com.nanodegree.android.bakingapp.model.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by marceloguerra on 06/10/2017.
 */
public class JsonUtils {
    private static String bakingJsonCache = "";
    private static final String RECIPE_ID = "id";
    private static final String RECIPE_NAME = "name";
    private static final String RECIPE_SERVINGS = "servings";
    private static final String RECIPE_INGREDIENTS = "ingredients";
    private static final String INGREDIENT_QUANTITY = "quantity";
    private static final String INGREDIENT_MEASURE = "measure";
    private static final String INGREDIENT_DESCRIPTION = "ingredient";
    private static final String RECIPE_STEPS = "steps";
    private static final String STEP_ID = "id";
    private static final String STEP_SHORTDESCRIPTION = "shortDescription";
    private static final String STEP_DESCRIPTION = "description";
    private static final String STEP_VIDEOURL = "videoURL";

    public static ArrayList<Recipe> getRecipeListFromJson(String json) throws JSONException {

        ArrayList<Recipe> recipes = new ArrayList<Recipe>();

        JSONArray jsonArray = new JSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject objRecipe = jsonArray.getJSONObject(i);

            Recipe recipe = new Recipe();
            recipe.setId(objRecipe.optInt(RECIPE_ID));
            recipe.setName(objRecipe.optString(RECIPE_NAME));
            recipe.setServings(objRecipe.optInt(RECIPE_SERVINGS));

            recipes.add(recipe);
        }

        return recipes;
    }

    public static ArrayList<Ingredient> getIngredientsByRecipeFromJson(String json, int recipeId) throws JSONException{
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();

        JSONArray jsonArrayRecipes = new JSONArray(json);

        for (int x = 0; x < jsonArrayRecipes.length(); x++) {
            JSONObject objRecipe = jsonArrayRecipes.getJSONObject(x);

            if (objRecipe.optInt(RECIPE_ID) == recipeId){
                JSONArray jsonArrayIngredients = objRecipe.getJSONArray(RECIPE_INGREDIENTS);

                for (int y = 0; y < jsonArrayIngredients.length(); y++){
                    JSONObject objIngredient = jsonArrayIngredients.getJSONObject(y);

                    Ingredient ingredient = new Ingredient();

                    ingredient.setId(y);
                    ingredient.setMeasure(objIngredient.optString(INGREDIENT_MEASURE, ""));
                    ingredient.setQuantity(objIngredient.optDouble(INGREDIENT_QUANTITY, 0));
                    ingredient.setDescription(objIngredient.optString(INGREDIENT_DESCRIPTION, ""));

                    ingredients.add(ingredient);
                }

                break;
            }
        }

        return ingredients;
    }

    public static ArrayList<Step> getStepsByRecipeFromJson(String json, int recipeId) throws JSONException{
        ArrayList<Step> steps = new ArrayList<Step>();

        JSONArray jsonArrayRecipes = new JSONArray(json);

        for (int x = 0; x < jsonArrayRecipes.length(); x++) {
            JSONObject objRecipe = jsonArrayRecipes.getJSONObject(x);

            if (objRecipe.optInt(RECIPE_ID) == recipeId){
                JSONArray jsonArraySteps = objRecipe.getJSONArray(RECIPE_STEPS);

                for (int y = 0; y < jsonArraySteps.length(); y++){
                    JSONObject objStep = jsonArraySteps.getJSONObject(y);

                    Step step = new Step();

                    step.setIndex(y);
                    step.setId(objStep.optInt(STEP_ID, y));
                    step.setDescription(objStep.optString(STEP_SHORTDESCRIPTION, ""));
                    step.setInstruction(objStep.optString(STEP_DESCRIPTION, ""));
                    step.setVideoURL(objStep.optString(STEP_VIDEOURL, ""));

                    steps.add(step);
                }

                break;
            }
        }

        return steps;
    }

    public static String getBakingJsonCache() {
        return bakingJsonCache;
    }

    public static void setBakingJsonCache(String bakingJsonCache) {
        JsonUtils.bakingJsonCache = bakingJsonCache;
    }
}
