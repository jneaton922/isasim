package isasim;

// Created by: Josh Eaton		Date: 06/23/2017
// package: isasim

/**
*<p> Description: This module acts as the Control Unit
* as utilized in the ISA simulator "isasim".
*
*<p> A Controller object provides a mode
* control word (MCW) to the Processor, which
* contains the control signals needed in this
* system. The input used to determine the MCW
* is the opcode/func portions of an instruction
*
*<p> Currently: the MCWs are stored as constants
* directly connected to the provided sequences of
* opcode bits.
*
*<p> Contained methods:
*		controlWord(): return the MCW
*			this method takes the 
*			opcode and function bits
*			of the current instruction and
*			will return the associated
*			mode control word that contains
*			the control signals necessary
*			to perform the given operation.
*
*<p>Updated: 06/25/2017
************************************/

public class Controller {
	
	//Mode control words for each of this system's operations.
	private String LA = "000100100";
	private String MR = "000000010";
	private String MW = "001000000";
	private String ADDI = "000100011";
	private String BEQ = "010000000";
	private String J = "100000000";

	private String ADD = "000000011";
	private String SUB = "000001011";
	private String NOR = "000010011";

	
	public Controller(){}

	/**
	*Determined the current MCW.
	*
	* This method will determine the mode 
	* control word that is necessary to carry out
	* the operation indicated by the current instruction's
	* opcode and func.
	*
	* @param opfunc the concatenated opcode and func.
	* @return the appropriate MCW.
	**/
	public String controlWord(String opfunc){
		String opcode = opfunc.substring(0,4);
		String func = opfunc.substring(4,8);
		
		switch (opcode) {
		
			case "0000":
				return Rcw(func);
			case "0001":
				return LA;
			case "0010":
				return MR;
			case "0110":
				return MW;
			case "0100":
				return ADDI;
			case "0111":
				return BEQ;
			case "1000":	
				return J;
			default:
				return null;

		}
		
	}

	/**
	*Decode the func portion of 'opfunc'.
	*
	* This method determines the MCW when 
	* the opcode indicates an R-type instruction.
	*
	* @param func the 4 LSB of the instruction "func"
	* @return the appropriate MCW for this instruction.
	**/
	private String Rcw(String func) {
		switch(func) {
			case "1000":
				return ADD;
			case "0001":
				return SUB;
			case "0010":
				return NOR;
			default:
				return null;
		}
	}
}
