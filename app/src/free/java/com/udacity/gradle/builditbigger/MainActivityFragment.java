package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.prady.pradeepkumarr.jokeandroidlib.JokeActivity;
/**
 * Created by pradeepkumarr on 14/10/16.
 */
public class MainActivityFragment extends Fragment implements View.OnClickListener {

    private ProgressBar mprogressBar;
    private InterstitialAd mInterstitialAd;
    private String mjoke;
    private Button jokeButton;


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        jokeButton = (Button) root.findViewById(R.id.button);
        jokeButton.setOnClickListener(this);

        AdView mAdView = (AdView) root.findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        mprogressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        mprogressBar.setVisibility(View.GONE);


        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                loadAd();
                startJokeActivity(mjoke);
            }
        });

        loadAd();


        return root;
    }

    private void loadAd() {
        final AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mInterstitialAd.loadAd(adRequest);
    }

    public void tellJoke(View view) {
        if (!mInterstitialAd.isLoaded() && mInterstitialAd.isLoading())
            loadAd();
        mprogressBar.setVisibility(View.VISIBLE);

        Toast.makeText(getActivity(), R.string.please_wait, Toast.LENGTH_SHORT).show();


        EndpointsAsyncTask endpointsAsyncTask = new EndpointsAsyncTask();
        endpointsAsyncTask.setListner(new EndpointsAsyncTask.TaskListner() {
            @Override
            public void getTaskResult(String result) {
                mprogressBar.setVisibility(View.GONE);
                mjoke = result;
                if (mInterstitialAd.isLoaded())
                    mInterstitialAd.show();
                else
                    startJokeActivity(result);


            }
        }).execute(getActivity());

    }

    private void startJokeActivity(String result) {
        Intent intent = new Intent(getActivity(), JokeActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, result);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        tellJoke(v);
    }
}
