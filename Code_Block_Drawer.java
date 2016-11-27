/*
    Draws the code blocks in an orderly fashion.
 */

import processing.core.PVector;
import processing.event.MouseEvent;

import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

public class Code_Block_Drawer {
    Main t;
    PVector viewOffset = new PVector(0,0);
    float scale = 1;

    Code_Block_Drawer(Main t) {
        this.t = t;
    }

    public void draw() {
        HashMap<Integer, Vector<Instruction_Runner>> levels = new HashMap<>();
        HashMap<Integer, Integer> level_counters = new HashMap<>();

        PVector scaledViewOffset = new PVector(viewOffset.x / scale, viewOffset.y / scale);

        t.scale(scale);

        //Put runners into levels.
        int amount = 1;
        for (Instruction_Runner runner : t.getRunners()) {
            if (!levels.containsKey(runner.level)) {
                levels.put(runner.level, new Vector<Instruction_Runner>());
                level_counters.put(runner.level, 0);
            }

            levels.get(runner.level).add(runner);
        }

        //Draw blocks.
        int direction = -1;
        for (Instruction_Runner runner : t.getRunners()) {
            if (runner.getBlock().directionInTree == 0) {
                runner.getBlock().directionInTree = direction;
            }

            int levelIndent = level_counters.get(runner.level);
            level_counters.put(runner.level, levelIndent + 1);
            runner.getBlock().draw(t, new PVector(100 * amount + scaledViewOffset.x + (250 * levelIndent) * runner.getBlock().directionInTree, 100 * amount + scaledViewOffset.y  + (1100 * runner.level)), scale);
            direction *= -1;
        }

        //draw lines
        t.stroke(255);
        for(Vector<Instruction_Runner> level : levels.values()) {
            for(Instruction_Runner runner : level) {
                if (runner.parent != null) {
                    if (runner.getBlock().pos != null && runner.parent.getBlock().pos != null) {
                        drawLine(runner, runner.parent);
                    }
                    //Draw parents
                    for (Instruction_Runner _parent : runner.parents) {
                        drawLine(runner, _parent);
                    }
                }
            }
        }

    }

    void drawLine(Instruction_Runner _parent, Instruction_Runner _child) {
        t.stroke(116, 255, 72);
        t.strokeWeight(ThreadLocalRandom.current().nextInt(1, 5));
        Code_Block parent = _parent.getBlock();
        Code_Block child = _child.getBlock();

        PVector start = parent.getFemaleStart();
        PVector end = child.getMaleStart();

        t.line(start.x, start.y , end.x, end.y);
        t.strokeWeight(1);
        t.stroke(255);
    }

    public void mouseDragged() {
        boolean draggingBlock = false;
        for (Instruction_Runner runner : t.getRunners()) {
            boolean isBeingDragged = runner.getBlock().mouseDragged(scale);
            if (isBeingDragged) draggingBlock = true;
        }

        if(!draggingBlock) {
            viewOffset.x += t.mouseX - t.pmouseX;
            viewOffset.y += t.mouseY - t.pmouseY;
        }
    }

    public void mouseWheel(MouseEvent event) {
        zoom(event.getCount() / 10.0f);
    }

    public void zoom(float amount) {
        scale += amount;

        if (scale < 0.1f) {
            scale = 0.1f;
        }
    }

    public void mousePressed(int mouseButton) {
        for (Instruction_Runner runner : t.getRunners()) {
            runner.getBlock().mousePressed(mouseButton, scale);
        }
    }
}
