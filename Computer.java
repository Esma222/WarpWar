public class Computer {
    static int score;
    private int x;
    private int y;
    static char body;

    public Computer(int score, int x, int y) {
        this.score = score;
        this.x = x;
        this.y = y;
        this.body = 'C';
    }

    public static int getScore() {
        return score;
    }

    public char getBody() {
        return body;
    }

    public void setBody(char body) {
        this.body = body;
    }

    public static void setScore(int score) {
        Computer.score += score;
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
