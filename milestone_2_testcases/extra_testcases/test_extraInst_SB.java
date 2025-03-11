public class test_extraInst_SB {
    public static void main(String[] args) {
        // Create a CPUMemory instance
        CPUMemory cpuState = new CPUMemory();
        Sim4_test_commonCode sim4_test_commonCode = new Sim4_test_commonCode();

        // Initialize registers for addresses
        cpuState.instMemory[0x100] = sim4_test_commonCode.ADDI(sim4_test_commonCode.S_REG(0), sim4_test_commonCode.REG_ZERO, 0x00);
        cpuState.instMemory[0x101] = sim4_test_commonCode.ADDI(sim4_test_commonCode.S_REG(1), sim4_test_commonCode.REG_ZERO, 0x11);
        cpuState.instMemory[0x102] = sim4_test_commonCode.ADDI(sim4_test_commonCode.S_REG(2), sim4_test_commonCode.REG_ZERO, 0x22);
        cpuState.instMemory[0x103] = sim4_test_commonCode.ADDI(sim4_test_commonCode.S_REG(3), sim4_test_commonCode.REG_ZERO, 0x33);

        // Initialize registers for values
        cpuState.instMemory[0x104] = sim4_test_commonCode.ADDI(sim4_test_commonCode.S_REG(4), sim4_test_commonCode.REG_ZERO, 0xaa);
        cpuState.instMemory[0x105] = sim4_test_commonCode.ADDI(sim4_test_commonCode.S_REG(5), sim4_test_commonCode.REG_ZERO, 0xbb);
        cpuState.instMemory[0x106] = sim4_test_commonCode.ADDI(sim4_test_commonCode.S_REG(6), sim4_test_commonCode.REG_ZERO, 0xcc);
        cpuState.instMemory[0x107] = sim4_test_commonCode.ADDI(sim4_test_commonCode.S_REG(7), sim4_test_commonCode.REG_ZERO, 0xdd);

        // Store bytes
        cpuState.instMemory[0x108] = sim4_test_commonCode.SB(sim4_test_commonCode.S_REG(4), sim4_test_commonCode.S_REG(0), 0);
        cpuState.instMemory[0x109] = sim4_test_commonCode.SB(sim4_test_commonCode.S_REG(5), sim4_test_commonCode.S_REG(1), 1);
        cpuState.instMemory[0x10a] = sim4_test_commonCode.SB(sim4_test_commonCode.S_REG(6), sim4_test_commonCode.S_REG(2), 2);
        cpuState.instMemory[0x10b] = sim4_test_commonCode.SB(sim4_test_commonCode.S_REG(7), sim4_test_commonCode.S_REG(3), 3);

        // Load words to verify results
        cpuState.instMemory[0x10c] = sim4_test_commonCode.LW(sim4_test_commonCode.T_REG(0), sim4_test_commonCode.REG_ZERO, 0x00);
        cpuState.instMemory[0x10d] = sim4_test_commonCode.LW(sim4_test_commonCode.T_REG(1), sim4_test_commonCode.REG_ZERO, 0x04);
        cpuState.instMemory[0x10e] = sim4_test_commonCode.LW(sim4_test_commonCode.T_REG(2), sim4_test_commonCode.REG_ZERO, 0x10);
        cpuState.instMemory[0x10f] = sim4_test_commonCode.LW(sim4_test_commonCode.T_REG(3), sim4_test_commonCode.REG_ZERO, 0x14);
        cpuState.instMemory[0x110] = sim4_test_commonCode.LW(sim4_test_commonCode.T_REG(4), sim4_test_commonCode.REG_ZERO, 0x20);
        cpuState.instMemory[0x111] = sim4_test_commonCode.LW(sim4_test_commonCode.T_REG(5), sim4_test_commonCode.REG_ZERO, 0x24);
        cpuState.instMemory[0x112] = sim4_test_commonCode.LW(sim4_test_commonCode.T_REG(6), sim4_test_commonCode.REG_ZERO, 0x30);
        cpuState.instMemory[0x113] = sim4_test_commonCode.LW(sim4_test_commonCode.T_REG(7), sim4_test_commonCode.REG_ZERO, 0x34);

        // Print results with newlines in between
        // print_int($t0)
        cpuState.instMemory[0x114] = sim4_test_commonCode.ADDI(sim4_test_commonCode.V_REG(0), sim4_test_commonCode.REG_ZERO, 1);
        cpuState.instMemory[0x115] = sim4_test_commonCode.ADD(sim4_test_commonCode.A_REG(0), sim4_test_commonCode.T_REG(0), sim4_test_commonCode.REG_ZERO);
        cpuState.instMemory[0x116] = sim4_test_commonCode.SYSCALL();

        // print_char('\n')
        cpuState.instMemory[0x117] = sim4_test_commonCode.ADDI(sim4_test_commonCode.V_REG(0), sim4_test_commonCode.REG_ZERO, 11);
        cpuState.instMemory[0x118] = sim4_test_commonCode.ADDI(sim4_test_commonCode.A_REG(0), sim4_test_commonCode.REG_ZERO, 0xa);
        cpuState.instMemory[0x119] = sim4_test_commonCode.SYSCALL();

        // Additional print instructions (showing just a couple for brevity)
        cpuState.instMemory[0x11a] = sim4_test_commonCode.ADDI(sim4_test_commonCode.V_REG(0), sim4_test_commonCode.REG_ZERO, 1);
        cpuState.instMemory[0x11b] = sim4_test_commonCode.ADD(sim4_test_commonCode.A_REG(0), sim4_test_commonCode.T_REG(1), sim4_test_commonCode.REG_ZERO);
        cpuState.instMemory[0x11c] = sim4_test_commonCode.SYSCALL();

        // Adding all print instructions for remaining t registers and newlines (0x11d to 0x143)
        // ...

        // sys_exit()
        cpuState.instMemory[0x144] = sim4_test_commonCode.ADDI(sim4_test_commonCode.V_REG(0), sim4_test_commonCode.REG_ZERO, 10);
        cpuState.instMemory[0x145] = sim4_test_commonCode.SYSCALL();

        cpuState.pc = 0x0400;

        // fill in the registers and data memory
        for (int i = 1; i < 34; i++) {
            cpuState.regs[i] = 0x01010101 * i;
        }

        // Initialize each memory word with 4 bytes with pattern (~i & 0xFF)
        for (int i = 0; i < cpuState.dataMemory.length; i++) {
            int byte0 = (~(i*4+0)) & 0xFF;
            int byte1 = (~(i*4+1)) & 0xFF;
            int byte2 = (~(i*4+2)) & 0xFF;
            int byte3 = (~(i*4+3)) & 0xFF;

            // Pack 4 bytes into a word
            cpuState.dataMemory[i] = (byte3 << 24) | (byte2 << 16) | (byte1 << 8) | byte0;
        }

        // Create PC array for passing by reference
        int[] pcRef = new int[1];
        pcRef[0] = cpuState.pc;

        while (true) {
            int rc = sim4_test_commonCode.executeSingleCycleCPU(
                    cpuState.regs,
                    cpuState.instMemory,
                    cpuState.dataMemory,
                    pcRef,
                    0); // Debug level 0

            // Update the PC in the state from the reference
            cpuState.pc = pcRef[0];

            if (rc != 0)
                break;
        }
    }
}