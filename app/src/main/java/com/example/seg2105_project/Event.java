package com.example.seg2105_project;

import java.util.ArrayList;
import java.util.List;

public class Event {

    private int event_id;
    private String title, description, date, start_time, end_time, event_address;
    private List<Attendee> attendeesList;
    private boolean isManualApproval; // New property for approval mode

    public Event(int event_id, String title, String description, String date, String start_time, String end_time, String event_address,boolean isManualApproval, List<Attendee> attendeesList) {
        this.event_id = event_id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.start_time = start_time;
        this.end_time= end_time;
        this.event_address = event_address;
        this.attendeesList = attendeesList != null ? attendeesList : new ArrayList<>();
        this.isManualApproval = isManualApproval;
    }

    public int getEvent_id() {return event_id;}

    public String getTitle() {return title;}

    public String getDescription() {return description;}

    public String getDate() {return date;}

    public String getStart_time() {return start_time;}

    public String getEnd_time() {return end_time;}

    public String getEvent_address() {return event_address;}

    public List<Attendee> getAttendeesList() {return attendeesList;}

    public boolean isManualApproval() { return isManualApproval; }

    // Method to add an attendee based on approval mode
    public void addAttendee(Attendee attendee) {
        if (isManualApproval) {
            attendee.setRegistrationStatus("pending"); // Set status to "pending" in manual mode
        } else {
            attendee.setRegistrationStatus("approved"); // Set status to "approved" in automatic mode
        }
        attendeesList.add(attendee); // Add attendee to the list

    }
    // Method to manually approve an attendee (for manual mode)
    public void approveAttendee(Attendee attendee) {
        if (attendeesList.contains(attendee) && attendee.getRegistrationStatus().equals("pending")) {
            attendee.setRegistrationStatus("approved");
        }
    }

    // Method to reject an attendee (for manual mode)
    public void rejectAttendee(Attendee attendee) {
        if (attendeesList.contains(attendee) && attendee.getRegistrationStatus().equals("pending")) {
            attendee.setRegistrationStatus("rejected");
        }
    }

    // Method to approve all pending attendees (for manual mode)
    public void approveAllAttendees() {
        for (Attendee attendee : attendeesList) {
            if (attendee.getRegistrationStatus().equals("pending")) {
                attendee.setRegistrationStatus("approved");
            }
        }
    }
}


