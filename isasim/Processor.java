package isasim;

// Created by: Josh Eaton	Date:  06/22/2017
// package: isasim

/**
* <p>Description: Processor module of the ISA simulator package.
*
* <p>The Processor module houses all components of 
* the single-cycle datapath used for isasim.
*
*<p> It is within Processor that the instruction 
* is retrieved from memory, and all actions
* encoded by the Controller are decoded
* and performed on the datapath Objects contained
* by the Processor.
*
* <p> Contained methods:
*  		tick(): carry out a clock cycle.
*			-fetch instruction from memory.
*			-send instruction to control unit for
*			 control signals.
*			-send control signals to appropriate
*			 Objects.
*		terminate(): prepare to exit.
*			-write the values stored in the memory
*			 objects to their data files in order
*		  	 to create a restorable state.
*		Constructor:
*			-The constructor Processor() requires
*			 various configuration values to create
*			 a Processor Object.
*			-The methods called by the constructor
*			 instantiate groups of Objects associated
*			 with the Processor module.
*<p> Updated: 06/29/2017
***********************************************/


public class Processor{
   	/**************************************
	* Datapath object contained by Processor.
	*
	* <p>Two Memory Objects representing the 
	* data memory and instruction memory of this 
	* system.
	* 
	* <p> Two independent Register Objects and
	* a Register array, representing the PC,
	* MAR, and register file.
	*
	* <p> Two ALU Objects, one for the core
	* ALU of the system, and one for the
	* address calculation for the PC.
	*
	*<p> One Controller Object, which represents
	* the control unit of the datapath.
	*
	*<p> Four Mux Objects to represent the 
	* multiplexers that are used in this
	* implementation
	*
	*<p> One SignExtend Object that carries out
	* the sign-extend function of the immediate value
	* in this architecture
	*
	*<p> **Further description of each of the objects
	* mentioned above can be found in the various modules
	* within this package.
	*	
	****************************************/ 
    
	Memory instructionMemory;
    Memory dataMemory;

    Register PC;
    Register MAR;
    Register[] regFile;
	
	Mux jumpMux;
	Mux branchMux;
	Mux regMux;
	Mux aluMux;  
	
	SignExtend signEx16;

	ALU alu;
	ALU pcAdder;
	
	Controller controller; 
	
	String InsFile;
	String DatFile;
	String ZERO_FILE = "zero.dat";
	int regWidth;
	int regFileSize;
	int imemSize;
	int dmemSize;
	int wordWidth;
	

	/**
	* Constructor for a processor object.
	* 
	* This constructor creates a processor
	* specific to the isasim simulator with
	* various input parameters to specify operation.
	*
	* @param rw Size of the registers of this processor.
	* @param rfs number of registers in the register file
	* @param ims size of the instruction memory
	* @param dms size of the data memory
	* @param ww size of a word in this system
	* @param ifi input filename for instruction memory
	* @param dfi input filename for data memory
	*
	**/ 
    Processor(int rw, int rfs, int ims,int dms, int ww, String ifi, String dfi){
		regWidth = rw; regFileSize = rfs; imemSize = ims;dmemSize = dms; wordWidth = ww;
		InsFile = ifi; DatFile = dfi;
	
		controller = new Controller();
		
		initializeRegisters();
		initializeMemory();
		initializeAlu();
		
		signEx16 = new SignExtend(16);	
		regMux = new Mux();
		aluMux = new Mux();
		jumpMux = new Mux();
		branchMux = new Mux();
	}

	/**
	* Instantiate all registers for this system.
	*
	* Creates the PC (program counter) MAR (Memory Access)
	* And register file Register.
	*
	**/ 
	private void initializeRegisters(){
	
       PC = new Register(regWidth);
       MAR = new Register(regWidth);
       regFile = new Register[regFileSize];
       for (int i = 0; i < regFileSize; i++){
            regFile[i] = new Register(regWidth);
       }
    }

	/**
	* Instantiate memories for this sytem.	
	*
	* Creates the instruction and data memories for this
	* Sytstem.
	*
	**/ 
	private void initializeMemory() {
    	instructionMemory = new Memory(imemSize,wordWidth,InsFile,PC);
		dataMemory = new Memory(dmemSize,wordWidth,DatFile,MAR);
	}
	
