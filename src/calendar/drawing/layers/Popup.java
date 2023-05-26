package calendar.drawing.layers;

import calendar.drawing.Canvas;
import calendar.drawing.Drawable;
import calendar.drawing.color.Theme;
import calendar.state.State;

public class Popup implements Drawable {
    private final int width;
    private final int height = 18;

    protected State state;

    public Popup(int width, State state) {
        this.width = width;

        this.state = state;
    }

    public Theme colors() { return state.colors(); }

    public Canvas draw() {
        return Canvas.rectangle(width, height, true, colors().text(), colors().background());
    }

    public int width() { return this.width; }
    public int height() { return this.height; }
}
