public class Trap {
    private int duration;
    private char body;
    private int x;
    private int y;

    public Trap(int duration, char body, int x, int y) {
        this.duration = duration;
        this.body = body;
        this.x = x;
        this.y = y;
    }

    public Trap(int duration, int x, int y) {
        this(25, '=', x, y);
    }

    public String toString() {
        return "=";
    }

    public boolean hasCaught(int targetX, int targetY) {

        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (targetX == i && targetY == j) {
                    return true;
                }

            }
        }
        return false;
    }

    public int getDuration() {
        return duration;
    }

    public void decreaseDuration(int dur) {
        this.duration -= dur;
    }

    public int getX() {
        return x;
    }

    public char getBody() {
        return body;
    }

    public void setBody(char body) {
        this.body = body;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

}
