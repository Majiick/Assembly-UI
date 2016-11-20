import processing.core.*;
import java.util.Arrays;
import capstone.Capstone;
import processing.event.MouseEvent;
import java.util.*;
import java.util.stream.Collectors;

public class Test extends PApplet{
    final static int entryPointOfProgram = 0x12A0 - 0x1000;
    final static int IAT_RVA = 0xE14C + 0x400000;
    final static int IAT_SIZE = 0xFC + 0x400000;
    final static int LOCATION_OF_EXITPROCESS = 0x70d0;

    //Not super use of static, I admit. But, there shouldn't ever be more than one of these in Test.
    static List<Instruction_Runner> runners = new ArrayList<>();
    static Capstone cs;
    static byte[] bytes;
    Code_Block_Drawer drawer;

    public void settings() {
        size(1000, 1000);
    }

    public void setup() {
        drawer = new Code_Block_Drawer(this);

        bytes = FileReader.readFile("C:\\Users\\Ecoste\\IdeaProjects\\i-didn-t-think-of-a-name-yet\\helloWorld32.bin");
        System.out.println("File size: " + bytes.length);
        System.out.println("Byte at entry point: " + String.format("%02x", bytes[entryPointOfProgram]));
        printBytes(Arrays.copyOfRange(bytes, entryPointOfProgram, entryPointOfProgram + 0x15));

        cs = new Capstone(Capstone.CS_ARCH_X86, Capstone.CS_MODE_32);
        cs.setDetail(Capstone.CS_OPT_DETAIL); //Turn on detailed mode.

        makeRunner(entryPointOfProgram);
    }

    public void draw(){
        background(0);

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

    public static void main(String... args){
        PApplet.main("Test");
    }

    public static void printBytes(byte[] bytes) {
        for (byte b : bytes) {
            System.out.print(String.format("%02x", b));
        }
        System.out.print("\n");
    }

    public static void makeRunner(int startLocation) {
        runners.add(new Instruction_Runner(bytes, startLocation, cs, 0));
    }

    public static void makeRunner(int startLocation, Instruction_Runner from_block, int level) {
        runners.add(new Instruction_Runner(bytes, startLocation, cs, from_block, level));
    }
}
