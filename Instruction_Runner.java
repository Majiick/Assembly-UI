/*
    Runs through instructions.
 */

import capstone.Capstone;
import java.util.*;


public class Instruction_Runner {
    static final String[] flowRedirectors = {"call", "jmp", "ret"};

    byte[] bytes;
    long startLocation;
    long nextInstruction;
    int level;
    Capstone cs;
    Instruction_Runner from_block;
    public List<Instruction_Runner> parents = new ArrayList<>();
    Code_Block block;
    Test t;

    boolean finished = false;
    boolean paused = false;

    public Code_Block getBlock() {
        return block;
    }



    Instruction_Runner(byte[] bytes, long startLocation, Capstone cs, Instruction_Runner from_block, int level, Test t) {
        this.bytes = bytes;
        this.startLocation = startLocation;
        this.cs = cs;
        this.nextInstruction = startLocation;
        block = new Code_Block();
        this.from_block = from_block;
        this.level = level;
        this.t = t;

        if (from_block == null) block.entryNode = true;
    }

    Instruction_Runner(byte[] bytes, long startLocation, Capstone cs, int level, Test t) {
        this(bytes, startLocation, cs, null, level, t);
    }

    public void step() {
        //http://stackoverflow.com/questions/14698350/x86-64-asm-maximum-bytes-for-an-instruction : The maximum bytes for one instruction is 15 bytes.
        Capstone.CsInsn[] allInsn = cs.disasm(Arrays.copyOfRange(bytes, (int)nextInstruction, (int)nextInstruction + 15), nextInstruction, 0x1); //Array out of bounds on brogue on the end of the program to .bss section, that call should never happen because it's not in the IAT so it's not gonna be set and we're gonna go nowhere.
        if (allInsn.length > 1 || allInsn.length == 0) {
            System.out.println("This should never happen. Not throwing an exception because it's to see if my preconception is false and this cannot be fixed.");
            System.exit(-1);
        }

        for (Capstone.CsInsn insn : allInsn) {
            System.out.printf("0x%x:\t%s\t%s\n", insn.address,
                    insn.mnemonic, insn.opStr);

            block.level = "Level: " + this.level;
            nextInstruction += insn.size;
            block.instructions.add(insn);

            if (Arrays.asList(flowRedirectors).contains(insn.mnemonic)) {
                Redirection redirection = Mnemonic_Redirection_Calculator.getRedirectionLocation(insn);
                int redirectionLocation = redirection.address;
                System.out.println("Redirection location: " + String.format("%08x", redirectionLocation));
                if(insn.mnemonic.equals("ret")) {
                    ret();
                    break;
                }

//                if(redirection.address == 0) {
//                    continue;
//                }
                if(redirection.isRegisterRedirection) {
                    continue;
                }

                if(redirection.address == Binary.LOCATION_OF_EXITPROCESS) {
                    block.descriptors.add("EXIT NODE.");
                    block.exitNode = true;
                    end();
                }

                if (!redirection.toIAT()) {
                    if (redirection.isMemoryDereference) {
                        continue;
                    }
                }

                if (redirection.toIAT()) {
                    if (from_block != null) {
                        block.descriptors.add(t.functionName(redirectionLocation));
                        ret();
                    }
                } else {
                    System.out.println("Redirection location: " + String.format("%08x", redirectionLocation));
                    Instruction_Runner exists = t.findRunner(redirectionLocation);
                    if (exists == null) {
                        this.paused = true;
                        Instruction_Runner x = t.makeRunner(redirectionLocation, this, this.level + 1);
                        x.getBlock().descriptors.add("Address: " + t.functionName(redirectionLocation));
                    } else {
                        exists.parents.add(this);
                        System.out.println("Already exists");
                    }
                }
            }
        }
    }

    void ret() {
        this.finished = true;

        try {
            this.from_block.awaken();
        } catch (java.lang.NullPointerException e) {
            System.out.println("Can't return, from_block is empty.");
        }
    }

    void awaken() {
        this.paused = false;
    }

    void end() {
        this.finished = true;
    }
}
