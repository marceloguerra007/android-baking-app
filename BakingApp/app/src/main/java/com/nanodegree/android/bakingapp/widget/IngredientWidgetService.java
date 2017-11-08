package com.nanodegree.android.bakingapp.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.widget.RemoteViews;

import com.nanodegree.android.bakingapp.R;
import com.nanodegree.android.bakingapp.model.Ingredient;
import com.nanodegree.android.bakingapp.model.Recipe;
import com.nanodegree.android.bakingapp.utilities.JsonUtils;
import com.nanodegree.android.bakingapp.utilities.NetworkUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by marceloguerra on 17/10/2017.
 */
public final class IngredientWidgetService extends IntentService {

    public static int recipeIndex = -1;
    public static int maxRecipeIndex = -1;
    public static ArrayList<Recipe> recipes;

    public static String statusMessage = "";
    public static String bakingJsonWidget = "";

    public IngredientWidgetService(){
        super("IngredientWidgetService");
    }

    public static int getNextRecipeIndex(){
        recipeIndex++;

        if (recipeIndex > maxRecipeIndex)
            recipeIndex = 0;

        return recipeIndex;
    }

    public static String getRecipeName(int recipeIndex){

        if ((recipes != null) && (recipes.size() > 0))
            return recipes.get(recipeIndex).getName();
        else
            return "";
    }

    public static String getRecipeIngredients(int recipeIndex){
        String ingredientsText = "";

        if ((recipes != null) && (recipes.size() > 0)){
            try{
                int recipeId = recipes.get(recipeIndex).getId();

                if (!bakingJsonWidget.isEmpty()){

                    ArrayList<Ingredient> ingredients = JsonUtils.getIngredientsByRecipeFromJson(bakingJsonWidget, recipeId);

                    for (Ingredient ingredient : ingredients) {
                        DecimalFormat df = new DecimalFormat("0.###");
                        String quantity = df.format(ingredient.getQuantity());
                        String text = quantity + ingredient.getMeasure() + " " + ingredient.getDescription();

                        if (!ingredientsText.isEmpty())
                            ingredientsText += "\n" + text;
                        else
                            ingredientsText = text;
                    }
                }

            } catch (Exception e){
                return "error:" + e.getMessage();
            }

            return ingredientsText;
        }else
        {
            return statusMessage;
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (recipes == null)
            loadRecipes();

        setLayoutData();
    }

    private void loadRecipes(){
        try
        {
            bakingJsonWidget = "";

            if (NetworkUtil.existsInternetConnection(getApplicationContext()))
                bakingJsonWidget = NetworkUtil.getJsonFromInternet();
            else
                throw new Exception(getApplicationContext().getString(R.string.status_no_internet_connection));

            recipes = JsonUtils.getRecipeListFromJson(bakingJsonWidget);
            maxRecipeIndex = recipes.size() - 1;
        }
        catch (Exception e){
            recipes = null;
            statusMessage = e.getMessage();
        }
    }

    private void setLayoutData(){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                IngredientWidgetProvider.class));

        for (int widgetId : appWidgetIds){

            int recipeIndex = IngredientWidgetService.getNextRecipeIndex();

            RemoteViews remoteViews = new RemoteViews(getPackageName(),
                    R.layout.widget_ingredient);
            remoteViews.setTextViewText(R.id.TV_WidgetRecipeName, getRecipeName(recipeIndex));
            remoteViews.setTextViewText(R.id.TV_WidgetIngredientList, getRecipeIngredients(recipeIndex));

            Intent intent = new Intent(getApplicationContext(), IngredientWidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.BT_OtherRecipe, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }
}

