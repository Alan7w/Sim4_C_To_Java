public class test_extraInst_DIV_MFLO {
    public static void main(String[] args) {
        // Create a CPUMemory instance
        CPUMemory cpuState = new CPUMemory();
        Sim4_test_commonCode sim4_test_commonCode = new Sim4_test_commonCode();

        // addi $s0, $zero, 5
        // addi $s1, $zero, 7
        cpuState.instMemory[0x100] = sim4_test_commonCode.ADDI(sim4_test_commonCode.S_REG(0), sim4_test_commonCode.REG_ZERO, 5);
        cpuState.instMemory[0x101] = sim4_test_commonCode.ADDI(sim4_test_commonCode.S_REG(1), sim4_test_commonCode.REG_ZERO, 7);

        // addi     $s2, $zero, 12345678  (pseudoinstruction)
        //     addi $s2, $zero, 0xbc
        //     addi $s2, $s2,$s2 (16 times)    - workaround for sll 16
        //     addi $s2, $s2, 0x614e
        cpuState.instMemory[0x102] = sim4_test_commonCode.ADDI(sim4_test_commonCode.S_REG(2), sim4_test_commonCode.REG_ZERO, 0xbc);
        for (int i = 0; i < 16; i++) {
            cpuState.instMemory[0x103 + i] = sim4_test_commonCode.ADD(sim4_test_commonCode.S_REG(2), sim4_test_commonCode.S_REG(2), sim4_test_commonCode.S_REG(2));
        }
        cpuState.instMemory[0x113] = sim4_test_commonCode.ADDI(sim4_test_commonCode.S_REG(2), sim4_test_commonCode.S_REG(2), 0x614e);

        // addi     $s3, $zero, 100000001 (pseudoinstruction)
        //     addi $s3, $zero, 0x5f6
        //     addi $s3, $s3,$s3 (16 times)    - workaround for sll 16
        //     addi $s3, $s3, 0xe101
        cpuState.instMemory[0x114] = sim4_test_commonCode.ADDI(sim4_test_commonCode.S_REG(3), sim4_test_commonCode.REG_ZERO, 0x5f6);
        for (int i = 0; i < 16; i++) {
            cpuState.instMemory[0x115 + i] = sim4_test_commonCode.ADD(sim4_test_commonCode.S_REG(3), sim4_test_commonCode.S_REG(3), sim4_test_commonCode.S_REG(3));
        }
        cpuState.instMemory[0x125] = sim4_test_commonCode.ADDI(sim4_test_commonCode.S_REG(3), sim4_test_commonCode.S_REG(3), 0xe101);

        // Many print instructions omitted for brevity...

        // div   $s2,$s0
        // mflo  $t0
        cpuState.instMemory[0x13e] = sim4_test_commonCode.DIV(sim4_test_commonCode.S_REG(2), sim4_test_commonCode.S_REG(0));
        cpuState.instMemory[0x13f] = sim4_test_commonCode.MFLO(sim4_test_commonCode.T_REG(0));

        // div   $s3,$s1
        // mflo  $t1
        cpuState.instMemory[0x140] = sim4_test_commonCode.DIV(sim4_test_commonCode.S_REG(3), sim4_test_commonCode.S_REG(1));
        cpuState.instMemory[0x141] = sim4_test_commonCode.MFLO(sim4_test_commonCode.T_REG(1));

        // Many more instructions omitted for brevity...

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