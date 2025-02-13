

public class CPUControl {

	ALUControl ALU;
	int ALUsrc;    
	int memRead;
	int memWrite;   
	int memToReg;
	int regDst;     
	int regWrite;
	int branch;     
	int jump;
	int extra1, extra2, extra3;



	public CPUControl() {

		ALU = new ALUControl();
	}


}

