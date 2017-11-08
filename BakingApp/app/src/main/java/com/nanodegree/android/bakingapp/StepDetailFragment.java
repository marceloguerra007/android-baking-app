package com.nanodegree.android.bakingapp;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.nanodegree.android.bakingapp.model.Step;
import com.nanodegree.android.bakingapp.utilities.NetworkUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepDetailFragment extends Fragment {
    @BindView(R.id.player_view) public SimpleExoPlayerView simpleExoPlayerView;
    @BindView(R.id.CV_NoVideo) public CardView noVideoCV;
    @BindView(R.id.TV_VideoStatus) public TextView videoStatusTV;
    @Nullable @BindView(R.id.TV_StepInstruction) public TextView stepInstructionTV;
    @Nullable @BindView(R.id.Bt_PreviousStep) public Button previousStepBt;
    @Nullable @BindView(R.id.Bt_NextStep) public Button nextStepBt;

    private SimpleExoPlayer player;
    private ArrayList<Step> mSteps;
    private int stepIndexCurrent;
    private boolean isOrientarionPortrait;

    public StepDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);

        ButterKnife.bind(this, rootView);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            previousStepBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadPreviousStep();
                }
            });

            nextStepBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadNextStep();
                }
            });
            isOrientarionPortrait = true;
        }
        else
            isOrientarionPortrait = false;


        if (savedInstanceState == null){
            stepIndexCurrent = getActivity().getIntent().getIntExtra(Step.STEP_INDEX, 0);
            mSteps = getActivity().getIntent().getParcelableArrayListExtra(Step.STEP_ARRAY);}
        else {
            stepIndexCurrent = savedInstanceState.getInt(Step.STEP_INDEX);
            mSteps = savedInstanceState.getParcelableArrayList(Step.STEP_ARRAY);
        }

        if (mSteps != null) {
            Step stepCurrent = mSteps.get(stepIndexCurrent);

            loadContent(stepCurrent);
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (outState != null){
            outState.putInt(Step.STEP_INDEX, stepIndexCurrent);
            outState.putParcelableArrayList(Step.STEP_ARRAY, mSteps);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        if (player != null){
            player.release();
        }

        super.onStop();
    }

    @Override
    public void onDestroyView() {
        if (player != null){
            player.release();
        }

        super.onDestroyView();
    }

    public void refreshContent(int stepIndex, ArrayList<Step> steps){
        if (player != null)
            player.release();

        stepIndexCurrent = stepIndex;
        mSteps = steps;

        Step stepCurrent = mSteps.get(stepIndexCurrent);

        loadContent(stepCurrent);
    }

    private void loadContent(Step step){
        if (isOrientarionPortrait)
            configButtons();

        if (step != null) {
            if (stepInstructionTV != null)
                stepInstructionTV.setText(step.getInstruction());

            if (!step.getVideoURL().toString().isEmpty()){
                if (NetworkUtil.existsInternetConnection(getContext())){
                    Uri uriVideo = Uri.parse(step.getVideoURL());

                    try {
                        createPlayer(uriVideo);

                        showPlayer();
                    }catch (Exception e){
                        showVideoStatus(getString(R.string.status_error_load_video));
                    }
                }
                else{
                    showVideoStatus(getString(R.string.status_no_internet_connection));
                }
            }else{
                showVideoStatus(getString(R.string.status_no_video_avaliable));
            }
        }
    }

    private void showPlayer(){
        noVideoCV.setVisibility(View.GONE);
        simpleExoPlayerView.setVisibility(View.VISIBLE);
    }

    private void showVideoStatus(String statusMessage){
        videoStatusTV.setText(statusMessage);

        noVideoCV.setVisibility(View.VISIBLE);
        simpleExoPlayerView.setVisibility(View.GONE);
    }

    private void createPlayer(Uri stepVideoUri){
        // 1. Create a default TrackSelector
        Handler mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        // 2. Create the player
        player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

        // Bind the player to the view.
        simpleExoPlayerView.setPlayer(player);

        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();

        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                Util.getUserAgent(getContext(), getString(R.string.app_name)), defaultBandwidthMeter);

        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource(stepVideoUri,
                dataSourceFactory, extractorsFactory, null, null);

        // Prepare the player with the source.
        player.prepare(videoSource);
    }

    private void configButtons(){
        if (stepIndexCurrent > 0){
            previousStepBt.setVisibility(View.VISIBLE);
        }else{
            previousStepBt.setVisibility(View.INVISIBLE);
        }

        if ((stepIndexCurrent+1) == mSteps.size()){
            nextStepBt.setVisibility(View.INVISIBLE);
        } else{
            nextStepBt.setVisibility(View.VISIBLE);
        }
    }

    private void loadPreviousStep(){
        if (player != null)
            player.release();

        stepIndexCurrent--;
        loadContent(mSteps.get(stepIndexCurrent));
    }

    private void loadNextStep(){
        if (player != null)
            player.release();

        stepIndexCurrent++;
        loadContent(mSteps.get(stepIndexCurrent));
    }
}
