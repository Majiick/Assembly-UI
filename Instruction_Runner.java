/*
    Runs through instructions.
 */
import capstone.Capstone;
import java.util.Arrays;


public class Instruction_Runner {
    byte[] bytes;
    int startLocation;
    int nextInstruction;
    Capstone cs;

    Instruction_Runner(byte[] bytes, int startLocation, Capstone cs) {
        this.bytes = bytes;
        this.startLocation = startLocation;
        this.cs = cs;
        this.nextInstruction = startLocation;
    }

    public void step() {
        //http://stackoverflow.com/questions/14698350/x86-64-asm-maximum-bytes-for-an-instruction : The maximum bytes for one instruction is 15 bytes.
        Capstone.CsInsn[] allInsn = cs.disasm(Arrays.copyOfRange(bytes, nextInstruction, nextInstruction + 15), nextInstruction, 0x1);
        if (allInsn.length > 1 || allInsn.length == 0) {
            System.out.println("This should never happen. Not throwing an exception because it's to see if my preconception is false and this cannot be fixed.");
            System.exit(-1);
        }

        for (Capstone.CsInsn insn : allInsn) {
            System.out.printf("0x%x:\t%s\t%s\n", insn.address,
                    insn.mnemonic, insn.opStr);

            nextInstruction += insn.size;
        }
    }

}