	/**
	*
	* Instantiate the ALU objects for this system.
	* Creates the ALU used for execution
	* as well as the dedicated ALU adder for 
	* PC incrementing.
	*
	**/ 
	private void initializeAlu(){
		alu = new ALU("00");
		pcAdder = new ALU("00");
	}

	/**
	* Run the processor to completion.
	* 
	* This method will continue to execute
	* instructions untill the end of instruction
	* memory is reached, or there are no more
	* valid instructions stored.
	*
	**/ 
	public void execute(){
		while (Utilities.binToInt(PC.getValue()) < imemSize && tick());
		return;
	}

	/**
	* Run one instruction of the processor.
	*
	* This method executes the instruction at the
	* word currently indicated by the PC.
	*
	* @return false if there are no more instructions to execute.
	**/ 
	public boolean oneCycle(){
		if (Utilities.binToInt(PC.getValue()) < imemSize && Utilities.binToInt(PC.getValue()) >= 0) return tick();
		terminate();
		return false;
	}

	/**
	* Restart the sytem.
	* 
	* This method sets all registers
	* in this system to 0 (including PC)
	* and updates the instruction memory object using
	* the assigned input files.
	*
	**/ 
	public void reset(){
		PC.update("");
		MAR.update("");
		for(int i = 0; i < regFileSize; i++) {
			regFile[i].update("");
		}	
		instructionMemory.readFromMem();
	}

	/**
	* Perform a clock cycle.
	*
	* This method carries out all of the
	* operations of a single cycle in this
	* datapath. This is the method where the
	* control signals and data are passed between
	* the various functional units of this system.
	* 
	* @return false if the current instruction is empty
	**/ 
    private boolean tick(){
		String instruction = instructionMemory.read();

		if (Utilities.binToInt(instruction) == 0){
			terminate();
			return false;
		}

		String opfunc = instruction.substring(0,4) + instruction.substring(12,16);
		String addrA = "0"+instruction.substring(4,8);//register addressing is unsigned
		String addrB = "0"+instruction.substring(8,12);//register addressing is unsigned
		String addrJ = instruction.substring(4,16);
		

		// potential operands
		String immediate = signEx16.signExtend(instruction.substring(8,16));
		Register RA = regFile[Utilities.binToInt(addrA)];
		Register RB = regFile[Utilities.binToInt(addrB)];
		
		// MCW encoding: [PCsrc][Branch][Memory][ALUsrc][ALUop]][MAR][Rwrite][Rsel]	
		String MCW = controller.controlWord(opfunc);	
		char brsel;
		if(MCW.substring(1,2).equals("1") && Utilities.binToInt(RA.getValue()) == 0){
			brsel = '1';
		}
		else{
			brsel = '0';
		}

		aluMux.update(RB.getValue(),immediate);
		branchMux.update(Utilities.intToBin(16,1),immediate);
		

		String aluInputB = aluMux.output(MCW.charAt(3));
		String pcAddend = branchMux.output(brsel);
		String newPC = pcAdder.operate(PC.getValue(),pcAddend);	
	
		alu.update(MCW.substring(4,6));	
		String aluRes = alu.operate(RA.getValue(),aluInputB);
		
		if (MCW.charAt(2) == '1') {
			dataMemory.write(RA.getValue());
		}	
		regMux.update(dataMemory.read(),aluRes);
		if (MCW.charAt(7) == '1') {
			RA.update(regMux.output(MCW.charAt(8)));
		}
	

		if (MCW.charAt(6) == '1') {
			MAR.update(aluRes);
		}
		
		String Jump = PC.getValue().substring(0,4)+addrJ;
		jumpMux.update(newPC,Jump);	
		PC.update(jumpMux.output(MCW.charAt(0)));	
		return true;
    }

	/**
	* Empty out the data memory.
	*
	* This method will overwrite the 
	* current contents of the data memory with
	* zero data, the current data in memory
	* will be lost.
	*/
	public void zeroData(){
		dataMemory.clear();
	}
	
	/**
	* Store the current state of the system.
	*
	* This method saves the state by writing the 
	* data memory, which is the only
	* object that has been manipulated which
	* will be recovered upon restarting the system.
	*
	**/ 
	public void terminate() {
		dataMemory.writeToMem();
	}

}
