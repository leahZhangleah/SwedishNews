package com.example.android.swedishnews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Story>> {
    private ArrayList<Story> stories = new ArrayList<>();
    private static final String API_KEY = "6b0aad99-4908-4a0e-94a3-770be261d469";
    private static final String URL_TO_REQUEST_CONTENT = "http://content.guardianapis.com/search?format=json&from-date=2018-01-01&order-by=newest&page-size=200&show-tags=contributor&q=sweden&api-key=6b0aad99-4908-4a0e-94a3-770be261d469";
    private static int LOADER_MANAGER_ID = 0;
    StoryAdapter adapter;
    ProgressBar progressBar;
    TextView emptyView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.list_view);
        adapter = new StoryAdapter(this,stories);
        listView.setAdapter(adapter);
        emptyView = (TextView) findViewById(R.id.empty_text_view);
        listView.setEmptyView(emptyView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String currentStoryUrl = stories.get(position).getStory_url();
                if (currentStoryUrl != null){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentStoryUrl));
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),getString(R.string.invalid_story_url),Toast.LENGTH_SHORT).show();
                }
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //check if there is internet connection
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = info != null && info.isConnectedOrConnecting();
        if (isConnected){
            getLoaderManager().initLoader(LOADER_MANAGER_ID,null,this);
        }else {
            progressBar.setVisibility(View.GONE);
            emptyView.setText(getString(R.string.no_internet));
        }

    }

    @Override
    public Loader<ArrayList<Story>> onCreateLoader(int id, Bundle args) {
        return new StoryTaskLoader(this,URL_TO_REQUEST_CONTENT);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Story>> loader, ArrayList<Story> data) {
        adapter.clear();
        progressBar.setVisibility(View.GONE);
        if (data != null && !data.isEmpty()){
            adapter.addAll(data);
        }else{
            emptyView.setText(getString(R.string.no_news));
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Story>> loader) {
        adapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_settings){
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
