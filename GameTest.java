import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GameTest {
    public static Console c;
    public static final char SPACE = ' ';
    public static final char WALL = '#';
    public static final Trap[] TRAPS = new Trap[20];
    public static final Warp[] WARPS = new Warp[20];
    public static final Computer[] COMPS = new Computer[20];
    public static final int[][] MOVING_NUMS = new int[50][2];
    public static final char[] LEGAL_CHARS = { 37, 38, 39, 40, 'W', 'A', 'S', 'D' };
    public static final char[] CANT_MOVE = { '=', '*', WALL, 'C' };
    Object[][] map;
    int timer = 0;

    public GameTest(String title) throws Exception {
        c = new Console(title, 100, 50, 15, 0);
    }

    public void start() {
        map = getRawMap();
        printMap(map);
        game();

    }

    public void game() {
        int millis = 0;
        InputQueue inputs = new InputQueue(15);
        Player player = new Player();
        int coords[] = putPlayerIntoMap('P', map, c);
        putCompIntoMap(map, c);
        printInputQueue(inputs);
        c.setCursorPosition(58, 17);
        c.print("P. Energy :   " + player.getEnergy() + "   ");
        c.setCursorPosition(58, 18);
        c.print("C. Score  :   " + "0");
        c.setCursorPosition(58, 19);
        c.print("P. Life   :   " + player.getHp());
        c.setCursorPosition(58, 20);
        c.print("C. Score  :   " + Computer.getScore());
        c.setCursorPosition(58, 21);
        c.print("Time      :   " + timer + "                 ");
        player.getBackpack().printBackpack();
        for (int i = 0; i < 20; i++) {
            Object temp = inputs.getNextElement();
            putObjectsIntoMap(temp, map, c);
        }
        int key;
        int actionKey = 0;
        boolean isGameOver = false;
        while (!isGameOver) {
            key = c.getKeyPress();
            if (isLegalChar(key)) {
                actionKey = key;
            }
            if (player.getEnergy() != 0) {
                if (millis % 250 == 0) {
                    playerActions(actionKey, coords, map, player);
                    actionKey = 0;
                }
            } else {
                if (millis % 500 == 0) {
                    playerActions(actionKey, coords, map, player);
                    actionKey = 0;
                }
            }
            if (millis % 500 == 0) {
                computerMove(map, coords, player);
                moveNumbers(map);
                c.setCursorPosition(58, 19);
                c.print("P. Life   :   " + player.getHp() + "      ");
                if (player.getHp() == 0) {
                    isGameOver = true;
                }
            }
            delay(10);
            millis += 10;
            if (millis >= 1000) {
                millis = 0;
                timer += 1;
                player.decreaseEnergy(1);
            }
            if (millis == 0) {
                c.setCursorPosition(58, 17);
                c.print("P. Energy :   " + player.getEnergy() + "    ");
                c.setCursorPosition(58, 18);
                c.print("P. Score  :   " + player.getScore() + "      ");
                c.setCursorPosition(58, 19);
                c.print("P. Life   :   " + player.getHp() + "      ");
                c.setCursorPosition(58, 20);
                c.print("C. Score  :   " + Computer.getScore());
                c.setCursorPosition(58, 21);
                c.print("Time      :   " + timer + "                 ");
            }
            if (timer % 3 == 0 && millis == 0) {
                putObjectsIntoMap(inputs.getNextElement(), map, c);
                printInputQueue(inputs);

            }
            if (timer % 1 == 0 && millis == 0) {
                decreaseDeviceDuration(TRAPS, 1);
                decreaseDeviceDuration(WARPS, 1);
                removeExpiredDevices(TRAPS);
                removeExpiredDevices(WARPS);
            }

        }
        c.setCursorPosition(50, 24);
        c.print("GAME OVER!");
        c.setCursorPosition(50, 25);
        c.print("Score : " + (player.getScore() - Computer.getScore()));

    }

    public void delay(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void printMap(Object[][] map) {
        for (int i = 0; i < 23; i++) {
            for (int j = 0; j < 55; j++) {
                c.print(map[i][j]);
            }
            c.println();
        }

    }

    public int[] findClosestCoords(Object[][] map, int coords[], int x, int y) {
        int[] tempArr = { x, y };
        double temp = 999;
        temp = Math.sqrt(Math.pow(coords[0] - x, 2) + Math.pow(coords[1] - y, 2));
        if (map[x + 1][y].toString().charAt(0) != WALL
                && Math.sqrt(Math.pow(coords[0] - (x + 1), 2) + Math.pow(coords[1] - y, 2)) < temp) {
            tempArr[0] = x + 1;
            tempArr[1] = y;
            temp = Math.sqrt(Math.pow(coords[0] - (x + 1), 2) + Math.pow(coords[1] - y, 2));
        }
        if (map[x - 1][y].toString().charAt(0) != WALL
                && Math.sqrt(Math.pow(coords[0] - (x - 1), 2) + Math.pow(coords[1] - y, 2)) < temp) {
            tempArr[0] = x - 1;
            tempArr[1] = y;
            temp = Math.sqrt(Math.pow(coords[0] - (x - 1), 2) + Math.pow(coords[1] - y, 2));
        }
        if (map[x][y + 1].toString().charAt(0) != WALL
                && Math.sqrt(Math.pow(coords[0] - (x), 2) + Math.pow(coords[1] - (y + 1), 2)) < temp) {
            tempArr[0] = x;
            tempArr[1] = y + 1;
            temp = Math.sqrt(Math.pow(coords[0] - (x), 2) + Math.pow(coords[1] - (y + 1), 2));
        }
        if (map[x][y - 1].toString().charAt(0) != WALL
                && Math.sqrt(Math.pow(coords[0] - (x), 2) + Math.pow(coords[1] - (y - 1), 2)) < temp) {
            tempArr[0] = x;
            tempArr[1] = y - 1;
            temp = Math.sqrt(Math.pow(coords[0] - (x), 2) + Math.pow(coords[1] - (y - 1), 2));
        }

        return tempArr;

    }

    public void updateComputerScore(Object[][] map, int x, int y, Player player, int[] coords) {
        if (map[x][y].toString().charAt(0) == '1') {
            Computer.setScore(2);
        } else if (map[x][y].toString().charAt(0) == '2') {
            Computer.setScore(10);
        } else if (map[x][y].toString().charAt(0) == '3') {
            Computer.setScore(30);
        } else if (map[x][y].toString().charAt(0) == '4') {
            Computer.setScore(100);
        } else if (map[x][y].toString().charAt(0) == '5') {
            Computer.setScore(300);
        }
        char[] temp = dropItemsFromBackpack(player, coords);
        for (int i = 0; i < 2; i++) {
            if (temp[i] != 0) {
                if (temp[i] == '1') {
                    Computer.setScore(2);
                } else if (temp[i] == '2') {
                    Computer.setScore(10);
                } else if (temp[i] == '3') {
                    Computer.setScore(30);
                } else if (temp[i] == '4') {
                    Computer.setScore(100);
                } else if (temp[i] == '5' || temp[0] == '*' || temp[0] == '=') {
                    Computer.setScore(300);
                }
            }
        }

    }

    private boolean isCaughtByDevice(Object[] devices, int x, int y) {
        for (int i = 0; i < devices.length; i++) {
            if (devices[i] instanceof Trap) {
                Trap trap = (Trap) devices[i];
                if (trap.hasCaught(x, y)) {
                    return true;
                }
            } else if (devices[i] instanceof Warp) {
                Warp warp = (Warp) devices[i];
                if (warp.hasCaught(x, y)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void computerMove(Object[][] map, int[] coords, Player player) {
        for (int i = 0; i < COMPS.length; i++) {
            if (COMPS[i] != null) {
                int compX = COMPS[i].getX();
                int compY = COMPS[i].getY();

                if (isCaughtByDevice(WARPS, compX, compY)) {
                    COMPS[i] = null;
                    map[compX][compY] = ' ';
                    c.setCursorPosition(compY, compX);
                    c.print(SPACE);
                    player.addScore(300);
                    return;
                }

                int[] temp = findClosestCoords(map, coords, compX, compY);
                updateComputerScore(map, temp[0], temp[1], player, coords);

                boolean hasAttacked = false;
                boolean hasBeenTrapped = isCaughtByDevice(TRAPS, compX, compY);
                if (!hasBeenTrapped && map[temp[0]][temp[1]].toString().charAt(0) == 'P') {
                    player.decreaseHp(1);
                    hasAttacked = true;
                }

                if (!hasBeenTrapped && !hasAttacked && map[temp[0]][temp[1]].toString().charAt(0) != 'C') {
                    if (map[temp[0]][temp[1]].toString().charAt(0) == '4'
                            || map[temp[0]][temp[1]].toString().charAt(0) == '5') {
                        removeNumber(MOVING_NUMS, temp[0], temp[1]);
                    }
                    map[temp[0]][temp[1]] = map[compX][compY];
                    map[compX][compY] = SPACE;
                    c.setCursorPosition(compY, compX);
                    c.print(SPACE);
                    c.setCursorPosition(temp[1], temp[0]);
                    c.print("C", c.redonblack);
                    COMPS[i].setX(temp[0]);
                    COMPS[i].setY(temp[1]);
                }

            }
        }
    }

    public char[] dropItemsFromBackpack(Player player, int[] coords) {
        char[] temp = { 0, 0 };
        for (int i = 0; i < 20; i++) {
            if (COMPS[i] != null) {
                if (COMPS[i].getX() + 1 == coords[0] && COMPS[i].getY() == coords[1]
                        || COMPS[i].getX() - 1 == coords[0] && COMPS[i].getY() == coords[1] ||
                        COMPS[i].getX() == coords[0] && COMPS[i].getY() + 1 == coords[1]
                        || COMPS[i].getX() == coords[0] && COMPS[i].getY() - 1 == coords[1]) {
                    if (player.getBackpack().peekItem() != null) {
                        temp[0] = player.getBackpack().popItem().toString().charAt(0);
                    }
                    if (player.getBackpack().peekItem() != null) {
                        temp[1] = player.getBackpack().popItem().toString().charAt(0);
                    }

                }
            }
        }
        player.getBackpack().printBackpack();
        return temp;
    }

    public boolean canMove(char point) {
        for (char c : CANT_MOVE) {
            if (c == point) {
                return false;
            }
        }

        return true;
    }

    public void playerActions(int input, int[] coords, Object[][] map, Player player) {

        if (coords[0] + 1 <= 22 && input == 40
                && canMove(map[coords[0] + 1][coords[1]].toString().charAt(0))) {
            if (map[coords[0] + 1][coords[1]].toString().charAt(0) == '4'
                    || map[coords[0] + 1][coords[1]].toString().charAt(0) == '5') {
                removeNumber(MOVING_NUMS, coords[0] + 1, coords[1]);
            }
            if (map[coords[0] + 1][coords[1]].toString().charAt(0) == SPACE
                    || player.addBackpackItem(map[coords[0] + 1][coords[1]])) {
                map[coords[0] + 1][coords[1]] = map[coords[0]][coords[1]];
                map[coords[0]][coords[1]] = SPACE;
                c.setCursorPosition(coords[1], coords[0]);
                c.print(SPACE);
                coords[0] += 1;
                c.setCursorPosition(coords[1], coords[0]);
                c.print(map[coords[0]][coords[1]].toString(), c.blueonblack);

            }

        } else if (coords[1] + 1 <= 54 && input == 39 && canMove(map[coords[0]][coords[1] + 1].toString().charAt(0))) {
            if (map[coords[0]][coords[1] + 1].toString().charAt(0) == '4'
                    || map[coords[0]][coords[1] + 1].toString().charAt(0) == '5') {
                removeNumber(MOVING_NUMS, coords[0], coords[1] + 1);
            }
            if (map[coords[0]][coords[1] + 1].toString().charAt(0) == SPACE
                    || player.addBackpackItem(map[coords[0]][coords[1] + 1])) {
                map[coords[0]][coords[1] + 1] = map[coords[0]][coords[1]];
                map[coords[0]][coords[1]] = SPACE;
                c.setCursorPosition(coords[1], coords[0]);
                c.print(SPACE);
                coords[1] += 1;
                c.setCursorPosition(coords[1], coords[0]);
                c.print(map[coords[0]][coords[1]].toString(), c.blueonblack);
            }

        } else if (coords[0] - 1 >= 0 && input == 38 && canMove(map[coords[0] - 1][coords[1]].toString().charAt(0))) {
            if (map[coords[0] - 1][coords[1]].toString().charAt(0) != '4'
                    || map[coords[0] - 1][coords[1]].toString().charAt(0) != '5') {
                removeNumber(MOVING_NUMS, coords[0] - 1, coords[1]);
            }
            if (map[coords[0] - 1][coords[1]].toString().charAt(0) == SPACE
                    || player.addBackpackItem(map[coords[0] - 1][coords[1]])) {
                map[coords[0] - 1][coords[1]] = map[coords[0]][coords[1]];
                map[coords[0]][coords[1]] = SPACE;
                c.setCursorPosition(coords[1], coords[0]);
                c.print(SPACE);
                coords[0] -= 1;
                c.setCursorPosition(coords[1], coords[0]);
                c.print(map[coords[0]][coords[1]].toString(), c.blueonblack);
            }

        } else if (coords[1] - 1 >= 0 && input == 37 && canMove(map[coords[0]][coords[1] - 1].toString().charAt(0))) {
            if (map[coords[0]][coords[1] - 1].toString().charAt(0) == '4'
                    || map[coords[0]][coords[1] - 1].toString().charAt(0) == '5') {
                removeNumber(MOVING_NUMS, coords[0], coords[1] - 1);
            }
            if (map[coords[0]][coords[1] - 1].toString().charAt(0) == SPACE
                    || player.addBackpackItem(map[coords[0]][coords[1] - 1])) {
                map[coords[0]][coords[1] - 1] = map[coords[0]][coords[1]];
                map[coords[0]][coords[1]] = SPACE;
                c.setCursorPosition(coords[1], coords[0]);
                c.print(SPACE);
                coords[1] -= 1;
                c.setCursorPosition(coords[1], coords[0]);
                c.print(map[coords[0]][coords[1]].toString(), c.blueonblack);
            }

        } else if (coords[0] + 1 <= 22 && input == 'S' && map[coords[0] + 1][coords[1]].toString().charAt(0) == SPACE) {
            popItemFromBackpack(player, coords[0] + 1, coords[1]);
        } else if (coords[1] + 1 <= 54 && input == 'D' && map[coords[0]][coords[1] + 1].toString().charAt(0) == SPACE) {
            popItemFromBackpack(player, coords[0], coords[1] + 1);
        } else if (coords[0] - 1 >= 0 && input == 'W' && map[coords[0] - 1][coords[1]].toString().charAt(0) == SPACE) {
            popItemFromBackpack(player, coords[0] - 1, coords[1]);
        } else if (coords[1] - 1 >= 0 && input == 'A' && map[coords[0]][coords[1] - 1].toString().charAt(0) == SPACE) {
            popItemFromBackpack(player, coords[0], coords[1] - 1);
        }

    }

    public void popItemFromBackpack(Player player, int x, int y) {
        if (player.getBackpack().peekItem() != null) {
            if (Backpack.isNumber(player.getBackpack().peekItem())) {
                player.getBackpack().popItem();
                player.getBackpack().printBackpack();
            } else {
                if ((char) player.getBackpack().peekItem() == '=') {
                    Trap trap = new Trap(25, '=', x, y);
                    addTrap(TRAPS, trap);
                    map[x][y] = trap;
                    c.setCursorPosition(y, x);
                    c.print("=", c.redonblack);
                    player.getBackpack().popItem();
                } else {
                    Warp warp = new Warp(25, '*', x, y);
                    addWarp(WARPS, warp);
                    map[x][y] = warp;
                    c.setCursorPosition(y, x);
                    c.print("*", c.redonblack);
                    player.getBackpack().popItem();
                }
            }
        }

        player.getBackpack().printBackpack();
    }

    public static Object[][] getRawMap() {
        Object[][] map = new Object[23][55];
        try {
            File file = new File("map.txt");
            Scanner sc = new Scanner(file);
            int i = 0;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                for (int j = 0; j < 55; j++) {
                    map[i][j] = line.charAt(j);
                }
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return map;
    }

    public void putCompIntoMap(Object[][] map, Console c) {

        while (true) {
            int x = (int) (Math.random() * 22);
            int y = (int) (Math.random() * 54);
            if ((char) map[x][y] == SPACE) {
                Computer obj = new Computer(0, x, y);
                map[x][y] = obj;
                addPC(COMPS, obj);
                c.setCursorPosition(y, x);
                c.print("C", c.redonblack);
                break;
            }
        }
    }

    public int[] putPlayerIntoMap(Object obj, Object[][] map, Console c) {
        int[] coords = { 0, 0 };
        while (true) {
            int x = (int) (Math.random() * 22);
            int y = (int) (Math.random() * 54);
            if ((char) map[x][y] == SPACE) {
                map[x][y] = obj;
                c.setCursorPosition(y, x);
                c.print(obj.toString(), c.blueonblack);
                coords[0] = x;
                coords[1] = y;
                return coords;
            }
        }
    }

    public static void putObjectsIntoMap(Object obj, Object[][] map, Console c) {

        if ((char) obj == '*') {
            while (true) {
                int x = (int) (Math.random() * 22);
                int y = (int) (Math.random() * 54);

                if (map[x][y].toString().charAt(0) == SPACE) {
                    Warp obj1 = new Warp(25, '*', x, y);
                    addWarp(WARPS, obj1);
                    map[x][y] = obj1;
                    c.setCursorPosition(y, x);
                    c.print(obj1.toString(), c.redonblack);
                    break;
                }
            }
        } else if ((char) obj == '=') {
            while (true) {
                int x = (int) (Math.random() * 22);
                int y = (int) (Math.random() * 54);

                if (map[x][y].toString().charAt(0) == SPACE) {
                    Trap obj2 = new Trap(25, '*', x, y);
                    addTrap(TRAPS, obj2);
                    map[x][y] = obj2;
                    c.setCursorPosition(y, x);
                    c.print(obj2.toString(), c.redonblack);
                    break;
                }
            }
        } else if ((char) obj == 'C') {
            while (true) {
                int x = (int) (Math.random() * 22);
                int y = (int) (Math.random() * 54);

                if (map[x][y].toString().charAt(0) == SPACE) {
                    Computer obj3 = new Computer(0, x, y);
                    addPC(COMPS, obj3);
                    map[x][y] = 'C';
                    c.setCursorPosition(y, x);
                    c.print("C", c.redonblack);
                    break;
                }
            }
        } else if ((char) obj == '4' || (char) obj == '5') {
            while (true) {
                int x = (int) (Math.random() * 22);
                int y = (int) (Math.random() * 54);

                if (map[x][y].toString().charAt(0) == SPACE) {
                    addNumber(x, y);
                    map[x][y] = obj.toString().charAt(0);
                    c.setCursorPosition(y, x);
                    String temp = obj.toString();
                    c.print(temp, c.greenonblack);
                    break;
                }
            }
        } else {
            while (true) {
                int x = (int) (Math.random() * 22);
                int y = (int) (Math.random() * 54);

                if (map[x][y].toString().charAt(0) == SPACE) {
                    map[x][y] = obj;
                    c.setCursorPosition(y, x);
                    c.print(obj.toString());
                    break;
                }
            }
        }

    }

    public void printInputQueue(InputQueue inputs) {
        c.setCursorPosition(57, 1);
        c.println("Input");
        c.setCursorPosition(57, 2);
        c.print("<<<<<<<<<<<<<<<");
        c.setCursorPosition(57, 3);
        c.print(inputs.toString());
        c.setCursorPosition(57, 4);
        c.print("<<<<<<<<<<<<<<<");
    }

    public void test() {
        char[] asd = new char[5];
        for (int i = 0; i < 5; i++) {
            c.setCursorPosition(0 + i, 0);
            c.print(asd[i]);
        }
    }

    public boolean isLegalChar(int key) {
        for (int i = 0; i < LEGAL_CHARS.length; i++) {
            if (LEGAL_CHARS[i] == key) {
                return true;
            }
        }
        return false;
    }

    public static boolean addTrap(Trap[] arr, Trap temp) {

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == null) {
                arr[i] = temp;
                return true;
            }
        }
        return false;
    }

    public static boolean addWarp(Warp[] arr, Warp temp) {

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == null) {
                arr[i] = temp;
                return true;
            }
        }
        return false;
    }

    public static boolean addPC(Computer[] arr, Computer temp) {

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == null) {
                arr[i] = temp;
                return true;
            }
        }
        return false;
    }

    public boolean removeObject(Object[] arr, Object temp) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == temp) {
                arr[i] = null;
                return true;
            }
        }
        return false;
    }

    public void decreaseDeviceDuration(Object[] arr, int dur) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != null) {
                if (arr[i] instanceof Trap) {
                    Trap trap = (Trap) arr[i];
                    trap.decreaseDuration(dur);
                } else if (arr[i] instanceof Warp) {
                    Warp warp = (Warp) arr[i];
                    warp.decreaseDuration(dur);
                }
            }
        }
    }

    private void removeExpiredDevices(Object[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != null) {
                if (arr[i] instanceof Trap) {
                    Trap trap = (Trap) arr[i];
                    if (trap.getDuration() <= 0) {
                        int x = trap.getX();
                        int y = trap.getY();
                        arr[i] = null;
                        map[x][y] = SPACE;
                        c.setCursorPosition(y, x);
                        c.print(SPACE);
                    }
                } else if (arr[i] instanceof Warp) {
                    Warp warp = (Warp) arr[i];
                    if (warp.getDuration() <= 0) {
                        int x = warp.getX();
                        int y = warp.getY();
                        arr[i] = null;
                        map[x][y] = SPACE;
                        c.setCursorPosition(y, x);
                        c.print(SPACE);
                    }
                }
            }
        }
    }

    private void moveNumbers(Object[][] map) {
        for (int i = 0; i < 50; i++) {
            int x = MOVING_NUMS[i][0];
            int y = MOVING_NUMS[i][1];
            if (x > 0) {

                if (isCaughtByDevice(WARPS, x, y)) {
                    map[x][y] = SPACE;
                    removeNumber(MOVING_NUMS, x, y);
                    c.setCursorPosition(y, x);
                    c.print(SPACE);

                } else {
                    boolean isCaught = isCaughtByDevice(TRAPS, x, y);
                    boolean flag = false;
                    while (!flag && !isCaught) {
                        int rnd = (int) (Math.random() * 4);
                        if (!isCaught) {
                            if (rnd == 0) {
                                if (map[x + 1][y].toString().charAt(0) == SPACE) {
                                    map[x + 1][y] = map[x][y];
                                    map[x][y] = SPACE;
                                    c.setCursorPosition(y, x);
                                    c.print(SPACE);
                                    x += 1;
                                    c.setCursorPosition(y, x);
                                    c.print(map[x][y].toString(), c.greenonblack);
                                    flag = true;
                                    MOVING_NUMS[i][0] = x;
                                }
                            } else if (rnd == 1) {
                                if (map[x][y + 1].toString().charAt(0) == SPACE) {
                                    map[x][y + 1] = map[x][y];
                                    map[x][y] = SPACE;
                                    c.setCursorPosition(y, x);
                                    c.print(SPACE);
                                    y += 1;
                                    c.setCursorPosition(y, x);
                                    c.print(map[x][y].toString(), c.greenonblack);
                                    flag = true;
                                    MOVING_NUMS[i][1] = y;
                                }
                            } else if (rnd == 2) {
                                if (map[x - 1][y].toString().charAt(0) == SPACE) {
                                    map[x - 1][y] = map[x][y];
                                    map[x][y] = SPACE;
                                    c.setCursorPosition(y, x);
                                    c.print(SPACE);
                                    x -= 1;
                                    c.setCursorPosition(y, x);
                                    c.print(map[x][y].toString(), c.greenonblack);
                                    flag = true;
                                    MOVING_NUMS[i][0] = x;
                                }
                            } else if (rnd == 3) {
                                if (map[x][y - 1].toString().charAt(0) == SPACE) {
                                    map[x][y - 1] = map[x][y];
                                    map[x][y] = SPACE;
                                    c.setCursorPosition(y, x);
                                    c.print(SPACE);
                                    y -= 1;
                                    c.setCursorPosition(y, x);
                                    c.print(map[x][y].toString(), c.greenonblack);
                                    flag = true;
                                    MOVING_NUMS[i][1] = y;
                                }
                            }
                        }

                    }
                }

            }

        }
    }

    private static void addNumber(int x, int y) {
        for (int i = 0; i < 50; i++) {
            if (MOVING_NUMS[i][0] <= 0) {
                MOVING_NUMS[i][0] = x;
                MOVING_NUMS[i][1] = y;
                break;
            }
        }
    }

    private static void removeNumber(int[][] arr, int x, int y) {
        for (int i = 0; i < 50; i++) {
            if (arr[i][0] == x && arr[i][1] == y) {
                arr[i][0] = -1;
                arr[i][1] = -1;
            }
        }
    }
}
