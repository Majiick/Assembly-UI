import processing.core.*;
import capstone.Capstone;
import processing.event.MouseEvent;
import java.util.*;
import java.util.stream.Collectors;

public class Test extends PApplet{
    //Not super use of static, I admit. But, there shouldn't ever be more than one of these in Test.
    List<Instruction_Runner> runners = new ArrayList<>();
    Capstone cs;
    Code_Block_Drawer drawer;
    BackgroundFX bgfx;

    public void settings() {
        size(1000, 1000);
    }

    public void setup() {
        //PFont scifiFont = loadFont("MagmawaveCaps-Bold-48.vlw");
        //textFont(scifiFont);
        bgfx = new BackgroundFX(this);
        drawer = new Code_Block_Drawer(this);

        cs = new Capstone(Capstone.CS_ARCH_X86, Capstone.CS_MODE_32);
        cs.setDetail(Capstone.CS_OPT_DETAIL); //Turn on detailed mode.

        makeRunner(Binary.ENTRY_POINT); //Make the first runner.
    }

    public void draw(){
        //background(109, 0, 182);
        background(0);
        bgfx.draw();
        stroke(0);

        if (keyPressed ) {
            for (Instruction_Runner runner : runners.stream().filter((r) -> !r.finished && !r.paused).collect(Collectors.toList())) {
                runner.step();
            }
        }

        drawer.draw(runners);
    }

    public void mouseDragged() {
        drawer.mouseDragged();
    }

    public void mouseWheel(MouseEvent event) {
        drawer.mouseWheel(event);
    }

    public void keyPressed() {
        if (key == '+') {
            drawer.zoom(0.1f);
        }

        if (key == '-') {
            drawer.zoom(-0.1f);
        }
    }

    public static void main(String... args){
        PApplet.main("Test");
    }

    public static void printBytes(byte[] bytes) {
        for (byte b : bytes) {
            System.out.print(String.format("%02x", b));
        }
        System.out.print("\n");
    }

    public Instruction_Runner makeRunner(int startLocation) {
        Instruction_Runner t = new Instruction_Runner(Binary.getInstance().bytes, startLocation, cs, 0, this);
        runners.add(t);
        return t;
    }

    public Instruction_Runner makeRunner(int startLocation, Instruction_Runner from_block, int level) {
        Instruction_Runner t = new Instruction_Runner(Binary.getInstance().bytes, startLocation, cs, from_block, level, this);
        runners.add(t);
        return t;
    }

    public Instruction_Runner findRunner(int startLocation) {
        for (Instruction_Runner runner : runners) { //Filter too pita here.
            if (runner.startLocation == startLocation) {
                return runner;
            }
        }

        return null;
    }

    public String functionName(int address) {
        if (Binary.getInstance().funcNames.containsKey(address)) {
            return Binary.getInstance().funcNames.get(address);
        }

        return "func_" + String.format("%02x", address);
    }
}
