public class test_extraInst_LB {
    public static void main(String[] args) {
        // Create a CPUMemory instance
        CPUMemory cpuState = new CPUMemory();
        Sim4_test_commonCode sim4_test_commonCode = new Sim4_test_commonCode();

        // addi  $s0, $zero, 0x00
        // addi  $s1, $zero, 0x11
        // addi  $s2, $zero, 0x22
        // addi  $s3, $zero, 0x33
        cpuState.instMemory[0x100] = sim4_test_commonCode.ADDI(sim4_test_commonCode.S_REG(0), sim4_test_commonCode.REG_ZERO, 0x00);
        cpuState.instMemory[0x101] = sim4_test_commonCode.ADDI(sim4_test_commonCode.S_REG(1), sim4_test_commonCode.REG_ZERO, 0x11);
        cpuState.instMemory[0x102] = sim4_test_commonCode.ADDI(sim4_test_commonCode.S_REG(2), sim4_test_commonCode.REG_ZERO, 0x22);
        cpuState.instMemory[0x103] = sim4_test_commonCode.ADDI(sim4_test_commonCode.S_REG(3), sim4_test_commonCode.REG_ZERO, 0x33);

        // lb   $t4, 0($s0)
        // lb   $t5, 1($s1)
        // lb   $s2, 2($s2)
        // lb   $s3, 3($s3)
        cpuState.instMemory[0x104] = sim4_test_commonCode.LB(sim4_test_commonCode.T_REG(4), sim4_test_commonCode.S_REG(0), 0);
        cpuState.instMemory[0x105] = sim4_test_commonCode.LB(sim4_test_commonCode.T_REG(5), sim4_test_commonCode.S_REG(1), 1);
        cpuState.instMemory[0x106] = sim4_test_commonCode.LB(sim4_test_commonCode.S_REG(2), sim4_test_commonCode.S_REG(2), 2);
        cpuState.instMemory[0x107] = sim4_test_commonCode.LB(sim4_test_commonCode.S_REG(3), sim4_test_commonCode.S_REG(3), 3);

        // Many print instructions omitted for brevity...

        // sys_exit()
        cpuState.instMemory[0x12c] = sim4_test_commonCode.ADDI(sim4_test_commonCode.V_REG(0), sim4_test_commonCode.REG_ZERO, 10);
        cpuState.instMemory[0x12d] = sim4_test_commonCode.SYSCALL();

        cpuState.pc = 0x0400;

        // Fill in the registers and data memory
        for (int i = 1; i < 34; i++) {
            cpuState.regs[i] = 0x01010101 * i;
        }

        // In C, this is setting individual bytes in memory
        // We need to do this differently in Java since we don't have byte pointers
        // Initialize each word with 4 bytes (each byte is (~i) & 0xFF)
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