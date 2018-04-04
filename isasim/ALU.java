package isasim;

// Created by: Josh Eaton	Date: 06/23/2017
// package: isasim

/**
* <p>Description: The ALU module utilized by the ISA
* simulator package "isasim".
*
* <p> This module performs the required arithmetic and
* logic functions utilized in this system.
*
* <p> Contained methods:
*			update(): receives the ALUop signal
*					  provided by the Controller
*
*			operate(): performs an operation
*					   on two data inputs based
*					   on the current stored control 
*					   signal
*
* <p>Updated: 06/29/2017
***************************************************/


public class ALU {
	private String opcode;


	/**
	* ALU constructor.
	* 
	* Creates an ALU object capable
	* of performing addition, subtraction
	* and nor operations on binary strings.
	*
	* @param oc the initial operating code of this ALU
	**/
	public ALU(String oc){
		opcode = oc;	
	}
	
	
	/**
	*Updates the operating code of this ALU.
	*
	*@param newSignal the new operating code.
	**/
	public void update(String newSignal){
		opcode = newSignal;
	}
	

	/**
	*Perform an Arithmetic Logic operation.
	*
	* Will determine the appropriate operation
	* based on the currently stored operating code.
	* subtraction will be interpreted as first param
	* minus second param (a - b).
	*
	* @param a one of the operands of the operation.
	* @param b the second operand of the operation.
	* @return the result of the operation.
	**/
	public String operate(String a, String b){
		switch (opcode){
			case "00":
				return add(a,b);
			case "01": 
				return sub(a,b); 
			case "10":
				return nor(a,b);
			default:
				return a;
		}
	}
	
	/**
	* Perform binary addition on two strings.
	* This method adds two binary strings interpreted
	* as two-complement numbers. A carry out from the
	* MSB is ignored, and there is no indication of 
	* overflow in this function. 
	*
	* @param a first operand
	* @param b second operand
	* @return the result of the addition.
	**/
	private String add(String a, String b) {	
		boolean carry = false;
		String ret = "";
		for (int i = a.length()-1; i > -1; i--){
			if (a.substring(i,i+1).equals("1")){
				if (b.substring(i,i+1).equals("1")){
					if (carry) {
						ret = "1" + ret;
					} else {
						ret = "0" + ret;
						carry = true;
					}
				}else {
					if (carry) {
						ret = "0" + ret;
					} else {
						ret = "1" + ret;
					}
				} 
			}
			else {
				if (b.substring(i,i+1).equals("1")){
					if (carry) {
						ret = "0" + ret;
					} else {
						ret = "1" + ret;
					}
				} else {
					if (carry) {
						ret = "1" + ret;
						carry = false;
					} else {
						ret = "0" + ret;
					}
				}
			}
		}
		return ret;
	}	
	
	/**
	* Perform binary substraction.
	* this method performs binary subtraction
	* via binary addition, by first converting
	* the second operand to its two-complement
	* utilizing the utility method "comp" contained
	* in this package.
	* 
	*@param a the first operand
	*@param b the second operand
	*@return the result of the subtraction.
	**/	
	private String sub(String a,String b) {

		return add(a,Utilities.comp(b));
	}
	
	/**
	*Perform a nor operation on two binary strings.
	* This method performs a bit-logical nor, in which
	* the only combination of inputs that creates an output
	* of '1' is "00".
	*	
	*@param a the first operand
	*@param b the second operand
	*@return the result of the nor.
	**/
	private String nor(String a, String b) {
		String ret = "";
		for(int i = 0; i < a.length(); i++){
			if (a.charAt(i) == '0' && b.charAt(i) == '0'){
				ret += '1';
			}else{
				ret += '0';
			}
		}
		return ret;
	}	

}
