import com.sun.prism.paint.Color;

import java.util.ArrayList;
import java.util.List;


public class BackgroundFX {
    public enum Direction{
        VERTICAL,
        HORIZONTAL
    }

    Test t;
    List<BLine> bLines = new ArrayList<>();

    class BLine {
        Traverse_Range range;
        Color color;
        Direction direction;

        BLine (Color color, Direction direction) {
            range = new Traverse_Range(0, direction == Direction.HORIZONTAL ? t.width : t.height);
            this.color = color;
            this.direction = direction;
        }

        public void draw() {
            t.strokeWeight(0.2f);
            float cord = range.getNext();

            if (direction == Direction.HORIZONTAL) {
                t.line(cord, 0, cord, t.height);
            } else {
                t.line(0, cord, t.width, cord);
            }

            t.strokeWeight(1);
        }

    }

    BackgroundFX(Test t) {
        this.t = t;
        bLines.add(new BLine(new Color(0.0f, 0.0f, 0.0f, 1.0f), Direction.HORIZONTAL));
        bLines.add(new BLine(new Color(0.0f, 0.0f, 0.0f, 1.0f), Direction.VERTICAL));
    }

    public void draw() {
        bLines.forEach((l) -> l.draw());
    }
}
