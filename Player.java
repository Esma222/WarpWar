public class Player {
    private int hp;
    private Backpack backpack;
    private static char body = 'P';
    private int energy;
    private int score;

    public Player(int health, Backpack backpack, int energy, int score) {
        this.hp = health;
        this.backpack = backpack;
        this.energy = energy;
        this.score = score;
    }

    public Backpack getBackpack() {
        return backpack;
    }

    public Player() {
        this(5, new Backpack(), 50, 0);
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void decreaseHp(int hp) {
        this.hp -= hp;
    }

    public Object popBackpackItem() {
        return backpack.popItem();
    }

    public boolean addBackpackItem(Object obj) {
        if (obj.toString().charAt(0) == '1') {
            addScore(1);
            return true;
        }
        char item = backpack.addItem(obj);
        if (backpack.peekItem() != null) {
            if (backpack.peekItem().toString().charAt(0) == '1') {
                addScore(1);
                getBackpack().popItem();
            } else if (backpack.peekItem().toString().charAt(0) == '2') {
                addScore(5);
            } else if (backpack.peekItem().toString().charAt(0) == '3') {
                addScore(15);
            } else if (backpack.peekItem().toString().charAt(0) == '4') {
                addScore(50);
            } else if (backpack.peekItem().toString().charAt(0) == '5') {
                addScore(150);
            }
        }

        if (item == 'f') {
            return false;
        } else if (item == '2') {
            getBackpack().popItem();
            increaseEnergy(30);

        } else if (item == '3') {
            getBackpack().popItem();
            backpack.pushItem('=');
        } else if (item == '4') {
            getBackpack().popItem();
            increaseEnergy(240);
        } else if (item == '5') {
            getBackpack().popItem();
            backpack.pushItem('*');
        }
        getBackpack().printBackpack();

        return true;
    }

    public static char getBody() {
        return body;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int increaseEnergy(int energy) {
        return this.energy += energy;
    }

    public int decreaseEnergy(int energy) {
        this.energy -= energy;
        if (this.energy < 0) {
            this.energy = 0;
        }
        return this.energy;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public void decreaseScore(int score) {
        this.score -= score;
    }

}
