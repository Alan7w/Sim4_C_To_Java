public class Sim4TestCommonCode {

    // Memory Sizes
    public static final int INST_WORDS = 1024;   // Instruction memory size in words
    public static final int DATA_WORDS = 16 * 1024; // Data memory size in words

    // CPU Memory Structure
    public static class CPUMemory {
        public int[] instMemory = new int[INST_WORDS]; // Read-only instruction memory
        public int[] dataMemory = new int[DATA_WORDS]; // Read-write data memory
        public int[] regs = new int[34]; // 32 general registers + lo, hi
        public int pc;

        public CPUMemory() {
            this.pc = 0;
        }
    }

    // Debugging and Execution Method Signatures (Implemented in Sim4TestUtils)
    public static int executeSingleCycleCPU(int[] regs, int[] instMemory, int[] dataMemory, int[] pc, int debug) {
        throw new UnsupportedOperationException("To be implemented in Sim4TestUtils.");
    }

    public static int execSyscall(int[] regs, int[] instMemory, int[] dataMemory, int[] pc, int debug) {
        throw new UnsupportedOperationException("To be implemented in Sim4TestUtils.");
    }

    public static void dumpPCInstruction(int pc, int inst) {
        throw new UnsupportedOperationException("To be implemented in Sim4TestUtils.");
    }

    public static void dumpFields(Sim4HeaderVersion2.InstructionFields fieldsIn) {
        throw new UnsupportedOperationException("To be implemented in Sim4TestUtils.");
    }

    public static void dumpControl(int rsVal, int rtVal, Sim4HeaderVersion2.CPUControl controlIn) {
        throw new UnsupportedOperationException("To be implemented in Sim4TestUtils.");
    }

    public static void dumpControlAluInputsOnly(Sim4HeaderVersion2.InstructionFields fieldsIn, Sim4HeaderVersion2.CPUControl controlIn) {
        throw new UnsupportedOperationException("To be implemented in Sim4TestUtils.");
    }

    public static void dumpControlAluCalcOnly(Sim4HeaderVersion2.InstructionFields fieldsIn, Sim4HeaderVersion2.CPUControl controlIn) {
        throw new UnsupportedOperationException("To be implemented in Sim4TestUtils.");
    }

    // Register Number Helpers
    public static int S_REG(int x) { return x + 16; }
    public static int T_REG(int x) { return (x < 8) ? (x + 8) : (x - 8 + 24); }
    public static int A_REG(int x) { return x + 4; }
    public static int V_REG(int x) { return x + 2; }

    public static final int RA_REG = 31;
    public static final int SP_REG = 29;
    public static final int FP_REG = 30;
    public static final int REG_ZERO = 0;

    // Instruction Encoding Functions
    public static int R_FORMAT(int funct, int rs, int rt, int rd, int shamt) {
        return R_FORMAT_X(0x00, funct, rs, rt, rd, shamt);
    }

    public static int R_FORMAT_X(int opcode, int funct, int rs, int rt, int rd, int shamt) {
        return ((opcode & 0x3F) << 26) |
                ((rs & 0x1F) << 21) |
                ((rt & 0x1F) << 16) |
                ((rd & 0x1F) << 11) |
                ((shamt & 0x1F) << 6) |
                (funct & 0x3F);
    }

    public static int I_FORMAT(int opcode, int rs, int rt, int imm16) {
        return ((opcode & 0x3F) << 26) |
                ((rs & 0x1F) << 21) |
                ((rt & 0x1F) << 16) |
                (imm16 & 0xFFFF);
    }

    public static int J_FORMAT(int opcode, int addr26) {
        return ((opcode & 0x3F) << 26) |
                (addr26 & 0x3FFFFFF);
    }

    // Instruction Macros (Converted to Methods)
    public static int NOP() { return 0; }

    public static int ADD(int rd, int rs, int rt) { return R_FORMAT(32, rs, rt, rd, 0); }
    public static int ADDU(int rd, int rs, int rt) { return R_FORMAT(33, rs, rt, rd, 0); }
    public static int SUB(int rd, int rs, int rt) { return R_FORMAT(34, rs, rt, rd, 0); }
    public static int SUBU(int rd, int rs, int rt) { return R_FORMAT(35, rs, rt, rd, 0); }
    public static int ADDI(int rt, int rs, int imm16) { return I_FORMAT(8, rs, rt, imm16); }
    public static int ADDIU(int rt, int rs, int imm16) { return I_FORMAT(9, rs, rt, imm16); }

    public static int MUL(int rd, int rs, int rt) { return R_FORMAT_X(0x1C, 2, rs, rt, rd, 0); }
    public static int MULT(int rs, int rt) { return R_FORMAT(24, rs, rt, 0, 0); }
    public static int MULTU(int rs, int rt) { return R_FORMAT(25, rs, rt, 0, 0); }
    public static int DIV(int rs, int rt) { return R_FORMAT(26, rs, rt, 0, 0); }
    public static int DIVU(int rs, int rt) { return R_FORMAT(27, rs, rt, 0, 0); }

    public static int MFHI(int rd) { return R_FORMAT(16, 0, 0, rd, 0); }
    public static int MFLO(int rd) { return R_FORMAT(18, 0, 0, rd, 0); }

    public static int AND(int rd, int rs, int rt) { return R_FORMAT(36, rs, rt, rd, 0); }
    public static int OR(int rd, int rs, int rt) { return R_FORMAT(37, rs, rt, rd, 0); }
    public static int XOR(int rd, int rs, int rt) { return R_FORMAT(38, rs, rt, rd, 0); }
    public static int NOR(int rd, int rs, int rt) { return R_FORMAT(39, rs, rt, rd, 0); }
    public static int ANDI(int rt, int rs, int imm16) { return I_FORMAT(12, rs, rt, imm16); }
    public static int ORI(int rt, int rs, int imm16) { return I_FORMAT(13, rs, rt, imm16); }
    public static int XORI(int rt, int rs, int imm16) { return I_FORMAT(14, rs, rt, imm16); }

    public static int LUI(int rt, int imm16) { return I_FORMAT(15, 0, rt, imm16); }

    public static int SLT(int rd, int rs, int rt) { return R_FORMAT(42, rs, rt, rd, 0); }
    public static int SLTI(int rt, int rs, int imm16) { return I_FORMAT(10, rs, rt, imm16); }

    public static int LW(int rt, int rs, int imm16) { return I_FORMAT(35, rs, rt, imm16); }
    public static int SW(int rt, int rs, int imm16) { return I_FORMAT(43, rs, rt, imm16); }

    public static int BEQ(int rs, int rt, int imm16) { return I_FORMAT(4, rs, rt, imm16); }
    public static int BNE(int rs, int rt, int imm16) { return I_FORMAT(5, rs, rt, imm16); }
    public static int J(int address) { return J_FORMAT(2, address); }

    public static int JAL(int address) { return J_FORMAT(3, address); }
    public static int JR(int rs) { return R_FORMAT(8, rs, 0, 0, 0); }
    public static int SYSCALL() { return R_FORMAT(12, 0, 0, 0, 0); }
}