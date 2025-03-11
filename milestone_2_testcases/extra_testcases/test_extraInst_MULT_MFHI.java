public class test_extraInst_MULT_MFHI {
    public static void main(String[] args) {
        // Create a CPUMemory instance
        CPUMemory cpuState = new CPUMemory();
        Sim4_test_commonCode sim4_test_commonCode = new Sim4_test_commonCode();

        // addi $s0, $zero, 5
        // addi $s1, $zero, 7
        cpuState.instMemory[0x100] = sim4_test_commonCode.ADDI(sim4_test_commonCode.S_REG(0), sim4_test_commonCode.REG_ZERO, 5);
        cpuState.instMemory[0x101] = sim4_test_commonCode.ADDI(sim4_test_commonCode.S_REG(1), sim4_test_commonCode.REG_ZERO, 7);

        // Setup for large values using workaround for sll 16
        // First large value
        cpuState.instMemory[0x102] = sim4_test_commonCode.ADDI(sim4_test_commonCode.S_REG(2), sim4_test_commonCode.REG_ZERO, 0xbc);
        for (int i = 0; i < 16; i++) {
            cpuState.instMemory[0x103 + i] = sim4_test_commonCode.ADD(sim4_test_commonCode.S_REG(2), sim4_test_commonCode.S_REG(2), sim4_test_commonCode.S_REG(2));
        }
        cpuState.instMemory[0x113] = sim4_test_commonCode.ADDI(sim4_test_commonCode.S_REG(2), sim4_test_commonCode.S_REG(2), 0x614e);

        // Second large value
        cpuState.instMemory[0x114] = sim4_test_commonCode.ADDI(sim4_test_commonCode.S_REG(3), sim4_test_commonCode.REG_ZERO, 0x5f6);
        for (int i = 0; i < 16; i++) {
            cpuState.instMemory[0x115 + i] = sim4_test_commonCode.ADD(sim4_test_commonCode.S_REG(3), sim4_test_commonCode.S_REG(3), sim4_test_commonCode.S_REG(3));
        }
        cpuState.instMemory[0x125] = sim4_test_commonCode.ADDI(sim4_test_commonCode.S_REG(3), sim4_test_commonCode.S_REG(3), 0xe101);

        // Print initial values
        // print_int($s0)
        cpuState.instMemory[0x126] = sim4_test_commonCode.ADDI(sim4_test_commonCode.V_REG(0), sim4_test_commonCode.REG_ZERO, 1);
        cpuState.instMemory[0x127] = sim4_test_commonCode.ADD(sim4_test_commonCode.A_REG(0), sim4_test_commonCode.S_REG(0), sim4_test_commonCode.REG_ZERO);
        cpuState.instMemory[0x128] = sim4_test_commonCode.SYSCALL();

        // print_char('\n')
        cpuState.instMemory[0x129] = sim4_test_commonCode.ADDI(sim4_test_commonCode.V_REG(0), sim4_test_commonCode.REG_ZERO, 11);
        cpuState.instMemory[0x12a] = sim4_test_commonCode.ADDI(sim4_test_commonCode.A_REG(0), sim4_test_commonCode.REG_ZERO, 0xa);
        cpuState.instMemory[0x12b] = sim4_test_commonCode.SYSCALL();

        // print_int($s1)
        cpuState.instMemory[0x12c] = sim4_test_commonCode.ADDI(sim4_test_commonCode.V_REG(0), sim4_test_commonCode.REG_ZERO, 1);
        cpuState.instMemory[0x12d] = sim4_test_commonCode.ADD(sim4_test_commonCode.A_REG(0), sim4_test_commonCode.S_REG(1), sim4_test_commonCode.REG_ZERO);
        cpuState.instMemory[0x12e] = sim4_test_commonCode.SYSCALL();

        // print_char('\n')
        cpuState.instMemory[0x12f] = sim4_test_commonCode.ADDI(sim4_test_commonCode.V_REG(0), sim4_test_commonCode.REG_ZERO, 11);
        cpuState.instMemory[0x130] = sim4_test_commonCode.ADDI(sim4_test_commonCode.A_REG(0), sim4_test_commonCode.REG_ZERO, 0xa);
        cpuState.instMemory[0x131] = sim4_test_commonCode.SYSCALL();

        // print_int($s2)
        cpuState.instMemory[0x132] = sim4_test_commonCode.ADDI(sim4_test_commonCode.V_REG(0), sim4_test_commonCode.REG_ZERO, 1);
        cpuState.instMemory[0x133] = sim4_test_commonCode.ADD(sim4_test_commonCode.A_REG(0), sim4_test_commonCode.S_REG(2), sim4_test_commonCode.REG_ZERO);
        cpuState.instMemory[0x134] = sim4_test_commonCode.SYSCALL();

        // print_char('\n')
        cpuState.instMemory[0x135] = sim4_test_commonCode.ADDI(sim4_test_commonCode.V_REG(0), sim4_test_commonCode.REG_ZERO, 11);
        cpuState.instMemory[0x136] = sim4_test_commonCode.ADDI(sim4_test_commonCode.A_REG(0), sim4_test_commonCode.REG_ZERO, 0xa);
        cpuState.instMemory[0x137] = sim4_test_commonCode.SYSCALL();

        // print_int($s3)
        cpuState.instMemory[0x138] = sim4_test_commonCode.ADDI(sim4_test_commonCode.V_REG(0), sim4_test_commonCode.REG_ZERO, 1);
        cpuState.instMemory[0x139] = sim4_test_commonCode.ADD(sim4_test_commonCode.A_REG(0), sim4_test_commonCode.S_REG(3), sim4_test_commonCode.REG_ZERO);
        cpuState.instMemory[0x13a] = sim4_test_commonCode.SYSCALL();

        // print_char('\n')
        cpuState.instMemory[0x13b] = sim4_test_commonCode.ADDI(sim4_test_commonCode.V_REG(0), sim4_test_commonCode.REG_ZERO, 11);
        cpuState.instMemory[0x13c] = sim4_test_commonCode.ADDI(sim4_test_commonCode.A_REG(0), sim4_test_commonCode.REG_ZERO, 0xa);
        cpuState.instMemory[0x13d] = sim4_test_commonCode.SYSCALL();

        // mult  $s0,$s1
        // mfhi  $t0
        cpuState.instMemory[0x13e] = sim4_test_commonCode.MULT(sim4_test_commonCode.S_REG(0), sim4_test_commonCode.S_REG(1));
        cpuState.instMemory[0x13f] = sim4_test_commonCode.MFHI(sim4_test_commonCode.T_REG(0));

        // mult  $s2,$s3
        // mfhi  $t1
        cpuState.instMemory[0x140] = sim4_test_commonCode.MULT(sim4_test_commonCode.S_REG(2), sim4_test_commonCode.S_REG(3));
        cpuState.instMemory[0x141] = sim4_test_commonCode.MFHI(sim4_test_commonCode.T_REG(1));

        // print_int($t0)
        cpuState.instMemory[0x142] = sim4_test_commonCode.ADDI(sim4_test_commonCode.V_REG(0), sim4_test_commonCode.REG_ZERO, 1);
        cpuState.instMemory[0x143] = sim4_test_commonCode.ADD(sim4_test_commonCode.A_REG(0), sim4_test_commonCode.T_REG(0), sim4_test_commonCode.REG_ZERO);
        cpuState.instMemory[0x144] = sim4_test_commonCode.SYSCALL();

        // print_char('\n')
        cpuState.instMemory[0x145] = sim4_test_commonCode.ADDI(sim4_test_commonCode.V_REG(0), sim4_test_commonCode.REG_ZERO, 11);
        cpuState.instMemory[0x146] = sim4_test_commonCode.ADDI(sim4_test_commonCode.A_REG(0), sim4_test_commonCode.REG_ZERO, 0xa);
        cpuState.instMemory[0x147] = sim4_test_commonCode.SYSCALL();

        // print_int($t1)
        cpuState.instMemory[0x148] = sim4_test_commonCode.ADDI(sim4_test_commonCode.V_REG(0), sim4_test_commonCode.REG_ZERO, 1);
        cpuState.instMemory[0x149] = sim4_test_commonCode.ADD(sim4_test_commonCode.A_REG(0), sim4_test_commonCode.T_REG(1), sim4_test_commonCode.REG_ZERO);
        cpuState.instMemory[0x14a] = sim4_test_commonCode.SYSCALL();

        // print_char('\n')
        cpuState.instMemory[0x14b] = sim4_test_commonCode.ADDI(sim4_test_commonCode.V_REG(0), sim4_test_commonCode.REG_ZERO, 11);
        cpuState.instMemory[0x14c] = sim4_test_commonCode.ADDI(sim4_test_commonCode.A_REG(0), sim4_test_commonCode.REG_ZERO, 0xa);
        cpuState.instMemory[0x14d] = sim4_test_commonCode.SYSCALL();

        // sys_exit()
        cpuState.instMemory[0x14e] = sim4_test_commonCode.ADDI(sim4_test_commonCode.V_REG(0), sim4_test_commonCode.REG_ZERO, 10);
        cpuState.instMemory[0x14f] = sim4_test_commonCode.SYSCALL();

        cpuState.pc = 0x0400;

        // fill in the registers and data memory
        for (int i = 1; i < 34; i++) {
            cpuState.regs[i] = 0x01010101 * i;
        }
        for (int i = 1; i < cpuState.dataMemory.length; i++) {
            cpuState.dataMemory[i] = 0xffff0000 + i * 4;
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