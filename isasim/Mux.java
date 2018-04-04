package isasim;

// Created by: Josh Eaton	Date: 06/22/2017
// package: isasim

/**
* <p> Description: Mux module utilized in the 
* ISA simulator package "isasim".
*
* <p> This module serves to represent multiplexers
* in the isasim datapath.
*
* <p> Muxes in this module are designed as 
* 2:1 multiplexers, with two inputs and a 
* one-bit binary control signal.
*
* <p> Contained methods:
*		update(): give the mux inputs.
*				given two Strings, this method will
*				store the values in this objects
*				input data members
*		output(): return the mux output.
*				this method takes the char that 
*				represents this objects current
*				control signal and returns
*				the corresponding input to the
*				mux.
*
*************************************/

public class Mux {
	private String[] inputs;
	
	/**
	* Constuctor for Mux object.
	* Creates a 2:1 Multiplexer object.
	**/	
	public Mux (){	
		inputs = new String[2];
	}

	/**
	* Changes the input signals stored in this Mux.
	*
	* The stored inputs to this object will be updated
	* and stored as these inputs.
	*
	* @param a the signal assigned to output '0'.
	* @param b the signal assigned to output '1'.
	**/
	public void update(String a, String b){
		inputs[0] = a;
		inputs[1] = b;	
	}
	

	/**
	* Provides the output from this Mux.
	* 
	* Given the select signal, this 
	* will return one of the two currently
	* stored inputs of this Mux object.
	*
	* @param select the control signal for this mux
	* @return the stored value appropriate to the provided signal.
	**/
	public String output(char select){
		if (select == '0') return inputs[0];
		if (select == '1') return inputs[1];
		return null;
	}

}
