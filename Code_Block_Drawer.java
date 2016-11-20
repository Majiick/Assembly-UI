/*
    Draws the code blocks in an orderly fashion.
 */

import processing.core.PVector;
import processing.event.MouseEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

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

        for(Vector<Instruction_Runner> level : levels.values()) {
            for(Instruction_Runner runner : level) {
                int levelIndent = level_counters.get(runner.level);
                level_counters.put(runner.level, levelIndent + 1);
                runner.getBlock().draw(t, new PVector(100 * amount + scaledViewOffset.x + (250 * levelIndent), 100 * amount + scaledViewOffset.y  + (1000 * runner.level)));
            }
        }

        //draw lines
        t.stroke(255);
        for(Vector<Instruction_Runner> level : levels.values()) {
            for(Instruction_Runner runner : level) {
                if (runner.from_block != null) {
                    if (runner.block.pos != null && runner.from_block.block.pos != null) {
                        t.line(runner.block.pos.x, runner.block.pos.y, runner.from_block.block.pos.x, runner.from_block.block.pos.y);
                    }
                }
            }
        }



    }

    public void mouseDragged() {
        viewOffset.x += t.mouseX - t.pmouseX;
        viewOffset.y += t.mouseY - t.pmouseY;
    }

    public void mouseWheel(MouseEvent event) {
        scale += event.getCount() / 10.0f;

        if (scale < 0.1f) {
            scale = 0.1f;
        }
    }
}
