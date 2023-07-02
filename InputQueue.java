public class InputQueue {
    private int size;
    private CircularQueue q;
    private static char treasures[] = { '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '2', '2', '2', '2',
            '2', '2', '2', '2', '3', '3', '3', '3', '3', '3', '4', '4', '4', '4',
            '5', '5', '5', '5', '=', '=', '*', 'C', 'C' };

    public InputQueue(CircularQueue queue, int size) {
        /*
         * if (queue.size() != size) {
         * throw new
         * IllegalArgumentException("Queue size must be equal to the input size");
         * }
         */
        this.q = queue;
        this.size = size;
    }

    public InputQueue(int size) {
        CircularQueue q = new CircularQueue(size);
        for (int i = 0; i < size; i++) {
            q.enqueue(treasures[(int) (Math.random() * treasures.length)]);
        }

        this.q = q;
        this.size = size;
    }

    private boolean addElementToQueue(char e) {
        if (q.size() == size) {
            return false;
        }
        q.enqueue(e);
        return true;
    }

    private boolean addRandomElementToQueue() {
        char c = treasures[(int) (Math.random() * treasures.length)];
        return addElementToQueue(c);
    }

    public char getNextElement() {
        char c = (char) q.dequeue();
        addRandomElementToQueue();
        return c;
    }

    public String toString() {
        CircularQueue temp = new CircularQueue(size);
        String res = "";
        while (!q.isEmpty()) {
            char c = (char) q.dequeue();
            res += c;
            temp.enqueue(c);
        }
        while (!temp.isEmpty()) {
            q.enqueue(temp.dequeue());
        }
        return res;
    }
}
