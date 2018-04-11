package com.example.android.swedishnews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class StoryAdapter extends ArrayAdapter<Story> {
    Context context;
    public StoryAdapter(@NonNull Context context, @NonNull ArrayList<Story> objects) {
        super(context, R.layout.story_item, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View storyItemView = convertView;
        if (storyItemView == null){
            storyItemView = LayoutInflater.from(context).inflate(R.layout.story_item,parent,false);
        }

        //get views for story item view
        TextView titleView = (TextView) storyItemView.findViewById(R.id.story_title);
        TextView contributorView = (TextView) storyItemView.findViewById(R.id.contributor);
        TextView dateView = (TextView) storyItemView.findViewById(R.id.publish_date);
        TextView timeView = (TextView) storyItemView.findViewById(R.id.publish_time);
        TextView sectionNameView = (TextView) storyItemView.findViewById(R.id.section);

        //set different views' contents
        Story currentStory = getItem(position);
        titleView.setText(currentStory.getStory_title());
        contributorView.setText(context.getString(R.string.by) + " "+currentStory.getStory_contributor());
        dateView.setText(currentStory.getStory_date());
        timeView.setText(currentStory.getStory_time());
        sectionNameView.setText(currentStory.getStory_section_name());

        return storyItemView;
    }
}
