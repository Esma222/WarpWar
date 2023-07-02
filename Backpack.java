public class Backpack {

    private int size = 8;
    private Stack backpack;

    public Backpack() {
        backpack = new Stack(size);
    }

    public Object popItem() {
        if (backpack.isEmpty()) {
            return null;
        }
        return backpack.pop();
    }

    public void pushItem(Object obj) {
        backpack.push(obj);

    }

    public Object peekItem() {
        if (backpack.isEmpty()) {
            return null;
        }
        return backpack.peek();
    }

    public char addItem(Object obj) {
        if (obj.toString().charAt(0) == ' ') {
            return 'f';
        }
        if (obj.toString().charAt(0) == '1') {
            return 't';
        }
        if (backpack.isEmpty() || backpack.peek() == null) {
            backpack.push(obj);
            return 't';
        }

        else if (!backpack.isFull()) {
            if (backpack.peek().toString().charAt(0) != obj.toString().charAt(0)) {
                if (!(isNumber(backpack.peek()) && isNumber(obj))) {
                    backpack.push(obj);
                    return 't';
                } else {
                    backpack.pop();
                    return 't';
                }
            } else {
                if (isNumber(obj)) {
                    if (obj.toString().charAt(0) == '1') {
                        backpack.pop();
                        return 't';
                    }
                    return obj.toString().charAt(0);
                } else
                    backpack.push(obj);
                return 't';

            }

        } else if (backpack.peek().toString().charAt(0) != obj.toString().charAt(0) && backpack.isFull()) {
            return 'f';
        }
        return 'f';

    }

    public static boolean isNumber(Object obj) {
        char temp = obj.toString().charAt(0);
        if (temp > '0' && temp < '6') {
            return true;
        } else {
            return false;
        }
    }

    public void printBackpack() {
        Console c = GameTest.c;
        Stack temp = new Stack(size);

        c.setCursorPosition(61, 7);
        c.print("|     |");
        c.setCursorPosition(61, 8);
        c.print("|     |");
        c.setCursorPosition(61, 9);
        c.print("|     |");
        c.setCursorPosition(61, 10);
        c.print("|     |");
        c.setCursorPosition(61, 11);
        c.print("|     |");
        c.setCursorPosition(61, 12);
        c.print("|     |");
        c.setCursorPosition(61, 13);
        c.print("|     |");
        c.setCursorPosition(61, 14);
        c.print("|     |");
        c.setCursorPosition(61, 15);
        c.print("+-----+");

        int j = 14;
        while (!backpack.isEmpty()) {
            temp.push(backpack.pop());

        }
        while (!temp.isEmpty()) {
            c.setCursorPosition(64, j);
            c.print(temp.peek());
            backpack.push(temp.pop());
            j--;

        }

    }

}
