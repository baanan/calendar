package calendar.input.layer;

import java.time.LocalDate;

import calendar.input.InputLayer;
import calendar.input.Key;
import calendar.input.LayerChange;
import calendar.input.LayerType;
import calendar.state.State;

// the input layer for the month screen
// handles moving throughout the month, or opening popups
public class MonthInputLayer implements InputLayer {
    private State state;

    private int day;

    private LocalDate date() { return state.date(); }
    private int maxDay() { return date().lengthOfMonth(); }

    public MonthInputLayer(State state) {
        this.state = state;
        this.day = date().getDayOfMonth();
    }

    public LayerChange handle(Key character) {
        if(character.toChar() == 'q') return LayerChange.exit();

        switch(character.toChar()) {
            case 'a':
                return LayerChange.switchTo(state.showEventPopup());
            case 's':
                return LayerChange.switchTo(state.showSectionPopup());
            case '?':
                return LayerChange.switchTo(state.showStandaloneHelpPopup(help));
            case 'p':
                return LayerChange.switchTo(state.showPreferencesPopup());
            case 'r':
                return removeEvents();
        }

        if(character.isUp() && day > 7)
            changeDate(-7);
        else if(character.isDown() && day <= maxDay() - 7)
            changeDate(7);
        else if(character.isLeft() && day > 1)
            changeDate(-1);
        else if(character.isRight() && day < maxDay())
            changeDate(1);
        else if(character.isShiftLeft())
            changeMonth(-1);
        else if(character.isShiftRight())
            changeMonth(1);

        return LayerChange.keep(); // for now
    }

    private void changeDate(int by) {
        day += by;
        state.setDate(date().withDayOfMonth(day));
    }

    private void changeMonth(int by) {
        state.changeMonth(by);
        day = 1;
    }

    private LayerChange removeEvents() {
        state.calendar.removeEventsOnDay(date());
        return LayerChange.keep();
    }

    private static String[] help = new String[]{
        "(←,↓,↑,→) (h,j,k,l)", 
        "  move between days",
        "",
        "(H) (shift-left)", 
        "  go to last month",
        "(L) (shift-right)", 
        "  go to next month",
        "",
        "(p) preferences",
        "(s) manage sections",
        "(a) add event",
        "(r) remove events",
    };

    public LayerType type() { return LayerType.Screen; }
}
