package isasim;

// Created by: Josh Eaton	Date: 06/22/2017
// package:isasim

/**
* Description: Memory module utilized in the ISA
* simulator package "isasim".
*
*<p> This module is used to represent both the 
* instruction and data memory objects of this
* architecture
*
*<p> Each Memory Object is given a file that data
* is to be retrieved from and written to. This is
* the data that will be stored in the Memory Object,
* altered by the Processor operations, and rewritten
* to the source file upon termination.
*
*<p> Each Memory Object is also assigned a Register
* Object that will be used to determine the address
* for indexing stored memory when write and read 
* operations are requested by Processor.
*
* <p> Contained methods:
*		read(): return data
*			-the data provided by this method
*			 is determined by the address stored
*			 in the assigned Register.
*		write(): overwrite data
*			-the data provided to this method
*			 will be used to replace the data
*			 stored atthe address stored in
*			 this objects assigned Register.
*		writeToMem(): store the Memory's current state.
*			-writes the current data files to the 
*			 data file assigned to this Object.
*			 Utilized in termination of a running Process.
*
*		Constructor:
*			-given values for the Register,
*			 input/output file, and a size/length
*			 for the Memory Object as required
*			 by Processor, a Memory Object is 
*			 created.
*
*<p> Updated: 06/29/2017
*****************************************/

import java.io.IOException;

import java.io.BufferedReader;
import java.io.FileReader;

import java.io.BufferedWriter;
import java.io.FileWriter;


public class Memory {
 
	private String emptyWord;
	String[] data;
	private int wordCount; 
	private int wordLength;
	private int size; 
  	private Register register;
	private String file;	
 
	/**
	* Memory object constructor.
	* <p>The memory object will read 'words'
	* from a file line by line, and fill in this 
	* objects data to the specified capacity.
	* 
	* <p> If the input file has more lines than
	* the specified memory capacity, the excess
	* memory words will be skipped.
	*
	* <p> If the input file has less lines than 
	* the specified capacity, the remaining memory
	* locations will be filled with empty words (0)
	*	
	* <p> If any line in the file that has less than
	* a word length of characters, that word will be
	* prepadded with '0' to meet the desired length
	* 
	* <p> The input of addressing to a Memory object 
	* will be limited to the assigned register
	*
	* @param capacity overall size of this Memory object
	* @param length word size for this Memory object
	* @param filename input file to initialize the data
	* @param addrRegister the Register to use for addressing
	*
	*******/
	public Memory(int capacity,int length, String filename, Register addrRegister) {
		data = new String[capacity];
		size = capacity;
		wordCount = 0;
		wordLength = length;
		register = addrRegister;
		file = filename;
		emptyWord = "";
		while (emptyWord.length() < wordLength) emptyWord = "0" + emptyWord;
		
		readFromMem();

	}

	/**
	* Store the current contents of this object.
	* 
	* This method will write the contents of the current
	* memory data to the file associated with this object
	* in order to maintain the memory values between runs of
	* the simulator.
	*
	**/
	public void writeToMem(){
		try (BufferedWriter outFile = new BufferedWriter(new FileWriter(file))){
			for (int i = 0; i < wordCount; i++){
				outFile.write(data[i]+"\n");	
			}
			outFile.close();
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	* Retrieve the data stored for this memory.
	*
	* This method will update this objects
	* current data to the data that is stored
	* in the data file associated with this object.
	*
	**/
	public void readFromMem(){	
		wordCount = 0;
		try (BufferedReader inFile = new BufferedReader(new FileReader(file))){
			String word = inFile.readLine();
			while (inFile.ready() && wordCount < size && word != null){
				 addWord(word);
				 word = inFile.readLine();
			}
			inFile.close();
			while (wordCount <size) addWord(emptyWord);	
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	* Add a word to this object's memory.
	* 
	* This method will update this object
	* to contain the provided data at the next
	* available location. The provided data will be
	* truncated or prepadded with "0"'s to ensure
	* that it matches the word length specified
	* for this object.
	*
	* @param word the data word to be added to memory.
	**/	
	private void addWord(String word){
		while (word.length() < wordLength) {
			word = "0"+word;
		}
		if (word.length() > wordLength) word = word.substring(0,wordLength);
		data[wordCount++] = word;
		return;
	}

	/**
	* Retrieve a data word from this object.
	* This method will provide the data value
	* stored in this object at the location indicated
	* by the addressing register associated with this object.
	* 
	* @return the data string stored at Memory[register.value]
	**/
	public String read(){
		int address = Utilities.binToInt(register.getValue());
		if(address >= size) return emptyWord;
		return data[address];	
	}	

	/**
	* Update this object's memory.
	* This method will write the provided
	* data to the location indicated by the 
	* addressing register associated with this
	* object.
	*
	* @param dataIn the data to be written
	**/
	public void write(String dataIn) {
		int address = Utilities.binToInt(register.getValue());
		if(address >= size) return;
		data[address] = dataIn;
	}	
	
	/**
	* Empty the data memory.
	*
	* The current data in this object
	* will be replaced by zero data.
	* the current state of this memory will
	* be lost.
	*/
	public void clear(){
		data = new String[size];
		for (int i = 0; i < size; i++) data[i] = emptyWord;
		writeToMem();
	}




}
