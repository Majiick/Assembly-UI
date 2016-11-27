import com.sun.prism.paint.Color;
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.List;


public class BackgroundFX {
    public enum Direction{
        VERTICAL,
        HORIZONTAL
    }

    class BLine {
        Traverse_Range range;
        Color color;
        Direction direction;

        BLine (Color color, Direction direction) {
            range = new Traverse_Range(0, direction == Direction.HORIZONTAL ? t.width : t.height);
            range.setVal(ThreadLocalRandom.current().nextInt(0, t.width));
            this.color = color;
            this.direction = direction;
        }

        public void draw() {
            t.strokeWeight(ThreadLocalRandom.current().nextFloat() / 2.0f);
            float cord = range.getNext();

            if (direction == Direction.HORIZONTAL) {
                t.line(cord, 0, cord, t.height);
            } else {
                t.line(0, cord, t.width, cord);
            }

            t.strokeWeight(1);
        }

    }

    Main t;
    List<BLine> bLines = new ArrayList<>();

    BackgroundFX(Main t) {
        this.t = t;
        for (int i = 0; i < ThreadLocalRandom.current().nextInt(50, 100); i++) {
            bLines.add(new BLine(new Color(0.0f, 0.0f, 0.0f, 1.0f), Direction.HORIZONTAL));
            bLines.add(new BLine(new Color(0.0f, 0.0f, 0.0f, 1.0f), Direction.VERTICAL));
        }
    }

    public void draw() {
        bLines.forEach((l) -> l.draw());
    }
}
