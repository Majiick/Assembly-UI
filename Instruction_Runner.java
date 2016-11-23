/*
    Runs through instructions.
 */

import capstone.Capstone;
import java.util.*;


public class Instruction_Runner {
    private static final String[] flowRedirectors = {"call", "jmp", "ret"};

    long startLocation;
    long nextInstruction;
    int level;
    Instruction_Runner parent;
    public List<Instruction_Runner> parents = new ArrayList<>();
    private Code_Block block;
    Test t;

    boolean finished = false;
    boolean paused = false;

    Instruction_Runner(long startLocation, Instruction_Runner parent, int level, Test t) {
        this.startLocation = startLocation;
        this.nextInstruction = startLocation;
        block = new Code_Block();
        this.parent = parent;
        this.level = level;
        this.t = t;

        if (parent == null) block.entryNode = true;
    }

    Instruction_Runner(long startLocation, int level, Test t) {
        this(startLocation, null, level, t);
    }

    public Code_Block getBlock() {
        return block;
    }

    public void step() {
        //http://stackoverflow.com/questions/14698350/x86-64-asm-maximum-bytes-for-an-instruction : The maximum bytes for one instruction is 15 bytes.
        //Read next instruction
        Capstone.CsInsn[] allInsn = t.getCs().disasm(Arrays.copyOfRange(Binary.getInstance().bytes, (int)nextInstruction, (int)nextInstruction + 15), nextInstruction, 0x1);
        Capstone.CsInsn insn = allInsn[0];


        System.out.printf("0x%x:\t%s\t%s\n", insn.address,
                insn.mnemonic, insn.opStr);

        block.level = "Level: " + this.level;
        nextInstruction += insn.size;
        block.instructions.add(insn);

        //If the instruction is a redirection.
        if (Arrays.asList(flowRedirectors).contains(insn.mnemonic)) {
            Redirection redirection = Mnemonic_Redirection_Calculator.getRedirectionLocation(insn);

            if(insn.mnemonic.equals("ret")) {
                ret();
                return;
            }

            //If the redirection takes address from register e.g. 'call eax' then just skip it.
            if(redirection.isRegisterRedirection) {
                return;
            }

            //If redirection calls the exit process, then that means this node is the exit node and we must end it.
            if(redirection.address == Binary.LOCATION_OF_EXITPROCESS) {
                block.exitNode = true;
                end();
                return;
            }

            //If the redirection dereferences memory e.g. 'call [0x1234]' then skip it.
            if (redirection.isMemoryDereference) {
                if (!redirection.toIAT()) {
                    return;
                }
            }

            //If the redirection calls an IAT location, then it's calling an imported function. This should only happen with the branch table.
            if (redirection.toIAT()) {
                if (parent != null) {
                    block.descriptors.add(t.functionName(redirection.address));
                    ret();
                }

                return;
            }

            //If a block with the starting location already exists, then point to it, else create a new runner for the new address block.
            Instruction_Runner exists = t.findRunner(redirection.address);
            if (exists == null) {
                this.paused = true;
                Instruction_Runner x = t.makeRunner(redirection.address, this, this.level + 1);
                x.getBlock().descriptors.add("Address: " + t.functionName(redirection.address));
            } else {
                exists.parents.add(this);
                System.out.println("Already exists");
            }
        }
    }

    void ret() {
        this.finished = true;

//        try {
            this.parent.awaken();
//        } catch (java.lang.NullPointerException e) {
//            System.out.println("Can't return, parent is empty.");
//        }
    }

    void awaken() {
        this.paused = false;
    }

    void end() {
        this.finished = true;
    }
}
