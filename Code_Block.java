/*
A block of assembly code that ends with a ret or call to exit.
 */

import capstone.Capstone;
import processing.core.PVector;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;
import java.util.*;

public class Code_Block {
    public List<Capstone.CsInsn> instructions = new ArrayList<>();
    public List<String> descriptors = new ArrayList<>();
    public String level = "0";
    public PVector pos;
    public PVector size;
    public int directionInTree = 0;
    public boolean entryPoint = false;

    public void draw(Test t, PVector loc) {
        pos = loc;
        size = new PVector(biggestInstructionLength() * 7, instructions.size() * 11 + 11, 7);
        //Draw rectangle.
        t.fill(255, 140, 0);
        if (entryPoint) {
            t.fill(0);
        }
        
        t.rect(loc.x, loc.y, size.x, size.y); //Draw a curved rectangle.
        traverseRangeFemale.updateRange((int)-(size.x/2), (int)(size.x/2));
        traverseRangeMale.updateRange((int)-(size.x/2), (int)(size.x/2));

        //Draw text.
        t.textSize(10);
        t.fill(85, 227, 0);
        t.textAlign(t.LEFT, t.TOP);

        float y = loc.y + 5;
        t.text(level, loc.x + 5, y);
        y += 10;

        //float y = loc.y;
        for (Capstone.CsInsn insn : instructions) {
            t.text(insn.mnemonic + " " + insn.opStr, loc.x + 5, y);
            y += 10;
        }
    }

    int biggestInstructionLength() {
        if (instructions.isEmpty()) {
            return 0;
        }

        return Collections.max(instructions, new Comparator<Capstone.CsInsn>() { //Can't have a lambda because .opStr doesn't have a getter.
            public int compare(Capstone.CsInsn a, Capstone.CsInsn b) {
                return Integer.compare((a.mnemonic + " " + a.opStr).length(), (b.mnemonic + " " + b.opStr).length());
            }
        }).opStr.length();
    }


    public enum Direction{
        RIGHT,
        LEFT
    }

    class Traverse_Range {
        Direction direction;
        int min;
        int max;
        int speed;
        int val = ThreadLocalRandom.current().nextInt(min, max + 1);


        Traverse_Range(int min, int max) {
            this.min = min;
            this.max = max;
            direction = ThreadLocalRandom.current().nextInt(0, 10) > 5 ? Direction.LEFT : Direction.RIGHT;
            speed = ThreadLocalRandom.current().nextInt(2, 5);
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

        public void updateRange(int min, int max) {
            this.min = min;
            this.max = max;
        }
    }

    Traverse_Range traverseRangeFemale = new Traverse_Range(0, 0);
    Traverse_Range traverseRangeMale = new Traverse_Range(0, 0);

    public PVector getMaleStart() {
        //return new PVector(pos.x + size.x/2  + traverseRangeMale.getNext(), pos.y + size.y);
        return new PVector(pos.x + size.x/2, pos.y + size.y);
    }

    public PVector getFemaleStart() {
        return new PVector(pos.x + size.x/2 + traverseRangeFemale.getNext(), pos.y);
    }
}
