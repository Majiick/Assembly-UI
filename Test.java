import processing.core.*;
import java.util.Arrays;
import capstone.Capstone;

public class Test extends PApplet{
    final static int entryPointOfProgram = 0x12A0 - 0x1000;

    public void settings() {
        size(1000, 1000);
    }

    public void setup() {
        byte[] bytes = FileReader.readFile("C:\\Users\\Ecoste\\IdeaProjects\\i-didn-t-think-of-a-name-yet\\helloWorld32.bin");
        System.out.println("File size: " + bytes.length);
        System.out.println("Byte at entry point: " + String.format("%02x", bytes[entryPointOfProgram]));
        printBytes(Arrays.copyOfRange(bytes, entryPointOfProgram, entryPointOfProgram + 0x15));

        Capstone cs = new Capstone(Capstone.CS_ARCH_X86, Capstone.CS_MODE_32);
        Instruction_Runner runner = new Instruction_Runner(bytes, entryPointOfProgram, cs);
        runner.step();
        runner.step();
    }

    public void draw(){
        background(0);
        ellipse(mouseX, mouseY, 20, 20);
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

}
