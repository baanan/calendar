package calendar;

import java.util.Calendar;
import java.util.GregorianCalendar;

import calendar.drawing.BoxChars;
import calendar.drawing.layers.Month;

public class Main {
    public static void main(String[] args) {
        // GENERAL IDEAS:
        //
        // calendar will be comprised of a set of passes,
        // one grid for the month
        // another for the days of the month
        // and another for the day grid
        // these grids will be combined in the MultiGrid using Text.combine
        //
        // then, there'll be passes for the numbers, seperate month shading, and tasks
        //
        // this will all be in its own layer - the Month layer
        //
        // then, popups are seperate layers that could go on top
        // and all the layers are packaged in a screen??? not so sure here
        // nah there'll probably be a seperate class that manages the month and all the popups
        // each screen is year-month-day (although the others may be too time-consuming)
        
        // light
        // String background = "\033[48;2;239;241;245m";
        // String foreground = "\033[38;2;230;69;83m";
        // String foreground = "\033[38;2;76;79;105m";
        // dark
        // String background = "\033[48;2;30;30;46m";
        // String foreground = "\033[38;2;243;139;168m";
        // String reset = "\033[0m";

        // Popup popup = new Popup(10, 5);
        // System.out.print(background + foreground);
        // popup.print();
        // System.out.print(reset);
        
        // System.out.print(background + foreground);
        // Grid grid = new Grid(11, 4, 5, 5, Justification.Middle);
        // grid.setValue(1, 1, "Bello");
        // grid.draw()
        //     .merge(30, 7, Canvas.rectangle(13, 7, false).drawText("Bello", 4, 3))
        //     .print();
        // System.out.print(reset);


        GregorianCalendar calendar = new GregorianCalendar(2023, Calendar.MAY, 1);

        Month month = new Month(11, 4, calendar);
        month.print();
    }
}
