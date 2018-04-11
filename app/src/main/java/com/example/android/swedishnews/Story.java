package com.example.android.swedishnews;

public class Story {
    private String story_title;
    private String story_contributor;
    private String story_date;
    private String story_time;
    private String story_section_name;
    private String story_url;

    public Story(String story_title, String story_contributor, String story_date, String story_time, String story_section_name, String story_url) {
        this.story_title = story_title;
        this.story_contributor = story_contributor;
        this.story_date = story_date;
        this.story_time = story_time;
        this.story_section_name = story_section_name;
        this.story_url = story_url;
    }

    public String getStory_title() {
        return story_title;
    }

    public String getStory_contributor() {
        return story_contributor;
    }

    public String getStory_date() {
        return story_date;
    }

    public String getStory_section_name() {
        return story_section_name;
    }

    public String getStory_time() {
        return story_time;
    }

    public String getStory_url() {
        return story_url;
    }
}
