import processing.core.*;
import capstone.Capstone;
import processing.event.MouseEvent;
import java.util.*;
import java.util.stream.Collectors;

public class Main extends PApplet{
    private List<Instruction_Runner> runners = new ArrayList<>();
    private Capstone cs;
    private Code_Block_Drawer drawer;
    private BackgroundFX bgfx;

    public static void main(String... args){
        PApplet.main("Main");
    }

    public Capstone getCs() {
        return cs;
    }

    public List<Instruction_Runner> getRunners() {
        return runners;
    }

    public void settings() {
        size(1000, 1000);
    }

    public void setup() {
        //Set text font.
        PFont scifiFont = loadFont("MagmawaveCaps-Bold-48.vlw");
        textFont(scifiFont);

        //Create backgroundfx and code block drawer.
        bgfx = new BackgroundFX(this);
        drawer = new Code_Block_Drawer(this);

        //Initialize capstone
        cs = new Capstone(Capstone.CS_ARCH_X86, Capstone.CS_MODE_32);
        cs.setDetail(Capstone.CS_OPT_DETAIL); //Turn on detailed mode.

        makeRunner(Binary.ENTRY_POINT); //Make the first runner.
    }

    public void draw(){
        background(0);
        bgfx.draw();

        if (keyPressed) {
            for (Instruction_Runner runner : runners.stream().filter((r) -> !r.finished && !r.paused).collect(Collectors.toList())) {
                runner.step();
            }
        }

        drawer.draw();
    }

    public void mouseDragged() {
        drawer.mouseDragged();
    }

    public void mousePressed() {
        drawer.mousePressed(mouseButton);
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

    public static void printBytes(byte[] bytes) {
        for (byte b : bytes) {
            System.out.print(String.format("%02x", b));
        }
        System.out.print("\n");
    }

    public Instruction_Runner makeRunner(long startLocation) {
        Instruction_Runner t = new Instruction_Runner(startLocation, 0, this);
        runners.add(t);
        return t;
    }

    public Instruction_Runner makeRunner(long startLocation, Instruction_Runner from_block, int level) {
        Instruction_Runner t = new Instruction_Runner(startLocation, from_block, level, this);
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
