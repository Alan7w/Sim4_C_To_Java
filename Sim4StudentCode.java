public class Sim4StudentCode {

    // Method implementations for students to complete
    public static int getInstruction(int curPC, int[] instructionMemory) {
        // TODO: Implement this function
        return 0;
    }

    public static void extractInstructionFields(int instruction, Sim4HeaderVersion2.InstructionFields fieldsOut) {
        // TODO: Implement this function
    }

    public static int fillCPUControl(Sim4HeaderVersion2.InstructionFields fields, Sim4HeaderVersion2.CPUControl controlOut) {
        // TODO: Implement this function
        return 0;
    }

    public static int getALUinput1(Sim4HeaderVersion2.CPUControl controlIn, Sim4HeaderVersion2.InstructionFields fieldsIn,
                                   int rsVal, int rtVal, int reg32, int reg33, int oldPC) {
        // TODO: Implement this function
        return 0;
    }

    public static int getALUinput2(Sim4HeaderVersion2.CPUControl controlIn, Sim4HeaderVersion2.InstructionFields fieldsIn,
                                   int rsVal, int rtVal, int reg32, int reg33, int oldPC) {
        // TODO: Implement this function
        return 0;
    }

    public static void executeALU(Sim4HeaderVersion2.CPUControl controlIn, int input1, int input2, Sim4HeaderVersion2.ALUResult aluResultOut) {
        // TODO: Implement this function
    }

    public static void executeMEM(Sim4HeaderVersion2.CPUControl controlIn, Sim4HeaderVersion2.ALUResult aluResultIn,
                                  int rsVal, int rtVal, int[] memory, Sim4HeaderVersion2.MemResult resultOut) {
        // TODO: Implement this function
    }

    public static int getNextPC(Sim4HeaderVersion2.InstructionFields fields, Sim4HeaderVersion2.CPUControl controlIn, int aluZero,
                                int rsVal, int rtVal, int oldPC) {
        // TODO: Implement this function
        return 0;
    }

    public static void executeUpdateRegs(Sim4HeaderVersion2.InstructionFields fields, Sim4HeaderVersion2.CPUControl controlIn,
                                         Sim4HeaderVersion2.ALUResult aluResultIn, Sim4HeaderVersion2.MemResult memResultIn, int[] regs) {
        // TODO: Implement this function
    }
}