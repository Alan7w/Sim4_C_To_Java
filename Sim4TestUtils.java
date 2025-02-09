public class Sim4TestUtils {

    public static void dumpPCInstruction(int pc, int inst) {
        System.out.printf("PC: 0x%04X_%04X   Instruction: 0x%04X_%04X%n",
                (pc >> 16) & 0xFFFF, pc & 0xFFFF,
                (inst >> 16) & 0xFFFF, inst & 0xFFFF);
    }

    public static void dumpFields(Sim4HeaderVersion2.InstructionFields fieldsIn) {
        System.out.println("  --");
        System.out.printf("  opcode  (6 bits)=0x%02X%n", fieldsIn.opcode);
        System.out.printf("  rs      (5 bits)=0x%02X%n", fieldsIn.rs);
        System.out.printf("  rt      (5 bits)=0x%02X%n", fieldsIn.rt);
        System.out.printf("  rd      (5 bits)=0x%02X%n", fieldsIn.rd);
        System.out.printf("  shamt   (5 bits)=0x%02X%n", fieldsIn.shamt);
        System.out.printf("  funct   (6 bits)=0x%02X%n", fieldsIn.funct);
        System.out.printf("  imm16  (16 bits)=0x%04X%n", fieldsIn.imm16);
        System.out.printf("  imm32  (32 bits)=0x%04X_%04X%n",
                (fieldsIn.imm32 >> 16) & 0xFFFF, fieldsIn.imm32 & 0xFFFF);
        System.out.printf("  addr   (26 bits)=0x%03X_%04X%n",
                (fieldsIn.address >> 16) & 0xFFFF, fieldsIn.address & 0xFFFF);
    }

    public static void dumpControl(int rsVal, int rtVal, Sim4HeaderVersion2.CPUControl controlIn) {
        System.out.println("  --");
        System.out.printf("  rsVal=0x%04X_%04X%n", (rsVal >> 16) & 0xFFFF, rsVal & 0xFFFF);
        System.out.printf("  rtVal=0x%04X_%04X%n", (rtVal >> 16) & 0xFFFF, rtVal & 0xFFFF);
        System.out.println("  --");
        System.out.printf("  ALUsrc     =%d%n", controlIn.ALUsrc);
        System.out.printf("  ALU.op     =%d%n", controlIn.ALU.op);
        System.out.printf("  ALU.bNegate=%d%n", controlIn.ALU.bNegate);
        System.out.printf("  memRead    =%d%n", controlIn.memRead);
        System.out.printf("  memWrite   =%d%n", controlIn.memWrite);
        System.out.printf("  memToReg   =%d%n", controlIn.memToReg);
        System.out.printf("  regDst     =%d%n", controlIn.regDst);
        System.out.printf("  regWrite   =%d%n", controlIn.regWrite);
        System.out.printf("  branch     =%d%n", controlIn.branch);
        System.out.printf("  jump       =%d%n", controlIn.jump);
    }

    public static void dumpControlAluInputsOnly(Sim4HeaderVersion2.InstructionFields fieldsIn, Sim4HeaderVersion2.CPUControl controlIn) {
        System.out.printf("  rs      (5 bits)=0x%02X%n", fieldsIn.rs);
        System.out.printf("  rt      (5 bits)=0x%02X%n", fieldsIn.rt);
        System.out.printf("  imm16  (16 bits)=0x%04X%n", fieldsIn.imm16);
        System.out.printf("  imm32  (32 bits)=0x%04X_%04X%n",
                (fieldsIn.imm32 >> 16) & 0xFFFF, fieldsIn.imm32 & 0xFFFF);
        System.out.printf("  ALUsrc     =%d%n", controlIn.ALUsrc);
    }

    public static void dumpControlAluCalcOnly(Sim4HeaderVersion2.InstructionFields fieldsIn, Sim4HeaderVersion2.CPUControl controlIn) {
        dumpControlAluInputsOnly(fieldsIn, controlIn);
        System.out.printf("  ALU.op     =%d%n", controlIn.ALU.op);
        System.out.printf("  ALU.bNegate=%d%n", controlIn.ALU.bNegate);
    }

    public static int executeSingleCycleCPU(Sim4TestCommonCode.CPUMemory cpuMemory, int debug) {
        int inst = Sim4HeaderVersion2.getInstruction(cpuMemory.pc, cpuMemory.instMemory);
        if (inst == 0) return 1; // Halt condition

        Sim4HeaderVersion2.InstructionFields fields = new Sim4HeaderVersion2.InstructionFields();
        Sim4HeaderVersion2.extractInstructionFields(inst, fields);

        int rsVal = cpuMemory.regs[fields.rs];
        int rtVal = cpuMemory.regs[fields.rt];
        int reg32 = cpuMemory.regs[32];
        int reg33 = cpuMemory.regs[33];

        Sim4HeaderVersion2.CPUControl control = new Sim4HeaderVersion2.CPUControl();
        if ((Sim4HeaderVersion2.fillCPUControl(fields, control)) != 1) {
            System.out.println("fillCPUControl() returned 0");
            return 1;
        }

        int aluInput1 = Sim4HeaderVersion2.getALUinput1(control, fields, rsVal, rtVal, reg32, reg33, cpuMemory.pc);
        int aluInput2 = Sim4HeaderVersion2.getALUinput2(control, fields, rsVal, rtVal, reg32, reg33, cpuMemory.pc);

        Sim4HeaderVersion2.ALUResult aluResult = new Sim4HeaderVersion2.ALUResult();
        Sim4HeaderVersion2.executeALU(control, aluInput1, aluInput2, aluResult);

        Sim4HeaderVersion2.MemResult memResult = new Sim4HeaderVersion2.MemResult();
        Sim4HeaderVersion2.executeMEM(control, aluResult, rsVal, rtVal, cpuMemory.dataMemory, memResult);

        cpuMemory.pc = Sim4HeaderVersion2.getNextPC(fields, control, aluResult.zero, rsVal, rtVal, cpuMemory.pc);

        Sim4HeaderVersion2.executeUpdateRegs(fields, control, aluResult, memResult, cpuMemory.regs);

        if (cpuMemory.regs[0] != 0) {
            System.out.printf("ERROR: The $zero register was changed to 0x%08X%n", cpuMemory.regs[0]);
        }

        return 0;
    }

    public static int execSyscall(Sim4TestCommonCode.CPUMemory cpuMemory, int debug) {
        int v0 = cpuMemory.regs[2];
        int a0 = cpuMemory.regs[4];

        switch (v0) {
            case 10:
                System.out.println("--- syscall 10 executed: Normal termination.");
                return 1;
            case 1:
                System.out.println(a0);
                break;
            case 11:
                System.out.print((char) a0);
                break;
            default:
                System.out.printf("--- ERROR: Unrecognized syscall $v0=%d%n", v0);
                break;
        }
        return 0;
    }
}