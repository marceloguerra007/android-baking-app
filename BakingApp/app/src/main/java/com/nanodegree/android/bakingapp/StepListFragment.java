package com.nanodegree.android.bakingapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nanodegree.android.bakingapp.model.Ingredient;
import com.nanodegree.android.bakingapp.model.Recipe;
import com.nanodegree.android.bakingapp.model.Step;
import com.nanodegree.android.bakingapp.utilities.JsonUtils;
import com.nanodegree.android.bakingapp.utilities.StepListAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
* Created by marceloguerra on 08/10/2017.
* */
public class StepListFragment extends Fragment implements StepListAdapter.StepListAdpEventsHandler{

    @BindView(R.id.TV_ContentIngredient) public TextView contentIngredientTV;
    @BindView(R.id.RV_StepList) public RecyclerView mRecyclerView;

    private StepListAdapter mStepListAdapter;
    private Recipe mRecipe;
    private ArrayList<Step> mSteps;

    public StepListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_step_list, container, false);

        ButterKnife.bind(this, rootView);

        mRecipe = getActivity().getIntent().getParcelableExtra(Recipe.RECIPE_OBJ);

        if (mRecipe != null){
            loadContent();
        }

        // Inflate the layout for this fragment
        return rootView;
    }

    private void loadContent(){
        //Load content from Ingredients
        new LoadIngredientListTask().execute(String.valueOf(mRecipe.getId()));

        //Load content from Steps
        GridLayoutManager gridLayoutManager;

        gridLayoutManager = new GridLayoutManager(getContext(), 1);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        gridLayoutManager.setReverseLayout(false);

        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mStepListAdapter = new StepListAdapter(this);
        mRecyclerView.setAdapter(mStepListAdapter);

        new LoadStepListTask().execute(String.valueOf(mRecipe.getId()));
    }

    private void refreshStepDetailFragment(int stepIndex){

        StepDetailFragment fragment = (StepDetailFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_step_detail);
        if (fragment != null)
            fragment.refreshContent(stepIndex, mSteps);
    }

    private void openStepDetailActivity(int stepIndex){
        Intent stepDetailIntent = new Intent(getActivity(), StepDetailActivity.class);
        stepDetailIntent.putExtra(Step.STEP_INDEX, stepIndex);
        stepDetailIntent.putExtra(Step.STEP_ARRAY, mSteps);
        stepDetailIntent.putExtra(Recipe.RECIPE_OBJ, mRecipe);
        startActivity(stepDetailIntent);
    }

    @Override
    public void clickStepListAdp(int stepIndex) {
        if (getActivity().findViewById(R.id.fragment_step_detail) != null)
        {
            refreshStepDetailFragment(stepIndex);
        }else
            openStepDetailActivity(stepIndex);
    }

    @Override
    public void focusChangeStepListAdp(int stepIndex){
        if (getActivity().findViewById(R.id.fragment_step_detail) != null)
        {
            refreshStepDetailFragment(stepIndex);
        }
    }

    public class LoadIngredientListTask extends AsyncTask<String, Void, ArrayList<Ingredient>>{

        @Override
        protected ArrayList<Ingredient> doInBackground(String... params) {
            ArrayList<Ingredient> ingredients;

            try{
                int recipeId = Integer.parseInt(params[0]);

                //TODO Testar.
                ingredients = JsonUtils.getIngredientsByRecipeFromJson(JsonUtils.getBakingJsonCache(), recipeId);

            } catch (Exception e){
                ingredients = null;
            };

            return ingredients;
        }

        @Override
        protected void onPostExecute(ArrayList<Ingredient> ingredients) {
            for (Ingredient ingredient : ingredients) {
                DecimalFormat df = new DecimalFormat("0.###");
                String quantity = df.format(ingredient.getQuantity());

                contentIngredientTV.append("\n" + quantity + ingredient.getMeasure() + " " + ingredient.getDescription());
            }
        }
    }

    public class LoadStepListTask extends AsyncTask<String, Void, ArrayList<Step>>{

        @Override
        protected ArrayList<Step> doInBackground(String... params) {
            ArrayList<Step> steps;

            try{
                int recipeId = Integer.parseInt(params[0]);

                //TODO 2. Testar.
                steps = JsonUtils.getStepsByRecipeFromJson(JsonUtils.getBakingJsonCache(), recipeId);

            } catch (Exception e){
                steps = null;
            }

            return steps;
        }

        @Override
        protected void onPostExecute(ArrayList<Step> steps) {
            if (steps != null){
                mStepListAdapter.setStepDataList(steps);
                mSteps = steps;
            }

            super.onPostExecute(steps);
        }
    }
}
