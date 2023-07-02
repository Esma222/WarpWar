import enigma.core.Enigma;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import enigma.console.TextAttributes;
import java.awt.Color;
import java.util.Scanner;

public class Console {
    // private static Scanner sc = new Scanner(System.in);
    private enigma.console.Console c;
    private KeyListener klis;
    private int keypr; // key pressed?
    private int rkey;
    private int WINX;
    private int WINY;

    public static final TextAttributes greenonblack = new TextAttributes(Color.GREEN);
    public static final TextAttributes redonblack = new TextAttributes(Color.RED);
    public static final TextAttributes blackongreen = new TextAttributes(Color.BLACK, Color.GREEN);
    public static final TextAttributes blackonred = new TextAttributes(Color.BLACK, Color.RED);
    public static final TextAttributes blueonblack = new TextAttributes(Color.BLUE);

    public Console(String title, int xSize, int ySize, int fontSize, int fontNo) throws Exception {
        c = Enigma.getConsole(title, xSize, ySize, fontSize, fontNo);
        WINX = xSize;
        WINY = ySize;
        setup();
    }

    public void setup() throws Exception { // --- Contructor
        // ------ Standard code for mouse and keyboard ------ Do not change
        klis = new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
                if (keypr == 0) {
                    keypr = 1;
                    rkey = e.getKeyCode();
                }
            }

            public void keyReleased(KeyEvent e) {
            }
        };
        c.getTextWindow().addKeyListener(klis);
        // ----------------------------------------------------
    }

    public int getKeyPress() {

        if (keypr == 1) { // if keyboard button pressed
            keypr = 0;
            return rkey;
        }
        return 0;
    }

    public int getKeyPressPause() {
        int key = getKeyPress();
        while (key == 0) {
            key = getKeyPress();
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {

            }
        }
        return key;
    }

    public void println() {
        c.getTextWindow().output("\n");
    }

    public void println(String str) {
        c.getTextWindow().output(str + "\n");
    }

    public void println(String str, TextAttributes t) {
        c.getTextWindow().output(str + "\n", t);
    }

    public void print(String str) {
        c.getTextWindow().output(str);
    }

    public void print(String str, TextAttributes t) {
        c.getTextWindow().output(str, t);
    }

    public void print(Object o) {
        c.getTextWindow().output(o.toString());
    }

    public void setCursorPosition(int x, int y) {
        c.getTextWindow().setCursorPosition(x, y);
    }

    public void clear() {
        for (int i = 0; i < WINY - 1; i++) {
            for (int j = 0; j < WINX; j++) {
                setCursorPosition(j, i);
                print(" ");
            }
        }
        setCursorPosition(0, 0);
    }

    public void clear(int x1, int y1, int x2, int y2) {
        for (int i = y1; i <= y2; i++) {
            for (int j = x1; j <= x2; j++) {
                setCursorPosition(j, i);
                print(" ");
            }
        }
    }

    public String readLine() {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        sc.close();
        return s;
    }

}