package com.nanodegree.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.nanodegree.android.bakingapp.model.Recipe;

public class StepDetailActivity extends AppCompatActivity {

    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        if (savedInstanceState == null)
            mRecipe = getIntent().getParcelableExtra(Recipe.RECIPE_OBJ);
        else
            mRecipe = savedInstanceState.getParcelable(Recipe.RECIPE_OBJ);

        if (mRecipe != null){
            setTitle(mRecipe.getName());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Recipe.RECIPE_OBJ, mRecipe);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: // Botão "Up" ou "Back"
                Intent intentDetail = new Intent(this, DetailActivity.class);
                intentDetail.putExtra(Recipe.RECIPE_OBJ,mRecipe);
                startActivity(intentDetail);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
