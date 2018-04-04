package isasim;

// Created by: Josh Eaton	Date: 06/22/2017
// package: isasim

/**
* Description: Utilities used in the ISA 
* simulator package "isasim".
*
* <p> This module contains utility methods
* that are used in this package to alter 
* binary strings and integers as required
* by the two-complement number system used
* in this architecture.
*
* <p> Contained methods:
*		binToInt(): converts a binary String to it's
*					integer value.
*		intToBin(): provides the binary String that represents
*					the given integer value.
*		comp():		converts a binary string to its 2-complement
*
**/


import static java.lang.Math.pow;
import static java.lang.Math.abs;

public class Utilities {


	/**
	* Convert a binary string to an integer.
	*	
	* This method takes binary data and converts
	* it to an integer using a 2-complement number
	* system. 
	*
	* @param bin the binary data
	* @return the equivalent integer value.
	**/
	public static int binToInt(String bin){
		boolean comped = false;
		if (bin.substring(0,1).equals("1")){
			bin = comp(bin);
			comped = true;
		}
		int value = 0;
		int power = bin.length();
		for (int i = 0; i < bin.length(); i++){
			power--;
			if (bin.substring(i,i+1).equals("1")){
				value += pow(2,power);
			}else{}
		}
		if(comped){
			return -1*value;
		}else{
			return value;
		}
		
	}

	/**
	* Convert an integer to a binary data-string.	
	*	
	* This method creates binary data equivalent
	* to a provided integer value, using a 
	* 2-complement number system.
	*
	* @param length the length of the return data
	* @param value the integer to convert
	* @return the binary string
	**/
	public static String intToBin(int length, int value){
		int power = length;
		boolean neg = false;
		if(value <0){
			value = abs(value);
			neg = true;
		}
		String bin = "";
		for (int i = 0; i < length;i++){
			power--;
			if(value >= pow(2,power)){
				bin+="1";
				value -= pow(2,power);
			}else{
				bin+="0";
			}
		}
		if(neg){
			return comp(bin);
		}else{
			return bin;
		}
	}

	/**
	* Convert a binary string to its complement.
	*
	* Using a 2-complement number system,
	* this method provides the complement
	* value of a provided string, that will
	* be the same length as the provided string.
	*
	* @param bin the binary string to complement.
	* @return the 2-complement binary string of bin
	**/
	public static String comp(String bin){

		int firstOne = -1;
		String newStr = "";
		for (int i = bin.length()-1; i > -1; i--){
			if(bin.substring(i,i+1).equals("1")){
				firstOne = i;
				break;
			}
		}
		if (firstOne == -1){
			// this string is zero, which has no complement
			return bin;
		}else{
			newStr = bin.substring(firstOne,bin.length());
			for( int i = firstOne-1; i > -1; i--){
				if(bin.substring(i,i+1).equals("1")){
					newStr = "0"+newStr;
				}else{
					newStr = "1"+newStr;
				}
			}
			return newStr;
		}
	}

}
