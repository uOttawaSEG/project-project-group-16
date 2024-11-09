package com.example.seg2105_project;

import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;

public class Event {

    private String title, description, date, start_time, end_time, event_address;
    private List<String> attendeesList;

    public Event(String title, String description, String date, String start_time, String end_time, String event_address, List<String> attendeesList) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.start_time = start_time;
        this.end_time= end_time;
        this.event_address = event_address;
        this.attendeesList = attendeesList;
    }

    public String getTitle() {return title;}

    public String getDescription() {return description;}

    public String getDate() {return date;}

    public String getStart_time() {return start_time;}

    public String getEnd_time() {return end_time;}

    public String getEvent_address() {return event_address;}

    public List<String> getAttendeesList() {return attendeesList;}


}
