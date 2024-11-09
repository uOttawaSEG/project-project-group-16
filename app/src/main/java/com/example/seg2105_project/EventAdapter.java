package com.example.seg2105_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {

    private Context context;
    private List<Event> events;

    public EventAdapter(Context context, List<Event> events) {
        super(context, R.layout.list_events_item, events);
        this.context = context;
        this.events = events;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        ViewHolder holder;

        if (listItem == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            listItem = inflater.inflate(R.layout.list_events_item, parent, false);

            holder = new ViewHolder();
            holder.eventTitle = listItem.findViewById(R.id.eventTitle);
            holder.eventDescription = listItem.findViewById(R.id.eventDescription);
            holder.eventDate = listItem.findViewById(R.id.eventDate);
            holder.eventStartTime = listItem.findViewById(R.id.eventStartTime);
            holder.eventEndTime = listItem.findViewById(R.id.eventEndTime);
            holder.eventAddress = listItem.findViewById(R.id.eventAddress);

            listItem.setTag(holder);
        } else {
            holder = (ViewHolder) listItem.getTag();
        }

        Event currentEvent = events.get(position);

        holder.eventTitle.setText(currentEvent.getTitle());
        holder.eventDescription.setText(currentEvent.getDescription());
        holder.eventDate.setText("Date: " + currentEvent.getDate());
        holder.eventStartTime.setText("From: " + currentEvent.getStart_time());
        holder.eventEndTime.setText("To: " + currentEvent.getEnd_time());
        holder.eventAddress.setText("Address : " + currentEvent.getEvent_address());

        return listItem;
    }

    private static class ViewHolder {
        TextView eventTitle;
        TextView eventDescription;
        TextView eventDate;
        TextView eventStartTime;
        TextView eventEndTime;
        TextView eventAddress;
    }

}
