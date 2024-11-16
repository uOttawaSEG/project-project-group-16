package com.example.seg2105_project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.List;

public class SearchEventAdapter extends ArrayAdapter<Event> {

    private Context context;
    private List<Event> events;

    public SearchEventAdapter(Context context, List<Event> events) {
        super(context, R.layout.list_event_search_by_attendees, events);
        this.context = context;
        this.events = events;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        SearchEventAdapter.ViewHolder holder;

        if (listItem == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            listItem = inflater.inflate(R.layout.list_event_search_by_attendees, parent, false);

            holder = new SearchEventAdapter.ViewHolder();
            holder.eventTitle = listItem.findViewById(R.id.eventTitle);
            holder.eventDate = listItem.findViewById(R.id.eventDate);
            holder.eventStartTime = listItem.findViewById(R.id.eventStartTime);
            holder.eventEndTime = listItem.findViewById(R.id.eventEndTime);
            holder.viewMoreInfo = listItem.findViewById(R.id.viewMoreInfo);

            listItem.setTag(holder);
        } else {
            holder = (SearchEventAdapter.ViewHolder) listItem.getTag();
        }

        Event currentEvent = events.get(position);

        holder.eventTitle.setText(currentEvent.getTitle());
        holder.eventDate.setText("Date: " + currentEvent.getDate());
        holder.eventStartTime.setText("From: " + currentEvent.getStart_time());
        holder.eventEndTime.setText("To: " + currentEvent.getEnd_time());

        // Set up info button click listener
        holder.viewMoreInfo.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, ViewSpecificEventAsAttendees.class);
            intent.putExtra("event_id", currentEvent.getEvent_id());
            context.startActivity(intent);
        });


        return listItem;
    }

    private static class ViewHolder {
        TextView eventTitle;
        TextView eventDate;
        TextView eventStartTime;
        TextView eventEndTime;
        Button viewMoreInfo;
    }

}
