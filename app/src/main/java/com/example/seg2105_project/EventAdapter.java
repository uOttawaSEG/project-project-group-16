package com.example.seg2105_project;

import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

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
            holder.deleteButton = listItem.findViewById(R.id.deleteEventButton);

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

        // Set up delete button click listener
        holder.deleteButton.setOnClickListener(v -> {
            // Show confirmation dialog
            new AlertDialog.Builder(context)
                    .setTitle("Confirm Deletion")
                    .setMessage("Are you sure you want to delete this event?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Call method to delete event
                        ((UpcomingEventsOverview) context).deleteEvent(currentEvent.getTitle());
                        events.remove(position); // Remove event from the list
                        notifyDataSetChanged(); // Refresh the list
                        Toast.makeText(context, "Event deleted", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        return listItem;
    }

    private static class ViewHolder {
        TextView eventTitle;
        TextView eventDescription;
        TextView eventDate;
        TextView eventStartTime;
        TextView eventEndTime;
        TextView eventAddress;
        Button deleteButton;
    }

}
