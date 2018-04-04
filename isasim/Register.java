package isasim;
// Created By: Josh Eaton 	Date: 06/22/2017
// package: isasim

/**
*<p> Description: Register module utilized in the ISA
* simulator package isasim.
*
*<p> This module is used to represent each of the 
* registers in the register file, as well as the 
* program counter PC and the memory access register
* MAR.	
*
* <p> Contained methods:
*		getValue(): returns the data currently stored
*					in this register.
*		update(): updates the data in the register to
*				  the provided value.
*
**/

public class Register {

    private String value;
	private int width;
	
	/**
	* Register constructor.
	*
	* This creates a Register object
	* of a specific size with a blank
	* initial value ( 0 ).
	*
	* @param size the size of this Register.
	**/    
	public Register(int size){
		width = size;
		update("");
    }
	
	/**
	* Register constructor.
	* 
	* This creates a Register
	* with a specified size and
	* initial value.
	*
	* @param size size of the Register
	* @param initVal initial value to store in this Register
	**/
	public Register(int size, String initVal) {
		width = size;
		update(initVal);
	}

	/**
	* Read the value of this Register.
	*
	* @return the value currently stored
	**/
	public String getValue(){
		return value;
	}

	/**
	* Update the value of this Register.
	*
	* This method will overwrite the data
	* currently stored by the register
	* if the provided data is not null.
	* if the provided data is longer than the 
	* size of the register, it will be clipped.	
	* If the value is too short, it will be pre-padded
	* with zero-data.
	*
	* @param newVal the data to store.
	**/
	public void update(String newVal) {
		value = "";
		if (newVal != null) {
			if (newVal.length() > width){
				value = newVal.substring(0,width);
			}
			else {
				value = newVal;
			}
		}
		while (value.length() < width) value = "0" +value;
		return;
	}

        
}
