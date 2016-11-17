/*
    Runs through instructions.
 */

import capstone.Capstone;
import java.util.*;


public class Instruction_Runner {
    static final String[] flowRedirectors = {"call", "jmp", "ret"};

    byte[] bytes;
    int startLocation;
    int nextInstruction;
    Capstone cs;
    Stack stack = new Stack();
    Instruction_Runner from_block;
    Code_Block block;

    boolean finished = false;
    boolean paused = false;

    public Code_Block getBlock() {
        return block;
    }



    Instruction_Runner(byte[] bytes, int startLocation, Capstone cs, Instruction_Runner from_block) {
        this.bytes = bytes;
        this.startLocation = startLocation;
        this.cs = cs;
        this.nextInstruction = startLocation;
        block = new Code_Block();
        this.from_block = from_block;
    }

    Instruction_Runner(byte[] bytes, int startLocation, Capstone cs) {
        this(bytes, startLocation, cs, null);
    }

    public void step() {
        //http://stackoverflow.com/questions/14698350/x86-64-asm-maximum-bytes-for-an-instruction : The maximum bytes for one instruction is 15 bytes.
        Capstone.CsInsn[] allInsn = cs.disasm(Arrays.copyOfRange(bytes, nextInstruction, nextInstruction + 15), nextInstruction, 0x1);
        if (allInsn.length > 1 || allInsn.length == 0) {
            System.out.println("This should never happen. Not throwing an exception because it's to see if my preconception is false and this cannot be fixed.");
            System.exit(-1);
        }

        for (Capstone.CsInsn insn : allInsn) {
//            System.out.printf("0x%x:\t%s\t%s\n", insn.address,
//                    insn.mnemonic, insn.opStr);

            nextInstruction += insn.size;
            block.instructions.add(insn);

            if (Arrays.asList(flowRedirectors).contains(insn.mnemonic)) {
                int redirectionLocation = Mnemonic_Redirection_Calculator.getRedirectionLocation(insn);
                if (redirectionLocation != 0) {
                    System.out.println("Redirection location: " + String.format("%08x", redirectionLocation));
                    this.paused = true;
                    Test.makeRunner(redirectionLocation, this);
                }
            }
        }
    }

}
