package calendar.input.layer;

import calendar.drawing.color.Theme;
import calendar.input.InputLayer;
import calendar.input.Key;
import calendar.input.LayerChange;
import calendar.input.LayerType;
import calendar.state.Config;
import calendar.state.State;
import calendar.state.layer.SelectionsLayer;

// the input layer for the preferences dialogue
// it is the thing that actually changes the preferences
public class PreferencesInputLayer implements InputLayer {
    private State state;
    private SelectionsLayer selector;

    public PreferencesInputLayer(State state, SelectionsLayer selector) {
        this.state = state;
        this.selector = selector;
        selector.setBounds(0, 7);
    }

    private int line() { return selector.selection(); }
    private Config config() { return state.config; }

    public LayerChange handle(Key character) {
        if(character.toChar() == 'q' || character.toChar() == 'p') return LayerChange.exit();

        // (q) exit
        if(character.toChar() == 'q') 
            return LayerChange.exit();

        // (enter) edit
        if(character.isEnter()) {
            switch(line()) {
                case 0: switchTheme(Theme.Latte); break;
                case 1: switchTheme(Theme.Frappe); break;
                case 2: switchTheme(Theme.Macchiato); break;
                case 3: switchTheme(Theme.Mocha); break;
                case 4: switchTheme(Theme.Transparent); break;
                case 5: config().toggleColorfulMonths(); break;
                case 7: config().toggleDrawEventTimes(); break;
            }
        }

        if(character.isUp()) 
            selector.prevSelection();
        else if(character.isDown()) 
            selector.nextSelection();
        else if(character.isLeft() && line() == 6)
            config().changeSelectedDayColor(-1);
        else if(character.isRight() && line() == 6)
            config().changeSelectedDayColor(1);

        return LayerChange.keep();
    }

    private void switchTheme(Theme theme) {
        config().setTheme(theme);
    }

    public LayerType type() { return LayerType.Popup; }
}
