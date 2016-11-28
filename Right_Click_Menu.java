import processing.core.PVector;
import java.util.concurrent.ThreadLocalRandom;
import java.util.*;

/*
This is the color menu when you right click.
 */

public class Right_Click_Menu {
    Main t;
    Code_Block block;
    List<PVector> colors = new ArrayList<>();

    Right_Click_Menu(Main t, Code_Block block) {
        this.t = t;
        this.block = block;

        for (int i = 0; i < 3; i++) {
            colors.add(new PVector(ThreadLocalRandom.current().nextInt(0, 255), ThreadLocalRandom.current().nextInt(0, 255), ThreadLocalRandom.current().nextInt(0, 255)));
        }
    }

    public void draw() {
        for (int i = 0; i < colors.size(); i++) {
            t.fill(colors.get(i).x, colors.get(i).y, colors.get(i).z);
            t.rect(block.pos.x + block.size.x + 10 + i * 40, block.pos.y, 30, 30);
        }
    }

    public boolean isMouseOver(float scale) {
        if(t.mouseX > (block.pos.x + block.size.x) * scale && t.mouseX < (block.pos.x + block.size.x + colors.size()*40 + 10)*scale &&
                t.mouseY > block.pos.y * scale && t.mouseY < (block.pos.y+30) * scale){
            return true;
        }

        return false;
    }

    public void mousePressed(int mouseButton, float scale) {
        if (!isMouseOver(scale)) return;

        for (int i = 0; i < colors.size(); i++) {
            if(t.mouseX > (block.pos.x + block.size.x + i * 40 + 10) * scale && t.mouseX < (block.pos.x + block.size.x + i * 40 + 10 + 30)*scale &&
                    t.mouseY > block.pos.y * scale && t.mouseY < (block.pos.y+30) * scale){
                block.color = colors.get(i);
                break;
            }
        }

        block.rMenu = null;
    }
}
