public class Sim4_test_commonCode {
    // Register number macros
    public static int S_REG(int x) { return x + 16; }
    public static int T_REG(int x) { return (x < 8) ? (x + 8) : (x - 8 + 24); }
    public static int A_REG(int x) { return x + 4; }
    public static int V_REG(int x) { return x + 2; }
    public static final int RA_REG = 31;
    public static final int SP_REG = 29;
    public static final int FP_REG = 30;
    public static final int REG_ZERO = 0;

    // Instruction encoding macros
    public static int R_FORMAT(int funct, int rs, int rt, int rd, int shamt) {
        return R_FORMAT_X(0x00, funct, rs, rt, rd, shamt);
    }

    public static int R_FORMAT_X(int opcode, int funct, int rs, int rt, int rd, int shamt) {
        return ((opcode & 0x3F) << 26) |
                ((rs & 0x1F) << 21) |
                ((rt & 0x1F) << 16) |
                ((rd & 0x1F) << 11) |
                ((shamt & 0x1F) << 6) |
                ((funct & 0x3F) << 0);
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

    // Encoded instruction macros
    public static final int NOP = 0;

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
    public static int SLTU(int rd, int rs, int rt) { return R_FORMAT(43, rs, rt, rd, 0); }
    public static int SLTI(int rt, int rs, int imm16) { return I_FORMAT(10, rs, rt, imm16); }
    public static int SLTIU(int rt, int rs, int imm16) { return I_FORMAT(11, rs, rt, imm16); }

    public static int LW(int rt, int rs, int imm16) { return I_FORMAT(35, rs, rt, imm16); }
    public static int SW(int rt, int rs, int imm16) { return I_FORMAT(43, rs, rt, imm16); }
    public static int LB(int rt, int rs, int imm16) { return I_FORMAT(32, rs, rt, imm16); }
    public static int SB(int rt, int rs, int imm16) { return I_FORMAT(40, rs, rt, imm16); }

    public static int BEQ(int rs, int rt, int imm16) { return I_FORMAT(4, rs, rt, imm16); }
    public static int BNE(int rs, int rt, int imm16) { return I_FORMAT(5, rs, rt, imm16); }
    public static int J(int address) { return J_FORMAT(2, address); }
    public static int JAL(int address) { return J_FORMAT(3, address); }
    public static int JR(int rs) { return R_FORMAT(8, rs, 0, 0, 0); }
    public static int SYSCALL() { return R_FORMAT(12, 0, 0, 0, 0); }


    public void dumpPCInstruction(int pc, int inst) {
        System.out.printf("PC: 0x%04x_%04x   Instruction: 0x%04x_%04x\n",
                (pc >> 16) & 0xFFFF, pc & 0xFFFF,
                (inst >> 16) & 0xFFFF, inst & 0xFFFF);
    }

    public static void dumpFields(InstructionFields fields) {
        System.out.println("  --");
        System.out.printf("  opcode  (6 bits)=0x%02x\n", fields.opcode);
        System.out.printf("  rs      (5 bits)=0x%02x\n", fields.rs);
        System.out.printf("  rt      (5 bits)=0x%02x\n", fields.rt);
        System.out.printf("  rd      (5 bits)=0x%02x\n", fields.rd);
        System.out.printf("  shamt   (5 bits)=0x%02x\n", fields.shamt);
        System.out.printf("  funct   (6 bits)=0x%02x\n", fields.funct);
        System.out.printf("  imm16  (16 bits)=0x     %04x\n", fields.imm16);
        System.out.printf("  imm32  (32 bits)=0x%04x_%04x\n",
                (fields.imm32 >> 16) & 0xFFFF, fields.imm32 & 0xFFFF);
        System.out.printf("  addr   (26 bits)=0x %03x_%04x\n",
                (fields.address >> 16) & 0xFFFF, fields.address & 0xFFFF);
    }

    public static void dumpControl(int rsVal, int rtVal, CPUControl control) {
        System.out.println("  --");
        System.out.printf("  rsVal=0x%04x_%04x\n", (rsVal >> 16) & 0xFFFF, rsVal & 0xFFFF);
        System.out.printf("  rtVal=0x%04x_%04x\n", (rtVal >> 16) & 0xFFFF, rtVal & 0xFFFF);
        System.out.println("  --");
        System.out.printf("  ALUsrc     =%d\n", control.ALUsrc);
        System.out.printf("  ALU.op     =%d\n", control.ALU.op);
        System.out.printf("  ALU.bNegate=%d\n", control.ALU.bNegate);
        System.out.printf("  memRead    =%d\n", control.memRead);
        System.out.printf("  memWrite   =%d\n", control.memWrite);
        System.out.printf("  memToReg   =%d\n", control.memToReg);
        System.out.printf("  regDst     =%d\n", control.regDst);
        System.out.printf("  regWrite   =%d\n", control.regWrite);
        System.out.printf("  branch     =%d\n", control.branch);
        System.out.printf("  jump       =%d\n", control.jump);
    }

    public void dumpControlAluInputsOnly(InstructionFields fields, CPUControl control) {
        System.out.printf("  rs      (5 bits)=0x%02x\n", fields.rs);
        System.out.printf("  rt      (5 bits)=0x%02x\n", fields.rt);
        System.out.printf("  imm16  (16 bits)=0x     %04x\n", fields.imm16);
        System.out.printf("  imm32  (32 bits)=0x%04x_%04x\n",
                (fields.imm32 >> 16) & 0xFFFF, fields.imm32 & 0xFFFF);
        System.out.printf("  ALUsrc     =%d\n", control.ALUsrc);
    }

    public void dumpControlAluCalcOnly(InstructionFields fields, CPUControl control) {
        dumpControlAluInputsOnly(fields, control);
        System.out.printf("  ALU.op     =%d\n", control.ALU.op);
        System.out.printf("  ALU.bNegate=%d\n", control.ALU.bNegate);
    }

    public int executeSingleCycleCPU(int[] regs, int[] instMemory, int[] dataMemory, int[] pc, int debug) {
        int i;

        int inst;
        InstructionFields fields = new InstructionFields();
        int rsVal, rtVal, reg32, reg33;
        CPUControl control = new CPUControl();
        int aluInput1, aluInput2;
        ALUResult aluResult = new ALUResult();
        MemResult memResult = new MemResult();

        int fail = 0;

        int pc_save;
        int[] regs_save = new int[34];
        int[] dataMem_save = new int[dataMemory.length];

        InstructionFields fields_save = new InstructionFields();
        CPUControl control_save = new CPUControl();
        ALUResult aluResult_save = new ALUResult();
        MemResult memResult_save = new MemResult();

        pc_save = pc[0];
        System.arraycopy(regs, 0, regs_save, 0, regs.length);
        System.arraycopy(dataMemory, 0, dataMem_save, 0, dataMemory.length);

        inst = Sim4.getInstruction(pc[0], instMemory);

        if (inst == SYSCALL()) {
            if (debug > 0) System.out.println("[systemCall]");
            pc[0] += 4;
            return execSyscall(regs, instMemory, dataMemory, pc, debug);
        }

        Sim4.extractInstructionFields(inst, fields);
        fields_save = new InstructionFields();
        fields_save.opcode = fields.opcode;
        fields_save.rs = fields.rs;
        fields_save.rt = fields.rt;
        fields_save.rd = fields.rd;
        fields_save.shamt = fields.shamt;
        fields_save.funct = fields.funct;
        fields_save.imm16 = fields.imm16;
        fields_save.imm32 = fields.imm32;
        fields_save.address = fields.address;

        rsVal = regs[fields.rs];
        rtVal = regs[fields.rt];
        reg32 = regs[32];
        reg33 = regs[33];

        int ok = Sim4.fillCPUControl(fields, control);
        control_save = new CPUControl();
        control_save.ALUsrc = control.ALUsrc;
        control_save.ALU.op = control.ALU.op;
        control_save.ALU.bNegate = control.ALU.bNegate;
        control_save.memRead = control.memRead;
        control_save.memWrite = control.memWrite;
        control_save.memToReg = control.memToReg;
        control_save.regDst = control.regDst;
        control_save.regWrite = control.regWrite;
        control_save.branch = control.branch;
        control_save.jump = control.jump;
        control_save.extra1 = control.extra1;
        control_save.extra2 = control.extra2;
        control_save.extra3 = control.extra3;

        if (debug > 0) {
            System.out.printf("Opcode: 0x%02x   at 0x%04x_%04x\n", fields.opcode, (pc[0] >> 16) & 0xffff, pc[0] & 0xffff);
            if (fields.opcode == 0) System.out.printf("  funct: 0x%02x\n", fields.funct);
        }

        if (ok == 0) {
            System.out.println("fill_CPUControl() returned 0");
            return 1;
        }

        if (debug >= 2) {
            dumpFields(fields);
            dumpControl(rsVal, rtVal, control);
        }

        fields.opcode = -1;
        fields_save.opcode = -1;
        fields.funct = -1;
        fields_save.funct = -1;

        aluInput1 = Sim4.getALUinput1(control, fields, rsVal, rtVal, reg32, reg33, pc_save);
        aluInput2 = Sim4.getALUinput2(control, fields, rsVal, rtVal, reg32, reg33, pc_save);

        Sim4.executeALU(control, aluInput1, aluInput2, aluResult);
        aluResult_save = new ALUResult();
        aluResult_save.result = aluResult.result;
        aluResult_save.zero = aluResult.zero;
        aluResult_save.extra = aluResult.extra;

        if (debug >= 2) {
            System.out.println("  --");
            System.out.printf("  ALU.result=0x%04x_%04x\n", (aluResult.result >> 16) & 0xffff, aluResult.result & 0xffff);
            System.out.printf("  ALU.zero  =%d\n", aluResult.zero);
        }

        Sim4.executeMEM(control, aluResult, rsVal, rtVal, dataMemory, memResult);
        memResult_save = new MemResult();
        memResult_save.readVal = memResult.readVal;

        if (debug >= 2) {
            System.out.println("  --");
            System.out.printf("  memResult.readVal=0x%04x_%04x\n", (memResult.readVal >> 16) & 0xffff, memResult.readVal & 0xffff);
            System.out.println("  --");
        }

        pc[0] = Sim4.getNextPC(fields, control, aluResult.zero, rsVal, rtVal, pc[0]);
        if (pc[0] == pc_save) {
            System.out.println("ERROR: The PC did not change!");
            fail = 1;
        }

        if ((pc[0] & 0x3) != 0) {
            System.out.printf("ERROR: The new Program Counter 0x%04x_%04x is not a multiple of 4!\n", (pc[0] >> 16) & 0xffff, pc[0] & 0xffff);
            return 1;
        }

        Sim4.executeUpdateRegs(fields, control, aluResult, memResult, regs);

        // AFTER THE INSTRUCTION: COMPARE THE VARIOUS STRUCTS

        // Ensure that instruction fields have not changed after execution
        if (fields_save.opcode != fields.opcode || fields_save.funct != fields.funct) {
            System.out.println("ERROR: The Instruction Fields struct changed somewhere in the testcase, after the original call to extractInstructionFields().");
        }

        // Ensure control structure remains unchanged (except for extra fields)
        if (control_save.ALUsrc != control.ALUsrc ||
                control_save.ALU.op != control.ALU.op ||
                control_save.ALU.bNegate != control.ALU.bNegate ||
                control_save.memRead != control.memRead ||
                control_save.memWrite != control.memWrite ||
                control_save.memToReg != control.memToReg ||
                control_save.regDst != control.regDst ||
                control_save.regWrite != control.regWrite ||
                control_save.branch != control.branch ||
                control_save.jump != control.jump) {
            System.out.println("ERROR: The Control struct changed somewhere in the testcase, after the original call to fillCPUControl().");
        }

        // Ensure ALU result remains unchanged
        if (aluResult_save.result != aluResult.result || aluResult_save.zero != aluResult.zero) {
            System.out.println("ERROR: The ALUResult struct changed somewhere in the testcase, after the original call to executeALU().");
        }

        // Ensure memory read result remains unchanged
        if (memResult_save.readVal != memResult.readVal) {
            System.out.println("ERROR: The MemResult struct changed somewhere in the testcase, after the original call to executeMEM().");
        }

        // Debugging check: Ensure register $zero remains unchanged
        if (regs[0] != 0) {
            System.out.printf("ERROR: The $zero register was changed to 0x%08x\n", regs[0]);
        }

        // Debugging output for register and memory changes
        if (debug > 0) {
            System.out.printf("  PC was: 0x%04x_%04x is: 0x%04x_%04x\n",
                    (pc_save >> 16) & 0xFFFF, pc_save & 0xFFFF,
                    (pc[0] >> 16) & 0xFFFF, pc[0] & 0xFFFF);

            for (i = 0; i < 34; i++) {
                if (regs_save[i] != regs[i]) {
                    System.out.printf("  reg[%2d] was: 0x%04x_%04x is 0x%04x_%04x\n",
                            i,
                            (regs_save[i] >> 16) & 0xFFFF, regs_save[i] & 0xFFFF,
                            (regs[i] >> 16) & 0xFFFF, regs[i] & 0xFFFF);
                }
            }

            for (i = 0; i < dataMem_save.length; i++) {
                if (dataMem_save[i] != dataMemory[i]) {
                    System.out.printf("  mem[%2d] was: 0x%04x_%04x is 0x%04x_%04x\n",
                            i,
                            (dataMem_save[i] >> 16) & 0xFFFF, dataMem_save[i] & 0xFFFF,
                            (dataMemory[i] >> 16) & 0xFFFF, dataMemory[i] & 0xFFFF);
                }
            }
            System.out.println();
        }

        return fail;
    }

    public int execSyscall(int[] regs, int[] instMemory, int[] dataMemory, int[] pc, int debug) {
        int v0 = regs[2];
        int a0 = regs[4];

        // syscall 10: exit
        if (v0 == 10) {
            System.out.println("--- syscall 10 executed: Normal termination of the assembly language program.");
            return 1;
        }

        // syscall 1: print_int
        if (v0 == 1) {
            System.out.printf("%d", a0);
        }
        // syscall 11: print_char
        else if (v0 == 11) {
            System.out.printf("%c", a0);
        }
        // syscall 4: print_str
        else if (v0 == 4) {
            System.out.printf("%s", new String(dataMemory, a0, dataMemory.length - a0));
        }
        // unrecognized syscall
        else {
            System.out.printf("--- ERROR: Unrecognized syscall $v0=%d\n", v0);
        }

        return 0;
    }
}