public class Sim4HeaderVersion1 {

    public static class InstructionFields {int opcode; int rs;
                                        int rt;        int rd;
                                        int shamt;     int funct;
                                        int imm16, imm32;   int address;
    }

    public static class CPUControl {ALUControl ALU = new ALUControl();
                                    int ALUsrc;     int memRead;
                                    int memWrite;   int memToReg;
                                    int regDst;     int regWrite;
                                    int branch;     int jump;
                                    int extra1, extra2, extra3;
                                    public static class ALUControl {int op; int bNegate;}
    }

    public static class ALUResult {int result; int zero; int extra;}

    public static class MemResult {int readVal;}

    public static int signExtend16to32(int val16) {
        return (val16 & 0x8000) != 0 ? val16 | 0xFFFF0000 : val16;
    }


    // Method signatures corresponding to function prototypes in sim4.h
    public static int getInstruction(int curPC, int[] instructionMemory) {
        // TODO: Your code goes here
        return 0;
    }

    public static void extractInstructionFields(int instruction, InstructionFields fieldsOut) {
        // TODO: Your code goes here
    }

    public static int fillCPUControl(InstructionFields fields, CPUControl controlOut) {
        // TODO: Your code goes here
        //  Implementation will decode fields and set control bits
        return 1; // Return 1 if recognized, 0 if invalid
    }

    public static int getALUinput1(CPUControl controlIn, InstructionFields fieldsIn,
                                   int rsVal, int rtVal, int reg32, int reg33, int oldPC) {
        // TODO: Your code goes here
        return 0;
    }

    public static int getALUinput2(CPUControl controlIn, InstructionFields fieldsIn,
                                   int rsVal, int rtVal, int reg32, int reg33, int oldPC) {
        // TODO: Your code goes here
        return 0;
    }

    public static void executeALU(CPUControl controlIn, int input1, int input2, ALUResult aluResultOut) {
        // TODO: Your code goes here
    }

    public static void executeMEM(CPUControl controlIn, ALUResult aluResultIn,
                                  int rsVal, int rtVal, int[] memory, MemResult resultOut) {
        // TODO: Your code goes here
    }

    public static int getNextPC(InstructionFields fields, CPUControl controlIn, int aluZero,
                                int rsVal, int rtVal, int oldPC) {
        // TODO: Your code goes here
        return 0; // Placeholder for correct branch/jump logic
    }

    public static void executeUpdateRegs(InstructionFields fields, CPUControl controlIn,
                                         ALUResult aluResultIn, MemResult memResultIn, int[] regs) {
        // TODO: Your code goes here
    }
}