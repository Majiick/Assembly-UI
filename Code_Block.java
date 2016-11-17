/*
A block of assembly code that ends with a ret or jmp.
 */

import capstone.Capstone;
import processing.core.PVector;
import java.util.Collections;

import java.util.*;

public class Code_Block {
    public List<Capstone.CsInsn> instructions = new ArrayList<>();

    public void draw(Test t, PVector loc) {
        //Draw rectangle.
        t.fill(255);
        t.rect(loc.x, loc.y, biggestInstructionLength() * 15, instructions.size() * 11, 7); //Draw a curved rectangle.

        //Draw text.
        t.textSize(10);
        t.fill(0);
        t.textAlign(t.LEFT, t.TOP);

        float y = loc.y;
        for (Capstone.CsInsn insn : instructions) {
            t.text(insn.opStr, loc.x + 5, y);
            y += 10;
        }
    }

    int biggestInstructionLength() {
        return Collections.max(instructions, new Comparator<Capstone.CsInsn>() { //Can't have a lambda because .opStr doesn't have a getter.
            public int compare(Capstone.CsInsn a, Capstone.CsInsn b) {
                return Integer.compare(a.opStr.length(), a.opStr.length());
            }
        }).opStr.length();
    }
}
