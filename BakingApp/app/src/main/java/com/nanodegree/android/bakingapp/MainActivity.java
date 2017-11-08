package com.nanodegree.android.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nanodegree.android.bakingapp.model.Recipe;
import com.nanodegree.android.bakingapp.utilities.JsonUtils;
import com.nanodegree.android.bakingapp.utilities.NetworkUtil;
import com.nanodegree.android.bakingapp.utilities.RecipeListAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipeListAdapter.RecipeListAdpOnClickHandler {

    @BindView(R.id.TV_StatusMessageDisplay) public TextView mStatusMessageDisplay;
    @BindView(R.id.recyclerview_recipelist) public RecyclerView mRecyclerView;
    @BindView(R.id.pb_loading_indicator) public ProgressBar mLoadingIndicator;
    @BindView(R.id.Bt_TryAgain) public Button mTryAgainButton;

    private RecipeListAdapter mRecipeListAdapter;
    private ArrayList<Recipe> recipesCache;
    private String loadRecipesStatusMessage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mTryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryDataLoadAgain();
            }
        });

        GridLayoutManager gridLayoutManager;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        else
            gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);

        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        gridLayoutManager.setReverseLayout(false);

        mRecyclerView.setLayoutManager(gridLayoutManager);

        mRecyclerView.setHasFixedSize(true);

        mRecipeListAdapter = new RecipeListAdapter(this);

        mRecyclerView.setAdapter(mRecipeListAdapter);

        new FetchRecipeListTask().execute();
    }

    private ArrayList<Recipe> loadRecipesFromJson(){
        ArrayList<Recipe> recipes;

        try
        {
            if (JsonUtils.getBakingJsonCache().isEmpty()){
                if (NetworkUtil.existsInternetConnection(this)) {
                    JsonUtils.setBakingJsonCache(NetworkUtil.getJsonFromInternet());
                }else
                    throw new Exception(getString(R.string.status_no_internet_connection));
            }

            recipes = JsonUtils.getRecipeListFromJson(JsonUtils.getBakingJsonCache());
        }
        catch (Exception e){
            recipes = null;
            loadRecipesStatusMessage = getResources().getString(R.string.status_error_load_json) + " " + e.getMessage();
        }

        return recipes;
    }

    private void tryDataLoadAgain(){
        new FetchRecipeListTask().execute();
    }

    private void showStatusMessage(String errorMessage){
        mRecyclerView.setVisibility(View.INVISIBLE);

        mStatusMessageDisplay.setText(errorMessage);
        mStatusMessageDisplay.setVisibility(View.VISIBLE);
        mTryAgainButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void clickRecipeListAdp(Recipe recipe) {
        Intent intentDetail = new Intent(this, DetailActivity.class);
        intentDetail.putExtra(Recipe.RECIPE_OBJ,recipe);
        startActivity(intentDetail);
    }

    public class FetchRecipeListTask extends AsyncTask<String, Void, ArrayList<Recipe>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);

            mStatusMessageDisplay.setVisibility(View.INVISIBLE);
            mTryAgainButton.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
        }

        @Override
        protected ArrayList<Recipe> doInBackground(String... params) {
            return loadRecipesFromJson();
        }

        @Override
        protected void onPostExecute(ArrayList<Recipe> recipes) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);

            if (recipes != null){
                if (recipes.size() > 0){
                    mRecipeListAdapter.setRecipeDataList(recipes);
                    recipesCache = recipes;

                    mRecyclerView.setVisibility(View.VISIBLE);
                }else{
                    showStatusMessage(getResources().getString(R.string.status_not_found_recipes));
                }
            }
            else
                showStatusMessage(loadRecipesStatusMessage);
        }
    }
}
