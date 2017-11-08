package com.nanodegree.android.bakingapp.utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nanodegree.android.bakingapp.R;
import com.nanodegree.android.bakingapp.model.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by marceloguerra on 03/10/2017.
 */
public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder>{

    private ArrayList<Recipe> mRecipeList;
    private RecipeListAdpOnClickHandler mClickHandler;

    public interface RecipeListAdpOnClickHandler{
        void clickRecipeListAdp(Recipe recipe);
    }

    public RecipeListAdapter(RecipeListAdpOnClickHandler clickHandler){
        mClickHandler = clickHandler;
    }

    public void setRecipeDataList(ArrayList<Recipe> recipeList){
        mRecipeList = recipeList;
        notifyDataSetChanged();
    }

    @Override
    public RecipeListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdListItem = R.layout.item_recipelist;
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(layoutIdListItem, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        vh.setIsRecyclable(false);
        vh.bind(position);
    }

    @Override
    public int getItemCount() {
        if (mRecipeList == null)
            return 0;
        else
            return mRecipeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_namerecipe) TextView mNameRecipeText;

        private Context mContext;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            ButterKnife.bind(this, itemView);

            mContext = itemView.getContext();
        }

        public void bind(int index){
            mNameRecipeText.setText(mRecipeList.get(index).getName());
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            // verifica se a posição do Adapter existe.
            if(position != RecyclerView.NO_POSITION){
                mClickHandler.clickRecipeListAdp(mRecipeList.get(position));
            }
        }
    }
}
