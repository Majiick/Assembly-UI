/*
    Draws the code blocks in an orderly fashion.
 */

import processing.core.PVector;
import processing.event.MouseEvent;

import java.util.List;

public class Code_Block_Drawer {
    Test t;
    PVector viewOffset = new PVector(0,0);
    float scale = 1;

    Code_Block_Drawer(Test t) {
        this.t = t;
    }

    public void draw(List<Instruction_Runner> runners) {
        PVector scaledViewOffset = new PVector(viewOffset.x / scale, viewOffset.y / scale);

        t.scale(scale);


        int amount = 1;
        for (Instruction_Runner runner : runners) {
            runner.getBlock().draw(t, new PVector(100 * amount + scaledViewOffset.x, 100 * amount + scaledViewOffset.y));
            amount++;
        }
        //runners.forEach((runner) -> runner.getBlock().draw(t, new PVector(30 + scaledViewOffset.x, 30 + scaledViewOffset.y)));
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
