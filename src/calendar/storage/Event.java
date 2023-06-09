package calendar.storage;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import calendar.state.State;

// represents a simple event, with a title, start, end, and section
public class Event implements Comparable<Event>, Serializable {
    private String title;

    private LocalDateTime start;
    private LocalDateTime end;

    private transient Section section;
    private transient Calendar calendar;

    public Event(Calendar calendar, Section section, String title, LocalDateTime start, LocalDateTime end) {
        this.title = title;

        this.start = start;
        this.end = end;

        this.calendar = calendar;
        this.section = section;
    }

    public String title() { return title; }
    public void setTitle(String title) { this.title = title; calendar.state().updateScreen(); }

    public LocalDateTime start() { return start; }
    public void setStart(LocalDateTime start) { this.start = start; calendar.state().updateScreen(); }

    public LocalDateTime end() { return end; }
    public void setEnd(LocalDateTime end) { this.end = end; calendar.state().updateScreen(); }

    public Section section() { return section; }
    // screen will get updated elsewhere
    protected void moveTo(Section section) { this.section = section; }


    public String displayTitle(State state) {
        String display = title;

        if(state.config.drawEventTimes())
            display = displayStart() + ": " + display;

        return display;
    }

    private String displayStart() {
        int hour = start.getHour();

        char ampm = 'A';
        if(hour >= 12) {
            ampm = 'P';
            if(hour > 12)
                hour -= 12;
        }

        return hour + "" + ampm;
    }


    // comparing is done through the starts
    public int compareTo(Event other) { return this.start.compareTo(other.start); }

    private boolean sameMonth(LocalDateTime a, LocalDate b) { return a.getYear() == b.getYear() && a.getMonth() == b.getMonth(); }

    public boolean inMonth(LocalDate time) {
        // LocalDateTime monthStart = time.withDayOfMonth(1).atTime(0, 0);
        // LocalDateTime monthEnd = time.withDayOfMonth(time.lengthOfMonth()).atTime(23, 59);
        // return start.isBefore(monthEnd) && end.isAfter(monthStart);
        LocalDate month = time.withDayOfMonth(1);
        return sameMonth(start, month) || sameMonth(end, month);
    }

    public boolean isOnDay(LocalDate date) {
        return this.start.toLocalDate().equals(date);
    }

    // populate after deserialization
    protected void populate(Calendar calendar, Section section) {
        this.calendar = calendar;
        this.section = section;
    }
} 
