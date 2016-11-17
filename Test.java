import capstone.Capstone;
import processing.core.*;
import java.util.Arrays;
import static javax.xml.bind.DatatypeConverter.printHexBinary;


public class Test extends PApplet{
    final static int entryPointOfProgram = 0x12A0 - 0x1000;

    public void settings(){
        size(1000, 1000);
    }

    public void setup() {
        byte[] bytes = FileReader.readFile("C:\\Users\\Ecoste\\IdeaProjects\\i-didn-t-think-of-a-name-yet\\helloWorld32.bin");
        System.out.println("File size: " + bytes.length);
        System.out.println("Byte at entry point: " + String.format("%02x", bytes[entryPointOfProgram]));
        printBytes(Arrays.copyOfRange(bytes, entryPointOfProgram, entryPointOfProgram + 0x15));

        Capstone cs = new Capstone(Capstone.CS_ARCH_X86, Capstone.CS_MODE_32);
        Capstone.CsInsn[] allInsn = cs.disasm(Arrays.copyOfRange(bytes, entryPointOfProgram, entryPointOfProgram + 0x15), entryPointOfProgram, 0x15);
        for (int i=0; i<allInsn.length; i++)
            System.out.printf("0x%x:\t%s\t%s\n", allInsn[i].address,
                    allInsn[i].mnemonic, allInsn[i].opStr);
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



//    public static byte [] CODE = { 0x55, 0x48, (byte) 0x8b, 0x05, (byte) 0xb8,
//            0x13, 0x00, 0x00 };
//
//    public void settings(){
//        size(1000, 1000);
//    }
//
//    public void setup() {
//        Capstone cs = new Capstone(Capstone.CS_ARCH_X86, Capstone.CS_MODE_64);
//        Capstone.CsInsn[] allInsn = cs.disasm(CODE, 0x1000);
//        for (int i=0; i<allInsn.length; i++)
//            System.out.printf("0x%x:\t%s\t%s\n", allInsn[i].address,
//                    allInsn[i].mnemonic, allInsn[i].opStr);
//    }
//
//    public void draw(){
//        background(0);
//        ellipse(mouseX, mouseY, 20, 20);
//    }
//
//    public static void main(String... args){
//        PApplet.main("Test");
//    }