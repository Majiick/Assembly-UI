/*
A block of assembly code that ends with a ret or call to exit.
 */

import capstone.Capstone;
import processing.core.PVector;
import java.util.Collections;

import java.util.*;

public class Code_Block {
    public List<Capstone.CsInsn> instructions = new ArrayList<>();
    public List<String> descriptors = new ArrayList<>();
    public String level = "0";
    public PVector pos;

    public void draw(Test t, PVector loc) {
        pos = loc;
        //Draw rectangle.
        t.fill(255);
        t.rect(loc.x, loc.y, biggestInstructionLength() * 7, instructions.size() * 11 + 11                       , 7); //Draw a curved rectangle.

        //Draw text.
        t.textSize(10);
        t.fill(0);
        t.textAlign(t.LEFT, t.TOP);

        float y = loc.y;
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
}
