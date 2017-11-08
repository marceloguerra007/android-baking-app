package com.nanodegree.android.bakingapp.utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nanodegree.android.bakingapp.R;
import com.nanodegree.android.bakingapp.model.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by marceloguerra on 12/10/2017.
 */
public class StepListAdapter extends RecyclerView.Adapter<StepListAdapter.ViewHolder>{

    private ArrayList<Step> mStepList;

    private StepListAdpEventsHandler mEventHandler;

    public interface StepListAdpEventsHandler{
        void clickStepListAdp(int stepIndex);
        void focusChangeStepListAdp(int stepIndex);
    }

    public StepListAdapter(StepListAdpEventsHandler eventHandler){
        mEventHandler = eventHandler;
    }

    public void setStepDataList(ArrayList<Step> stepList){
        mStepList = stepList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdListItem = R.layout.item_steplist;
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
        if (mStepList == null)
            return 0;
        else
            return mStepList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnFocusChangeListener {
        @BindView(R.id.TV_StepDescription) public TextView stepDescriptionTV;

        private Context mContext;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnFocusChangeListener(this);

            ButterKnife.bind(this, itemView);

            mContext = itemView.getContext();
        }

        public void bind(int index){
            stepDescriptionTV.setText(mStepList.get(index).getDescription());
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            // verifica se a posição do Adapter existe.
            if(position != RecyclerView.NO_POSITION){
                mEventHandler.clickStepListAdp(mStepList.get(position).getIndex());
            }
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION) {
                mEventHandler.focusChangeStepListAdp(mStepList.get(position).getIndex());
            }
        }
    }
}
