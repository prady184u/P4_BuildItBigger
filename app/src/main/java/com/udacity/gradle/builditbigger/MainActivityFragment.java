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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.prady.pradeepkumarr.jokeandroidlib.JokeActivity;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements View.OnClickListener {

    private Button jokeButton;
    private ProgressBar mprogressBar;
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        AdView mAdView = (AdView) root.findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);



        jokeButton = (Button) root.findViewById(R.id.button);
        jokeButton.setOnClickListener(this);

        mprogressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        mprogressBar.setVisibility(View.GONE);

        return root;
    }

    @Override
    public void onClick(View v) {
        mprogressBar.setVisibility(View.VISIBLE);
       Toast.makeText(getActivity(), R.string.please_wait, Toast.LENGTH_SHORT).show();
        EndpointsAsyncTask endpointsAsyncTask = new EndpointsAsyncTask();
        endpointsAsyncTask.setListner(new EndpointsAsyncTask.TaskListner() {
            @Override
            public void getTaskResult(String result) {
                mprogressBar.setVisibility(View.GONE);
                Intent intent = new Intent(getActivity(), JokeActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, result);
                startActivity(intent);

            }
        }).execute(getActivity());
    }
}
