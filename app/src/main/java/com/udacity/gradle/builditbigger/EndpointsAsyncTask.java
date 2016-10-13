package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.os.AsyncTask;

import com.example.pradeepkumarr.jokes.backend.jokeApi.JokeApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

/**
 * Created by pradeepkumarr on 13/10/16.
 */

class EndpointsAsyncTask extends AsyncTask<Context, Void, String> {
    private static JokeApi myApiService = null;
    private Context context;
    private TaskListner mlistner = null;


    @Override
    protected String doInBackground(Context... params) {
        if (myApiService == null) {  // Only do this once
            JokeApi.Builder builder = new JokeApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://fbmsgrpoc.appspot.com/_ah/api/");

            myApiService = builder.build();
        }

        context = params[0];


        try {
            return myApiService.getJoke().execute().getData();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (null != mlistner)
            this.mlistner.getTaskResult(result);

    }


    public interface TaskListner {
        void getTaskResult(String result);
    }

    public EndpointsAsyncTask setListner(TaskListner listner) {
        this.mlistner = listner;
        return this;
    }


}