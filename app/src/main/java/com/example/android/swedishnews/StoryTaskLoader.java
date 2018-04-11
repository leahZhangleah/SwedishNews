package com.example.android.swedishnews;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

public class StoryTaskLoader extends AsyncTaskLoader<ArrayList<Story>> {
    String urlToRequest;

    public StoryTaskLoader(Context context, String url){
        super(context);
        urlToRequest = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Story> loadInBackground() {
        if (urlToRequest == null){
            return null;
        }
        ArrayList<Story> stories = QueryUtils.fetchDataByURL(urlToRequest);
        return stories;
    }
}
