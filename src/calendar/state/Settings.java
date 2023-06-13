package calendar.state;

import calendar.drawing.color.Color;
import calendar.drawing.color.Theme;

// set of settings editable by the dialogue
// stuff that shouldn't just be assumed, such as the Theme
public class Settings {
    private State state;

    private Theme colors;
    private boolean colorfulMonths;
    private int selectedDayColor;

    public Settings(State state) {
        this.state = state;
        this.colors = Theme.Latte;
        this.colorfulMonths = true;
        this.selectedDayColor = 5; // teal 
    }

    public Theme colors() { return this.colors; }
    public boolean colorfulMonths() { return this.colorfulMonths; }
    public Color selectedDayColor() { return this.colors.highlights()[selectedDayColor]; }

    public void toggleColorfulMonths() { this.colorfulMonths = !this.colorfulMonths; state.updateScreen(); }
    public void setColorfulMonths(boolean colorfulMonths) { this.colorfulMonths = colorfulMonths; state.updateScreen(); }
    public void setSelectedDayColor(int index) { this.selectedDayColor = index; state.updateScreen(); }
    public void changeSelectedDayColor(int by) {
        int max = Theme.HIGHLIGHT_COUNT;
        setSelectedDayColor((((selectedDayColor + by) % max) + max) % max);
    }

    public int dayColorIndex() { return this.selectedDayColor; }

    public void setTheme(Theme theme) {
        this.colors = theme; 
        state.screen.month.reinitialize();
        state.updateScreen();
    }
}