public class Sim4HeaderVersion2 {

    public static class InstructionFields {
        public int opcode;
        public int rs;
        public int rt;
        public int rd;
        public int shamt;
        public int funct;
        public int imm16, imm32;
        public int address;
    }

    public static class CPUControl {
        public int ALUsrc;
        public ALUControl ALU = new ALUControl();
        public int memRead;
        public int memWrite;
        public int memToReg;
        public int regDst;
        public int regWrite;
        public int branch;
        public int jump;
        public int extra1, extra2, extra3;

        public static class ALUControl {
            public int op;
            public int bNegate;
        }
    }

    public static class ALUResult {
        public int result;
        public int zero;
        public int extra;
    }

    public static class MemResult {
        public int readVal;
    }

    // Method declarations (to be implemented by students)
    public static int getInstruction(int curPC, int[] instructionMemory) {
        throw new UnsupportedOperationException("Implementation required");
    }

    public static void extractInstructionFields(int instruction, InstructionFields fieldsOut) {
        throw new UnsupportedOperationException("Implementation required");
    }

    public static int fillCPUControl(InstructionFields fields, CPUControl controlOut) {
        throw new UnsupportedOperationException("Implementation required");
    }

    public static int getALUinput1(CPUControl controlIn, InstructionFields fieldsIn,
                                   int rsVal, int rtVal, int reg32, int reg33, int oldPC) {
        throw new UnsupportedOperationException("Implementation required");
    }

    public static int getALUinput2(CPUControl controlIn, InstructionFields fieldsIn,
                                   int rsVal, int rtVal, int reg32, int reg33, int oldPC) {
        throw new UnsupportedOperationException("Implementation required");
    }

    public static void executeALU(CPUControl controlIn, int input1, int input2, ALUResult aluResultOut) {
        throw new UnsupportedOperationException("Implementation required");
    }

    public static void executeMEM(CPUControl controlIn, ALUResult aluResultIn,
                                  int rsVal, int rtVal, int[] memory, MemResult resultOut) {
        throw new UnsupportedOperationException("Implementation required");
    }

    public static int getNextPC(InstructionFields fields, CPUControl controlIn, int aluZero,
                                int rsVal, int rtVal, int oldPC) {
        throw new UnsupportedOperationException("Implementation required");
    }

    public static void executeUpdateRegs(InstructionFields fields, CPUControl controlIn,
                                         ALUResult aluResultIn, MemResult memResultIn, int[] regs) {
        throw new UnsupportedOperationException("Implementation required");
    }

    public static int signExtend16to32(int val16) {
        return (val16 & 0x8000) != 0 ? val16 | 0xFFFF0000 : val16;
    }
}