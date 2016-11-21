/*
    Draws the code blocks in an orderly fashion.
 */

import processing.core.PVector;
import processing.event.MouseEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

public class Code_Block_Drawer {
    Test t;
    PVector viewOffset = new PVector(0,0);
    float scale = 1;
    //Vector<Vector<Instruction_Runner>> levels = new Vector<>();

    Code_Block_Drawer(Test t) {
        this.t = t;
    }

    public void draw(List<Instruction_Runner> runners) {
        HashMap<Integer, Vector<Instruction_Runner>> levels = new HashMap<>();
        HashMap<Integer, Integer> level_counters = new HashMap<>();

        PVector scaledViewOffset = new PVector(viewOffset.x / scale, viewOffset.y / scale);

        t.scale(scale);

        //Put runners into levels. Why do I even need this tbh?
        int amount = 1;
        for (Instruction_Runner runner : runners) {
//            runner.getBlock().draw(t, new PVector(100 * amount + scaledViewOffset.x, 100 * amount + scaledViewOffset.y));
//            amount++;
            if (!levels.containsKey(runner.level)) {
                levels.put(runner.level, new Vector<Instruction_Runner>());
                level_counters.put(runner.level, 0);
            }

            levels.get(runner.level).add(runner);
        }
        //runners.forEach((runner) -> runner.getBlock().draw(t, new PVector(30 + scaledViewOffset.x, 30 + scaledViewOffset.y)));
        int direction = -1;
        for(Vector<Instruction_Runner> level : levels.values()) {
            for(Instruction_Runner runner : level) {
                if (runner.getBlock().directionInTree == 0) {
                    runner.getBlock().directionInTree = direction;
                }

                int levelIndent = level_counters.get(runner.level);
                level_counters.put(runner.level, levelIndent + 1);
                runner.getBlock().draw(t, new PVector(100 * amount + scaledViewOffset.x + (250 * levelIndent) * runner.getBlock().directionInTree, 100 * amount + scaledViewOffset.y  + (1100 * runner.level)), scale);
                direction *= -1;
            }
        }

        //draw lines
        t.stroke(255);
        for(Vector<Instruction_Runner> level : levels.values()) {
            for(Instruction_Runner runner : level) {
                if (runner.from_block != null) {
                    if (runner.block.pos != null && runner.from_block.block.pos != null) {
                        t.stroke(116, 255, 72);
                        t.strokeWeight(ThreadLocalRandom.current().nextInt(1, 5));
                        Code_Block parent = runner.from_block.block;
                        Code_Block child = runner.block;

                        PVector start = parent.getMaleStart();
                        PVector end = child.getFemaleStart();

                        t.line(start.x, start.y , end.x, end.y);
                        t.strokeWeight(1);
                        t.stroke(255);
                    }
                }
            }
        }



    }

    public void mouseDragged() {
        boolean draggingBlock = false;
        for (Instruction_Runner runner : t.runners) {
            boolean isBeingDragged = runner.getBlock().mouseDragged(scale);
            if (isBeingDragged) draggingBlock = true;
        }

        if(!draggingBlock) {
            viewOffset.x += t.mouseX - t.pmouseX;
            viewOffset.y += t.mouseY - t.pmouseY;
        }
    }

    public void mouseWheel(MouseEvent event) {
        scale += event.getCount() / 10.0f;

        if (scale < 0.1f) {
            scale = 0.1f;
        }
    }
}
