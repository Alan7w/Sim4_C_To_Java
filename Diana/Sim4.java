public class Sim4 {



	public int signExtend16to32(int val16) {

		if ((val16 & 0x8000) != 0)
			return val16 | 0xFFFF0000;
		else
			return val16;

	}


	// Method signatures corresponding to function prototypes in sim4.h
	public int getInstruction(int curPC, int[] instructionMemory) {
		// TODO: Your code goes here
		return instructionMemory[curPC / 4];
	}

	public void extractInstructionFields(int instruction, InstructionFields fieldsOut) {
		// TODO: Your code goes here
		fieldsOut.opcode = (instruction >> 26) & 0x3F;
		fieldsOut.rs = (instruction >> 21) & 0x1F;
		fieldsOut.rt = (instruction >> 16) & 0x1F;
		fieldsOut.rd = (instruction >> 11) & 0x1F;
		fieldsOut.shamt = (instruction >> 6) & 0x1F;
		fieldsOut.funct = instruction & 0x3F;
		fieldsOut.imm16 = instruction & 0xFFFF;
		fieldsOut.imm32 = signExtend16to32(fieldsOut.imm16);
		fieldsOut.address = instruction & 0x3FFFFFF;
	}

	public int fillCPUControl(InstructionFields fields, CPUControl controlOut) {
		// TODO: Your code goes here
		//  Implementation will decode fields and set control bits
		controlOut.ALUsrc = 0;
		controlOut.ALU.op = 0;
		controlOut.ALU.bNegate = 0;
		controlOut.memRead = 0;
		controlOut.memWrite = 0;
		controlOut.memToReg = 0;
		controlOut.regDst = 0;
		controlOut.regWrite = 0;
		controlOut.branch = 0;
		controlOut.jump = 0;

		switch (fields.opcode) {
		case 2: // J
			controlOut.jump = 1;
			break;

		case 4: // BEQ
		case 5: // BNE
			controlOut.ALU.op = 2; // SUB
			controlOut.ALU.bNegate = 1;
			controlOut.branch = 1;
			controlOut.extra1 = fields.opcode & 0x1;
			break;

		case 8: // ADDI
		case 9: // ADDIU
			controlOut.ALUsrc = 1;
			controlOut.ALU.op = 2; // ADD
			controlOut.regDst = 0;
			controlOut.regWrite = 1;
			break;

		case 10: // SLTI
		case 11: // SLTIU
			controlOut.ALUsrc = 1;
			controlOut.ALU.op = 3; // SLT
			controlOut.ALU.bNegate = 1;
			controlOut.regWrite = 1;
			break;

		case 12: // ANDI
			controlOut.ALUsrc = 2;
			controlOut.ALU.op = 0; // AND
			controlOut.regWrite = 1;
			break;

		case 13: // ORI
			controlOut.ALUsrc = 2;
			controlOut.ALU.op = 1; // OR
			controlOut.regWrite = 1;
			break;

		case 14: // XORI
			controlOut.ALUsrc = 2;
			controlOut.ALU.op = 4; // XOR
			controlOut.regWrite = 1;
			break;

		case 15: // LUI
			controlOut.ALUsrc = 1;
			controlOut.ALU.op = 5; // LUI
			controlOut.regWrite = 1;
			break;

		case 28: // MUL (Only funct=2 supported)
			if (fields.funct != 2) return 0;
			controlOut.ALU.op = 9; // Multiply
			controlOut.regDst = 1;
			controlOut.regWrite = 1;
			break;

		case 32: // LB
		case 35: // LW
		case 40: // SB
		case 43: // SW
			controlOut.ALUsrc = 1;
			controlOut.ALU.op = 2; // ADD
			controlOut.memWrite = (fields.opcode >> 3) & 0x1;
			controlOut.memRead = 1 - controlOut.memWrite;
			controlOut.memToReg = controlOut.memRead;
			controlOut.regDst = 0;
			controlOut.regWrite = controlOut.memRead;
			controlOut.extra2 = (fields.opcode & 0x1) == 0 ? 1 : 0;
			break;

		case 0: // R-format instructions
			switch (fields.funct) {
			case 32: // ADD
			case 33: // ADDU
			case 34: // SUB
			case 35: // SUBU
				controlOut.ALUsrc = 0;
				controlOut.ALU.op = 2; // ADD or SUB
				controlOut.ALU.bNegate = (fields.funct >> 1) & 0x1;
				controlOut.regDst = 1;
				controlOut.regWrite = 1;
				break;

			case 36: // AND
			case 37: // OR
			case 38: // XOR
			case 39: // NOR
				controlOut.ALUsrc = 0;
				controlOut.ALU.op = (fields.funct == 36) ? 0 : (fields.funct == 37) ? 1 :
					(fields.funct == 38) ? 4 : 11;
				controlOut.regDst = 1;
				controlOut.regWrite = 1;
				break;

			case 42: // SLT
			case 43: // SLTU
				controlOut.ALUsrc = 0;
				controlOut.ALU.op = 3; // SLT
				controlOut.ALU.bNegate = 1;
				controlOut.regDst = 1;
				controlOut.regWrite = 1;
				break;

			default:
				return 0; // Unrecognized funct
			}
			break;

		default:
			return 0; // Unrecognized opcode
		}

		return 1; // Return 1 if recognized, 0 if invalid
	}

	public int getALUinput1(CPUControl controlIn, InstructionFields fieldsIn,
			int rsVal, int rtVal, int reg32, int reg33, int oldPC) {
		// TODO: Your code goes here
		switch (controlIn.extra3) {
		case 0: return rsVal;
		case 1: return fieldsIn.shamt;
		case 2: return reg32;
		case 3: return reg33;
		default: return 0;
		}
	}

	public int getALUinput2(CPUControl controlIn, InstructionFields fieldsIn,
			int rsVal, int rtVal, int reg32, int reg33, int oldPC) {
		// TODO: Your code goes here
		switch (controlIn.ALUsrc) {
		case 0: return rtVal;
		case 1: return fieldsIn.imm32;
		case 2: return fieldsIn.imm16;
		case 3: return 0;
		default: return 0;
		}
	}

	public void executeALU(CPUControl controlIn, int input1, int input2, ALUResult aluResultOut) {
		// TODO: Your code goes here
		switch (controlIn.ALU.op) {
		case 0: aluResultOut.result = input1 & input2; break;
		case 1: aluResultOut.result = input1 | input2; break;
		case 2: aluResultOut.result = controlIn.ALU.bNegate == 0 ? input1 + input2 : input1 - input2; break;
		case 3: aluResultOut.result = input1 < input2 ? 1 : 0; break;
		case 4: aluResultOut.result = input1 ^ input2; break;
		case 5: aluResultOut.result = input2 << 16; break;
		case 6: aluResultOut.result = input2 << input1; break;
		case 7: aluResultOut.result = input2 >>> input1; break;
		case 8: aluResultOut.result = input2 >> input1; break;
		case 9: aluResultOut.result = input1 * input2; break;
		case 10:
			aluResultOut.result = input1 / input2;
			aluResultOut.extra = input1 % input2;
			break;
		}
		aluResultOut.zero = (aluResultOut.result == 0) ? 1 : 0;
	}

	public void executeMEM(CPUControl controlIn, ALUResult aluResultIn,
			int rsVal, int rtVal, int[] memory, MemResult resultOut) {
		// TODO: Your code goes here
		if (controlIn.memWrite == 1) {
			memory[aluResultIn.result / 4] = rtVal;
		}
		resultOut.readVal = (controlIn.memRead == 1) ? memory[aluResultIn.result / 4] : 0;
	}

	public int getNextPC(InstructionFields fields, CPUControl controlIn, int aluZero,
			int rsVal, int rtVal, int oldPC) {
		// TODO: Your code goes here
		int plus4 = oldPC + 4;
		if (controlIn.jump == 1) return (plus4 & 0xF0000000) | (fields.address << 2);
		if (controlIn.branch == 1 && aluZero != (controlIn.extra1 & 0x1)) return plus4 + (fields.imm32 << 2);
		return plus4;
	}

	public void executeUpdateRegs(InstructionFields fields, CPUControl controlIn,
			ALUResult aluResultIn, MemResult memResultIn, int[] regs) {
		// TODO: Your code goes here
		if (controlIn.regWrite == 0) return;
		int val = (controlIn.memToReg == 0) ? aluResultIn.result : memResultIn.readVal;
		if (controlIn.regDst == 0) regs[fields.rt] = val;
		else if (controlIn.regDst == 1) regs[fields.rd] = val;
		else { regs[33] = val; regs[32] = aluResultIn.extra; }
	}
}