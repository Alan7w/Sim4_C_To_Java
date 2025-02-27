public class test_milestone1_03_executeControl2 {
    public static void main(String[] args) {
        CPUMemory state = new CPUMemory();

        state.instMemory[0] = Sim4_test_commonCode.ADDU(1, 0, 2);
        state.instMemory[1] = Sim4_test_commonCode.SUBU(3, 4, 5);
        state.instMemory[2] = Sim4_test_commonCode.ADDIU(6, 7, 0x1234);
        state.instMemory[3] = Sim4_test_commonCode.AND(9, 10, 11);
        state.instMemory[4] = Sim4_test_commonCode.OR(12, 13, 14);
        state.instMemory[5] = Sim4_test_commonCode.XOR(15, 16, 17);
        state.instMemory[7] = Sim4_test_commonCode.SLT(21, 22, 23);
        state.instMemory[8] = Sim4_test_commonCode.SLTI(24, 25, -1);
        state.instMemory[9] = Sim4_test_commonCode.LW(26, 0, 104);
        state.instMemory[10] = Sim4_test_commonCode.SW(29, 0, 108);
        state.instMemory[11] = Sim4_test_commonCode.BEQ(1, 0, 0x0104);
        state.instMemory[12] = Sim4_test_commonCode.J(0x01234567);
        state.instMemory[13] = Sim4_test_commonCode.J(0x03FFFFFF);

        for (int i = 0; i < 0x400; i++) {
            int inst = state.instMemory[i];

            if (inst != 0) {
                InstructionFields fields = new InstructionFields();
                CPUControl control = new CPUControl();

                Sim4.extractInstructionFields(inst, fields);
                Sim4.fillCPUControl(fields, control);

                System.out.printf("Instruction: 0x%04x_%04x\n",
                        (inst >> 16) & 0xFFFF, inst & 0xFFFF);

                Sim4_test_commonCode.dumpFields(fields);
                Sim4_test_commonCode.dumpControl(0, 0, control);
            }
        }
    }
}