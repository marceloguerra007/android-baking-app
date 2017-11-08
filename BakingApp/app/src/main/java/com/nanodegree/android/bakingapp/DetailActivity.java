package com.nanodegree.android.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nanodegree.android.bakingapp.model.Recipe;

public class DetailActivity extends AppCompatActivity {

    public Recipe currentRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            currentRecipe = getIntent().getParcelableExtra(Recipe.RECIPE_OBJ);
        }else {
            currentRecipe = savedInstanceState.getParcelable(Recipe.RECIPE_OBJ);
        }

        if (currentRecipe != null)
            setTitle(currentRecipe.getName());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Recipe.RECIPE_OBJ, currentRecipe);
        super.onSaveInstanceState(outState);
    }
}
