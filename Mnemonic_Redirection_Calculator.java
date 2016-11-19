/*
    Helper class to find out where an instruction will redirect the execution.
 */

import capstone.Capstone;
import capstone.X86;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Mnemonic_Redirection_Calculator {
    interface IInstruction {
        int calculateAbsoluteAddress(Capstone.CsInsn insn);
    }

    static class Valid_Instruction{ //SOME INSTRUCTIONS ARE 2 OPCODES BTW.
        public byte opcode;
        public byte modrm;

        Valid_Instruction(byte opcode, byte modrm) {
            this.opcode = opcode;
            this.modrm = modrm;
        }

        Valid_Instruction(int opcode, int modrm) {
            this((byte)opcode, (byte)modrm);
        }

        @Override
        public boolean equals(Object object) {
            Valid_Instruction other = (Valid_Instruction) object;
            return this.opcode == other.opcode && this.modrm == other.modrm;
        }
    }

    static class FF_15 extends Valid_Instruction implements IInstruction{
        FF_15() {
            super(0xFF, 0x15);
        }

        public int calculateAbsoluteAddress(Capstone.CsInsn insn) {
            X86.OpInfo operands = (X86.OpInfo) insn.operands;
            System.out.println((int)operands.op[0].value.mem.disp);
            return (int)operands.op[0].value.mem.disp;
        }
    }

    static class FF_25 extends Valid_Instruction implements IInstruction{
        FF_25() {
            super(0xFF, 0x25);
        }

        public int calculateAbsoluteAddress(Capstone.CsInsn insn) {
            X86.OpInfo operands = (X86.OpInfo) insn.operands;
            System.out.println((int)operands.op[0].value.mem.disp);
            return (int)operands.op[0].value.mem.disp;
        }
    }

    static class E8 extends Valid_Instruction implements IInstruction{
        E8() {
            super(0xE8, 0x00);
        }

        public int calculateAbsoluteAddress(Capstone.CsInsn insn) {
            X86.OpInfo operands = (X86.OpInfo) insn.operands;

            return (int)operands.op[0].value.imm;
        }
    }

    static IInstruction[] validInstructions = {new FF_15(), new E8(), new FF_25()};

    public static Redirection getRedirectionLocation(Capstone.CsInsn insn) {
        X86.OpInfo operands = (X86.OpInfo) insn.operands;
        if (operands.op.length > 1) {
            System.out.println("There shouldn't ever be more than 1 operand here.");
            System.exit(-2);
        }

//        System.out.printf("0x%x:\t%s\t%s\n", insn.address,
//                    insn.mnemonic, insn.opStr);

        List<IInstruction> l = Arrays.asList(validInstructions).stream().filter((o) -> o.equals(new Valid_Instruction(operands.opcode[0], operands.modrm))).collect(Collectors.toList());
        if(!l.isEmpty()) {
            return new Redirection(l.get(0).calculateAbsoluteAddress(insn));
        }


//        if(Arrays.asList(validInstructions).contains(new Valid_Instruction(operands.opcode[0], operands.modrm))) {
//            System.out.println(operands.op[0].value.mem.disp);
//        }


//        System.out.println(operands.opcode[0] == (byte)0xFF && operands.modrm == (byte)0x15);
//        System.out.println(new Valid_Instruction(0xFF, 0x15).equals(new Valid_Instruction(operands.opcode[0], operands.modrm)));
//        System.out.println(String.format("%02x", operands.opcode[0]) + " " + String.format("%02x", operands.modrm));

        return new Redirection(0);
    }
}
