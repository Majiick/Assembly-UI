import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

//// Test.java
//import capstone.Capstone;
//
//public class Test {
//    public static byte [] CODE = { 0x55, 0x48, (byte) 0x8b, 0x05, (byte) 0xb8,
//            0x13, 0x00, 0x00 };
//
//    public static void main(String argv[]) {
//        Capstone cs = new Capstone(Capstone.CS_ARCH_X86, Capstone.CS_MODE_64);
//        Capstone.CsInsn[] allInsn = cs.disasm(CODE, 0x1000);
//        for (int i=0; i<allInsn.length; i++)
//            System.out.printf("0x%x:\t%s\t%s\n", allInsn[i].address,
//                    allInsn[i].mnemonic, allInsn[i].opStr);
//    }
//}

import processing.core.*;

public class Test extends PApplet{

    public void settings(){
        size(1000, 1000);
    }

    public void draw(){
        background(0);
        ellipse(mouseX, mouseY, 20, 20);
    }

    public static void main(String... args){
        PApplet.main("Test");
    }
}