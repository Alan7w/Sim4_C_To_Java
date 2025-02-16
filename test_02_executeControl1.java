public class test_02_executeControl1 {
    public static void main(String[] args) {
        CPUMemory state = new CPUMemory();

        state.instMemory[0] = Sim4_test_commonCode.ADD(Sim4_test_commonCode.S_REG(0), Sim4_test_commonCode.S_REG(1), Sim4_test_commonCode.S_REG(2));
        state.instMemory[1] = Sim4_test_commonCode.SUB(Sim4_test_commonCode.S_REG(3), Sim4_test_commonCode.S_REG(4), Sim4_test_commonCode.S_REG(5));
        state.instMemory[256] = Sim4_test_commonCode.ADDI(Sim4_test_commonCode.T_REG(2), Sim4_test_commonCode.T_REG(3), -1);
        state.instMemory[512] = Sim4_test_commonCode.ADDI(Sim4_test_commonCode.T_REG(4), Sim4_test_commonCode.T_REG(5), 16);

        for (state.pc = 0x0000; state.pc < 0x1000; state.pc += 4) {
            // Save original state
            CPUMemory stateOrig = new CPUMemory();
            System.arraycopy(state.instMemory, 0, stateOrig.instMemory, 0, state.instMemory.length);
            System.arraycopy(state.dataMemory, 0, stateOrig.dataMemory, 0, state.dataMemory.length);
            stateOrig.pc = state.pc;
            System.arraycopy(state.regs, 0, stateOrig.regs, 0, state.regs.length);

            int inst = Sim4.getInstruction(state.pc, state.instMemory);

            if (inst != 0) {
                InstructionFields fields = new InstructionFields();
                CPUControl control = new CPUControl();

                Sim4.extractInstructionFields(inst, fields);
                Sim4.fillCPUControl(fields, control);

                System.out.printf("PC: 0x%04x_%04x   Instruction: 0x%04x_%04x\n",
                        state.pc >> 16, state.pc & 0xffff,
                        inst >> 16, inst & 0xffff);

                Sim4_test_commonCode.dumpFields(fields);
                Sim4_test_commonCode.dumpControl(state.regs[fields.rs], state.regs[fields.rt], control);
            }

            if (!java.util.Arrays.equals(state.instMemory, stateOrig.instMemory) ||
                    !java.util.Arrays.equals(state.dataMemory, stateOrig.dataMemory) ||
                    state.pc != stateOrig.pc ||
                    !java.util.Arrays.equals(state.regs, stateOrig.regs)) {
                System.out.printf("ERROR: When the testcase called getInstruction() or executeControl() at PC=0x%08x, the 'state' struct was modified.\n", stateOrig.pc);
            }
        }
    }
}
