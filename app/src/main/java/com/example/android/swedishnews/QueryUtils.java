package com.example.android.swedishnews;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getName();

    private QueryUtils(){}

    public static ArrayList<Story> fetchDataByURL(String urlToRequest){
        URL url;
        String jsonResponse = "";
        ArrayList<Story> stories = new ArrayList<>();

        url = createUrl(urlToRequest);
        try{
            jsonResponse = makeHttpRequest(url);
        }catch(IOException e){

            Log.e(LOG_TAG, "Error in making http request" + e.getMessage());
        }
        stories = extractDataFromString(jsonResponse);

        return stories;
    }

    //get URL from string
    private static URL createUrl(String url){
        URL urlToRequest = null;
        if (url == null){
            return null;
        }
        try{
            urlToRequest = new URL(url);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG,"Malformed URL exception: " + e.getMessage());
        }
        return urlToRequest;
    }

    //make http request based on URL
    private static String makeHttpRequest(URL urlToRequest) throws IOException{
        //check if url is null. if it is, directly return null without doing further ulr connection request
        if (urlToRequest == null){
            return null;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        String jsonResponse = "";
        try{
            urlConnection = (HttpURLConnection) urlToRequest.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(15000);
            urlConnection.setReadTimeout(10000);
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            jsonResponse = convertFromStreamToString(inputStream);
        }finally{
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    //convert inputstream into String
    private static String convertFromStreamToString(InputStream inputStream){
        //check if inputstream is null. if it is, directly return null without doing further inputstream reading
        if (inputStream == null){
            return null;
        }
        InputStreamReader reader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuilder builder = new StringBuilder();
        String line;
        try{
            while ((line = bufferedReader.readLine()) != null){
                builder.append(line);
            }
        }catch (IOException e){
            Log.e(LOG_TAG,"Error reading inputstream" + e.getMessage());
        }
        return builder.toString();
    }

    //convert string into arraylist to supply data for adapter
    private static ArrayList<Story> extractDataFromString(String jsonResponse){
        if (jsonResponse == null){
            return null;
        }
        ArrayList<Story> stories = new ArrayList<>();
        try{
            JSONObject root = new JSONObject(jsonResponse);
            JSONObject object = root.getJSONObject("response");
            JSONArray results = object.getJSONArray("results");
            for (int i= 0; i < results.length(); i++){
                JSONObject result = (JSONObject) results.get(i);
                String story_title = result.getString("webTitle");
                String story_section = result.getString("sectionName");
                String story_url = result.getString("webUrl");
                JSONArray tags = result.getJSONArray("tags");
                JSONObject contributor_tag = (JSONObject) tags.get(0);
                String story_contributor = contributor_tag.getString("webTitle");
                String date = result.getString("webPublicationDate");

                //format date
                String formattedDate = formatDate(date);
                String story_date = formattedDate.substring(0,7);
                String story_time = formattedDate.substring(7,formattedDate.length());

                stories.add(new Story(story_title,story_contributor,story_date,story_time,story_section,story_url));
            }
        }catch (JSONException e){
            Log.e(LOG_TAG,"Error in parsing json" + e.getMessage());
        }
        return stories;
    }

    private static String formatDate(String date){
        String dateFormatted = "";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try{
            Date result = dateFormat.parse(date);
            SimpleDateFormat formatter = new SimpleDateFormat("MMM dd HH:mm");
            dateFormatted = formatter.format(result);
        }catch(ParseException e){
            Log.e(LOG_TAG,"error in parsing date format");
        }
        return dateFormatted;
    }

}
