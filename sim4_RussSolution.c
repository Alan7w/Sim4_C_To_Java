#include <stdio.h>
#include <memory.h>

#include "sim4.h"



void extract_instructionFields(WORD instruction, InstructionFields *fieldsOut)
{
	fieldsOut->opcode  = (instruction >> 26) & 0x3f;
	fieldsOut->rs      = (instruction >> 21) & 0x1f;
	fieldsOut->rt      = (instruction >> 16) & 0x1f;
	fieldsOut->rd      = (instruction >> 11) & 0x1f;
	fieldsOut->shamt   = (instruction >>  6) & 0x1f;
	fieldsOut->funct   = (instruction      ) & 0x3f;
	fieldsOut->imm16   = (instruction      ) & 0x0ffff;
	fieldsOut->imm32   = signExtend16to32(fieldsOut->imm16);
	fieldsOut->address = (instruction      ) & 0x3ffffff;
}



int  fill_CPUControl(InstructionFields *fields, CPUControl *controlOut)
{
	memset(controlOut, 0, sizeof(*controlOut));

	switch(fields->opcode)
	{
	// we'll do case 0 later, all in a big bunch.

	case 2:	// J
		controlOut->ALUsrc      = 0;   // don't care
		controlOut->ALU.op      = 0;   // don't care
		controlOut->ALU.bNegate = 0;   // don't care
		controlOut->memRead     = 0;
		controlOut->memWrite    = 0;
		controlOut->memToReg    = 0;   // don't care
		controlOut->regDst      = 0;   // don't care
		controlOut->regWrite    = 0;
		controlOut->branch      = 0;
		controlOut->jump        = 1;
		break;

#if 0   // TODO: implement me
	case 3:	// JAL
		break;
#endif

	case 4:	// BEQ
	case 5:	// BNE
		controlOut->ALUsrc      = 0;
		controlOut->ALU.op      = 2;   // SUB
		controlOut->ALU.bNegate = 1;   //
		controlOut->memRead     = 0;
		controlOut->memWrite    = 0;
		controlOut->memToReg    = 0;   // don't care
		controlOut->regDst      = 0;   // don't care
		controlOut->regWrite    = 0;
		controlOut->branch      = 1;
		controlOut->jump        = 0;

		controlOut->extra1      = (fields->opcode & 0x1);
		break;

	case 8:	// ADDI
	case 9:	// ADDIU
		controlOut->ALUsrc      = 1;
		controlOut->ALU.op      = 2;   // ADD
		controlOut->ALU.bNegate = 0;   //
		controlOut->memRead     = 0;
		controlOut->memWrite    = 0;
		controlOut->memToReg    = 0;   // ALU result
		controlOut->regDst      = 0;   // rt
		controlOut->regWrite    = 1;
		controlOut->branch      = 0;
		controlOut->jump        = 0;
		break;

	case 10:	// SLTI
	case 11:	// SLTIU
		controlOut->ALUsrc      = 1;
		controlOut->ALU.op      = 3;   // SLT
		controlOut->ALU.bNegate = 1;   //
		controlOut->memRead     = 0;
		controlOut->memWrite    = 0;
		controlOut->memToReg    = 0;   // ALU result
		controlOut->regDst      = 0;   // rt
		controlOut->regWrite    = 1;
		controlOut->branch      = 0;
		controlOut->jump        = 0;
		break;

	case 12:	// ANDI
		controlOut->ALUsrc      = 2;   // zero-extended
		controlOut->ALU.op      = 0;   // AND
		controlOut->ALU.bNegate = 0;   //
		controlOut->memRead     = 0;
		controlOut->memWrite    = 0;
		controlOut->memToReg    = 0;   // ALU result
		controlOut->regDst      = 0;   // rt
		controlOut->regWrite    = 1;
		controlOut->branch      = 0;
		controlOut->jump        = 0;
		break;

	case 13:	// ORI
		controlOut->ALUsrc      = 2;   // zero-extended
		controlOut->ALU.op      = 1;   // OR
		controlOut->ALU.bNegate = 0;   //
		controlOut->memRead     = 0;
		controlOut->memWrite    = 0;
		controlOut->memToReg    = 0;   // ALU result
		controlOut->regDst      = 0;   // rt
		controlOut->regWrite    = 1;
		controlOut->branch      = 0;
		controlOut->jump        = 0;
		break;

	case 14:	// XORI
		controlOut->ALUsrc      = 2;   // zero-extended
		controlOut->ALU.op      = 4;   // XOR
		controlOut->ALU.bNegate = 0;   //
		controlOut->memRead     = 0;
		controlOut->memWrite    = 0;
		controlOut->memToReg    = 0;   // ALU result
		controlOut->regDst      = 0;   // rt
		controlOut->regWrite    = 1;
		controlOut->branch      = 0;
		controlOut->jump        = 0;
		break;

	case 15:	// LUI
		controlOut->ALUsrc      = 1;
		controlOut->ALU.op      = 5;   // LUI
		controlOut->ALU.bNegate = 0;   //
		controlOut->memRead     = 0;
		controlOut->memWrite    = 0;
		controlOut->memToReg    = 0;   // ALU result
		controlOut->regDst      = 0;   // rt
		controlOut->regWrite    = 1;
		controlOut->branch      = 0;
		controlOut->jump        = 0;
		break;

	case 28:	// MUL
		// only funct=2 (MUL) is supported
		if (fields->funct != 2)
			return 0;

		controlOut->ALUsrc      = 0;
		controlOut->ALU.op      = 9;   // multiply
		controlOut->ALU.bNegate = 0;
		controlOut->memRead     = 0;
		controlOut->memWrite    = 0;
		controlOut->memToReg    = 0;   // ALU result
		controlOut->regDst      = 1;   // rd   (we'll ignore the upper word)
		controlOut->regWrite    = 1;
		controlOut->branch      = 0;
		controlOut->jump        = 0;
		break;

	case 32:	// LB
	case 35:	// LW
	case 40:	// SB
	case 43:	// SW
		controlOut->ALUsrc      = 1;
		controlOut->ALU.op      = 2;   // ADD
		controlOut->ALU.bNegate = 0;   //
		controlOut->memWrite    = ((fields->opcode >> 3) & 0x1);   // 1=STORE
		controlOut->memRead     = 1 - controlOut->memWrite;
		controlOut->memToReg    = controlOut->memRead;   // 1 = MEM result 0 = don't care
		controlOut->regDst      = 0;   // rt or don'tcare
		controlOut->regWrite    = controlOut->memRead;
		controlOut->branch      = 0;
		controlOut->jump        = 0;

		controlOut->extra2      = !(fields->opcode & 0x1);   // 1 - bytes
		break;

	case 0: // R-format
		switch (fields->funct)
		{
		case 0:	// SLL
		case 2:	// SRL
		case 3:	// SRA
		case 4:	// SLLV
		case 6:	// SRLV
		case 7:	// SRAV
			controlOut->extra3 = 1-((fields->funct >> 2) & 0x1);  // 0 - register 1 - shamt

			controlOut->ALUsrc      = 0;   // register, always.  This is what is shifted!

			if ((fields->funct & 0x3) == 0)
				controlOut->ALU.op = 6;  // shift left
			else if ((fields->funct & 0x3) == 2)
				controlOut->ALU.op = 7;  // shift right logical
			else
				controlOut->ALU.op = 8;  // shift right arithmetic

			controlOut->ALU.bNegate = 0;

			controlOut->memRead     = 0;
			controlOut->memWrite    = 0;
			controlOut->memToReg    = 0;   // ALU result
			controlOut->regDst      = 1;   // rd
			controlOut->regWrite    = 1;
			controlOut->branch      = 0;
			controlOut->jump        = 0;
			break;

		case 16:	// MFHI
		case 18:	// MFLO
			controlOut->extra3 = 2 + ((fields->funct >> 1) & 0x1);

			controlOut->ALUsrc      = 3;   // constant ZERO

			controlOut->ALU.op      = 2;   // ADD
			controlOut->ALU.bNegate = 0;
			controlOut->memRead     = 0;
			controlOut->memWrite    = 0;
			controlOut->memToReg    = 0;   // ALU result
			controlOut->regDst      = 1;   // rd
			controlOut->regWrite    = 1;
			controlOut->branch      = 0;
			controlOut->jump        = 0;
			break;

		case 24:	// MULT
		case 25:	// MULTU
		case 26:	// DIV
		case 27:	// DIVU
			controlOut->ALUsrc      = 0;
			controlOut->ALU.op      = 9 + ((fields->funct >> 1) & 0x1);   // multiply or divide
			controlOut->ALU.bNegate = 0;
			controlOut->memRead     = 0;
			controlOut->memWrite    = 0;
			controlOut->memToReg    = 0;   // ALU result
			controlOut->regDst      = 2;   // HI/LO
			controlOut->regWrite    = 1;
			controlOut->branch      = 0;
			controlOut->jump        = 0;
			break;

		case 32:	// ADD
		case 33:	// ADDU
		case 34:	// SUB
		case 35:	// SUBU
			controlOut->ALUsrc      = 0;
			controlOut->ALU.op      = 2;   // ADD or SUB
			controlOut->ALU.bNegate = ((fields->funct >> 1) & 0x1);
			controlOut->memRead     = 0;
			controlOut->memWrite    = 0;
			controlOut->memToReg    = 0;   // ALU result
			controlOut->regDst      = 1;   // rd
			controlOut->regWrite    = 1;
			controlOut->branch      = 0;
			controlOut->jump        = 0;
			break;

		case 36:	// AND
		case 37:	// OR
		case 38:	// XOR
		case 39:	// NOR
			controlOut->ALUsrc      = 0;

			if (fields->funct == 36)
				controlOut->ALU.op = 0;   // AND
			if (fields->funct == 37)
				controlOut->ALU.op = 1;   // OR
			if (fields->funct == 38)
				controlOut->ALU.op = 4;   // XOR
			if (fields->funct == 39)
				controlOut->ALU.op = 11;   // NOR

			controlOut->ALU.bNegate = 0;
			controlOut->memRead     = 0;
			controlOut->memWrite    = 0;
			controlOut->memToReg    = 0;   // ALU result
			controlOut->regDst      = 1;   // rd
			controlOut->regWrite    = 1;
			controlOut->branch      = 0;
			controlOut->jump        = 0;
			break;

		case 42:	// SLT
		case 43:	// SLTU
			controlOut->ALUsrc      = 0;
			controlOut->ALU.op      = 3;   // SLT
			controlOut->ALU.bNegate = 1;
			controlOut->memRead     = 0;
			controlOut->memWrite    = 0;
			controlOut->memToReg    = 0;   // ALU result
			controlOut->regDst      = 1;   // rd
			controlOut->regWrite    = 1;
			controlOut->branch      = 0;
			controlOut->jump        = 0;
			break;

		default:
			// unrecognized funct field
			return 0;
		}
		break;

	default:
		// unrecognized opcode
		return 0;
	}


	// common case: we recognized the instruction!
	return 1;
}


