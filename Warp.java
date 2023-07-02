public class Warp {
    public int duration;
    private char body;
    private int x;
    private int y;

    public Warp(int duration, char body, int x, int y) {
        this.duration = duration;
        this.body = body;
        this.x = x;
        this.y = y;
    }

    public Warp(int duration, int x, int y) {
        this(25, '*', x, y);
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

    public String toString() {
        return "*";
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
