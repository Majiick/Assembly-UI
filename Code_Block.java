/*
A block of assembly code that ends with a ret or call to exit.
 */

import capstone.Capstone;
import processing.core.PVector;
import java.util.Collections;
import java.util.*;

public class Code_Block {
    //Public abuse yeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee boiiiiiiiiiiiiiiiiiiiiiiiiii
    public List<Capstone.CsInsn> instructions = new ArrayList<>();
    public List<String> descriptors = new ArrayList<>();
    public String level = "0";
    public PVector pos;
    public PVector size;
    public int directionInTree = 0;
    public boolean entryNode = false;
    public boolean exitNode = false;
    public PVector userMoveOffset = new PVector(0, 0);
    public PVector color = new PVector(0, 0, 0);
    Right_Click_Menu rMenu = null;
    Main t;


    public void draw(Main t, PVector loc, float scale) {
        this.t = t; //This should be in the constructor, but need to refactor errything.
        pos = loc;
        pos.x += userMoveOffset.x;
        pos.y += userMoveOffset.y;
        size = new PVector(biggestInstructionLength() * 7, (descriptors.size() + instructions.size()) * 11 + 11, 7);
        isMouseOver(scale);

        //Draw rectangle.
        t.stroke(116, 255, 72);
        t.strokeWeight(10.0f);
        t.fill(0);
        t.fill(color.x, color.y, color.z);
        if (entryNode) {
            t.fill(255, 0, 0);
        }
        if (exitNode) {
            t.fill(255);
        }
        t.rect(pos.x, pos.y, size.x, size.y); //Draw a curved rectangle.
        t.strokeWeight(1.0f);
        t.stroke(0);

        //Update female and male connection ranges in case width changes.
        traverseRangeFemale.updateRange((int)-(size.x/2), (int)(size.x/2));
        traverseRangeMale.updateRange((int)-(size.x/2), (int)(size.x/2));

        //Draw text.
        t.textSize(10);
        t.fill(54, 164, 0);
        t.textAlign(t.LEFT, t.TOP);

        float y = loc.y + 5;
        t.text(level, loc.x + 5, y);
        y += 10;

        for (String desc : descriptors) {
            t.text(desc, loc.x + 5, y);
            y += 10;
        }

        t.fill(85, 227, 0);
        //float y = loc.y;
        for (Capstone.CsInsn insn : instructions) {
            t.text(insn.mnemonic + " " + insn.opStr, loc.x + 5, y);
            y += 10;
        }

        if (rMenu != null) rMenu.draw();
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

    Traverse_Range traverseRangeFemale = new Traverse_Range(0, 0);
    Traverse_Range traverseRangeMale = new Traverse_Range(0, 0);

    public PVector getMaleStart() {
        //return new PVector(pos.x + size.x/2  + traverseRangeMale.getNext(), pos.y + size.y);
        return new PVector(pos.x + size.x/2, pos.y + size.y);
    }

    public PVector getFemaleStart() {
        return new PVector(pos.x + size.x/2 + traverseRangeFemale.getNext(), pos.y);
    }

    boolean isMouseOver(float scale) {
        if(t.mouseX > pos.x * scale && t.mouseX < (pos.x+size.x)*scale && t.mouseY > pos.y * scale && t.mouseY < (pos.y+size.y) * scale){
            return true;
        }

        return false;
    }

    public boolean mouseDragged(float scale) {
        if(!isMouseOver(scale)) {
            return false;
        }

        userMoveOffset.x += (t.mouseX - t.pmouseX) / scale;
        userMoveOffset.y += (t.mouseY - t.pmouseY) / scale;
        return true;
    }

    public void mousePressed(int mouseButton, float scale) {
        if(isMouseOver(scale) && mouseButton == t.RIGHT) {
            if (rMenu == null) {
                rMenu = new Right_Click_Menu(t, this);
            }
        } else {
            if (rMenu != null && !rMenu.isMouseOver(scale)) {
                rMenu = null;
            }
        }

        if (rMenu != null && rMenu.isMouseOver(scale)) rMenu.mousePressed(mouseButton, scale);
    }

}
