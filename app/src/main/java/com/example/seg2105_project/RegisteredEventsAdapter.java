package com.example.seg2105_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RegisteredEventsAdapter extends RecyclerView.Adapter<RegisteredEventsAdapter.ViewHolder> {

    private Context context;
    private List<Event> events;

    public RegisteredEventsAdapter(Context context, List<Event> events) {
        this.context = context;
        this.events = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_registered_events_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = events.get(position);


        holder.eventTitle.setText(event.getTitle());
        holder.eventDescription.setText(event.getDescription());
        holder.eventDate.setText("Date: " + event.getDate());
        holder.eventStartTime.setText("Start: " + event.getStart_time());
        holder.eventEndTime.setText("End: " + event.getEnd_time());
        holder.eventAddress.setText("Address: " + event.getEvent_address());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventTitle, eventDescription, eventDate, eventStartTime, eventEndTime, eventAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTitle = itemView.findViewById(R.id.eventTitle);
            eventDescription = itemView.findViewById(R.id.eventDescription);
            eventDate = itemView.findViewById(R.id.eventDate);
            eventStartTime = itemView.findViewById(R.id.eventStartTime);
            eventEndTime = itemView.findViewById(R.id.eventEndTime);
            eventAddress = itemView.findViewById(R.id.eventAddress);
        }
    }
}
