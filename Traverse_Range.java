import java.util.concurrent.ThreadLocalRandom;

/*
    Class that takes in a range and will walk along the path returning values at a certain speed(step), when it reaches an edge it will go in the other direction.
*/

class Traverse_Range {
    public enum Direction{
        RIGHT,
        LEFT
    }

    Direction direction;
    int min;
    int max;
    int speed;
    int val = ThreadLocalRandom.current().nextInt(min, max + 1);


    Traverse_Range(int min, int max) {
        this(min, max, ThreadLocalRandom.current().nextInt(2, 5));
    }

    Traverse_Range(int min, int max, int speed) {
        this.min = min;
        this.max = max;
        direction = ThreadLocalRandom.current().nextInt(0, 10) > 5 ? Direction.LEFT : Direction.RIGHT;
        this.speed = speed;
    }

    int getNext() {
        if(direction == Direction.LEFT) {
            val += speed;
        } else {
            val -= speed;
        }

        if (val > max || val < min) {
            direction = direction == Direction.LEFT ? Direction.RIGHT : Direction.LEFT;
        }

        return val;
    }

    public void setVal (int val) {
        this.val = val;
    }

    public void setSpeed (int speed) {
        this.speed = speed;
    }

    public void updateRange(int min, int max) {
        this.min = min;
        this.max = max;
    }
}