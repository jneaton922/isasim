package isasim;

// Created by: Josh Eaton	Date: 06/23/2017
// package: isasim

/**
* <p>Description: Sign Extend module
* utilized in the ISA simulator package
* "isasim".
*
* <p> This module is used solely to perform
* the actions of a sign-extend 16 function
* needed in this architecture to extend the
* immediate value in I-type instructions.
*
* <p> Contained methods:
*		signExtend(): sign extends the input by
*					  repeating the first bit until
*					  the length matches the length
*					  specified by this SignExtend's
*					  "extendLen".
**/

public class SignExtend {
	private int extendLen;

	/**
	* SignExtend constructor.
	*
	* Creates a sign-extend object
	* that will extend to the provided
	* size.
	*
	* @param size indicates the extension length of this object.
	**/
	public SignExtend(int size) {
		extendLen = size;
	}
	
	/** 
	* Perform the sign extend operation.
	*
	* This function interprets the sign-bit
	* as the MSB or leftmost bit of the
	* bit-string. This same bit
	* will be repeatedly prepend to the data
	* until it reaches the desired length.
	*
	* @param input the data to extend
	* @return the sign-extended data
	**/
	public String signExtend(String input){
		String output;
		output = ""+input;
		String signbit = input.substring(0,1);
		while (output.length() < extendLen) output = signbit + output;
		return output;
	}



}
