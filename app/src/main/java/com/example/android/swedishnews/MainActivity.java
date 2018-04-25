package com.example.android.swedishnews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
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
    private String url = "http://content.guardianapis.com/search";
    private static int LOADER_MANAGER_ID = 0;
    StoryAdapter adapter;
    ProgressBar progressBar;
    TextView emptyView;
    String office = "";
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
        //get preferences values from setting
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String query = sharedPreferences.getString(getString(R.string.settings_query_key),getString(R.string.setting_query_default_value));
        boolean filterOffice = sharedPreferences.getBoolean(getString(R.string.settings_filter_office_key),false);
        if (filterOffice){
            office = sharedPreferences.getString(getString(R.string.settings_office_key),getString(R.string.settings_office_UK_value));
        }
        String order_by = sharedPreferences.getString(getString(R.string.settings_order_by_key),getString(R.string.settings_order_by_newest_value));
        String from_time = sharedPreferences.getString(getString(R.string.settings_from_time_key),getString(R.string.settings_from_time_default_value));
        String end_time = sharedPreferences.getString(getString(R.string.settings_end_time_key),getString(R.string.settings_from_time_default_value));

        //build request url
        Uri baseUri = Uri.parse(url);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("format","json");
        uriBuilder.appendQueryParameter("order-by",order_by);
        uriBuilder.appendQueryParameter("page-size","200");
        uriBuilder.appendQueryParameter("show-tags","contributor");
        uriBuilder.appendQueryParameter("q",query);
        uriBuilder.appendQueryParameter("api-key",API_KEY);
        uriBuilder.appendQueryParameter("from-date",from_time);
        uriBuilder.appendQueryParameter("to-date",end_time);
        if (filterOffice){
            uriBuilder.appendQueryParameter("production-office",office);
        }

        return new StoryTaskLoader(this,uriBuilder.toString());

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
