package calendar.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import calendar.state.State;

// stores all the data of a calendar
// it maintains a list of sections, who each have their own lists of events
// it also provides methods to add or remove sections and events
public class Calendar implements Serializable {
    private ArrayList<Section> sections = new ArrayList<>();

    private transient State state;

    public Calendar(State state) {
        this.state = state;
    }

    public State state() { return this.state; }
    public List<Section> sections() { return this.sections; }

    public Section createDefaultSection() {
        return new Section(this, "Add Section", 0, state);
    }

    public Section addSection(String title, int color) {
        Section section = new Section(this, title, color, state);
        sections.add(section);
        return section;
    }

    public void removeSection(int index) {
        this.sections.remove(index);
        state.updateScreen();
    }


    public EditingEvent createDefaultEvent() {
        return new EditingEvent(this);
    }

    public Event addEvent(Section section, String name, LocalDateTime start, LocalDateTime end) {
        Event event = new Event(this, section, name, start, end);
        section.add(event);
        state().updateScreen();
        return event;
    }

    public Event addEvent(Section section, Event event) {
        event.moveTo(section);
        section.add(event);
        state().updateScreen();
        return event;
    }

    public void addEvent(EditingEvent event) {
        Event toAdd = event.toEvent();
        if(toAdd == null) 
            state.displayError(EditingEvent.EVENT_MAKE_ERROR);
        else 
            toAdd.section().add(toAdd);

    }
    
    public void moveEvent(Event event, Section to) {
        event.section().remove(event);
        addEvent(to, event);
    }


    public Stream<Event> eventsStream() {
        // java iterators
        // thought i'd never see it
        return sections.stream()
            .map(section -> section.events())
            .flatMap(Collection::stream)
            .sorted();
    }

    public List<Event> events() {
        return eventsStream()
            .collect(Collectors.toList());
    }

    public List<Event> eventsInMonth(LocalDate date) {
        return eventsStream()
            .filter(event -> event.inMonth(date))
            .collect(Collectors.toList());
    }

    public void removeEventsOnDay(LocalDate date) {
        for(Section section : sections) {
            List<Event> events = section.events();

            for(int i = 0; i < events.size(); i++) {
                Event event = events.get(i);
                if(event.isOnDay(date)) {
                    events.remove(i);
                    i--;
                }
            }
        }

        state.updateScreen();
    }

    public List<Event> eventsInCurrentMonth() { return eventsInMonth(state().date()); }

    // populate after deserialization
    protected void populate(State state) {
        this.state = state;

        for(Section section : sections)
            section.populate(this, state);
    }

    public static Calendar deserialize(String file, State state) {
        if(new File(file).exists()) {
            try {
                try(FileInputStream fileInput = new FileInputStream(file)) {
                    try(ObjectInputStream objectInput = new ObjectInputStream(fileInput)) {
                        Calendar calendar = (Calendar) objectInput.readObject();
                        calendar.populate(state);
                        return calendar;
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        // TODO: some logging or something
        return new Calendar(state);
    } 

    public void serialize(String file) {
        try {
            try(FileOutputStream fileOut = new FileOutputStream(file)) {
                try(ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
                    out.writeObject(this);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
} 
