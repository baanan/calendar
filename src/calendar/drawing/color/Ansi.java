package calendar.drawing.color;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.util.Scanner;
import java.util.regex.Pattern;

import calendar.util.Coord;

public class Ansi {
    public static final String ESC = "\033";
    public static final String RESET = ESC + "[0m";

    public static final String FOREGROUND = ESC + "[38;2;%d;%d;%dm";
    public static final String FOREGROUND_RESET = ESC + "[39m";

    public static final String BACKGROUND = ESC + "[48;2;%d;%d;%dm";
    public static final String BACKGROUND_RESET = ESC + "[49m";

    public static final String CLEAR_SCREEN = ESC + "[2J" + ESC + "[H";
    public static final String MOVE = ESC + "[%d;%dH";

    public static void color(StringBuilder builder, char character, Color foreground, Color background) {
        if(foreground != null)
            builder.append(String.format(FOREGROUND, foreground.r, foreground.g, foreground.b));
        else builder.append(FOREGROUND_RESET);

        if(background != null)
            builder.append(String.format(BACKGROUND, background.r, background.g, background.b));
        else builder.append(BACKGROUND_RESET);

        builder.append(character);
    }

    private static final String GET_CURSOR = ESC + "[6n";

    // you gotta do some cursed stuff for this
    public static Coord getDimensions() {
        // turn off canonical mode on unix
        File f = new File("/bin/stty");
        if(f.exists() && f.canExecute()) {
            ProcessBuilder pb = new ProcessBuilder("/bin/stty", "-icanon");
            pb.redirectInput(Redirect.from(new File("/dev/tty")));
            try { 
                pb.start(); 
            } catch(IOException e) { e.printStackTrace(); }
        }

        // move the furthest possible and ask for cursor position
        System.out.print(String.format(MOVE, 1000, 1000) + GET_CURSOR);

        // read the output position
        Scanner scanner = new Scanner(System.in).useDelimiter("R");

        // will output like ESC[row;columnR
        String pos = scanner.next();
        int square = pos.indexOf('[');
        int semi = pos.indexOf(';');

        scanner.close();

        // deserialize
        int row = Integer.parseInt(pos.substring(square + 1, semi));
        int column = Integer.parseInt(pos.substring(semi + 1));

        return new Coord(column, row);
    }
}
