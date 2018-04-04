package isasim;

// Created by: Josh Eaton	Date: 6/22/2017
// package: isasim 

/**
* Description: The main Simulator module
* for the simulator package "isasim".
*
*<p> This module is used to create the GUI
* that displays the details of the datapath, as
* well as the functions available to the user.
* The GUI allows a user to run the current instruction
* memory to completion, run one cycle of the instruction 
* memory, load a new instruction file, and reset the 
* system to repeat the current instruction memory.
*
*<p> Contained methods:
*		the main method of this module
*		creates the processor object and
*		creates the Gui object to display
*		the processor's information. and
*		waits for user action to continue.
*
*<p>		compile():
*			if the main method is provided
*			an argument, that string will be
*			given to this method as a filename
*			to open and translate to instructions
*			to be understood by this processor.
*			if the input file is valid, the 
*			generated instructions will be
*			written to the instruction memory file
*			before the processor is initialized.
*			----This method can also be used by the Gui
*				to update the instruction memory with
*				another file during a simulation run.
*
*
*<p> Updated: 7/27/2017
***********************************/
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.event.*;


public class Simulator {

	//constants for the system. maybe make configurable?
	private final static int REGISTER_WIDTH = 16;
	private final static int REGISTER_FILE_SIZE = 16;
	private final static int INSTRUCTION_MEM_SIZE = 128;
	private final static int DATA_MEM_SIZE = 128;
	private final static int WORD_WIDTH = 16;
	
	private final static String INSTR_FILE = "Imem.dat";
	private final static String DAT_FILE = "Dmem.dat";

	JFrame window;
    Container content;
    JLabel[] registers;
    JLabel[] registerName;
    JLabel[] dmem;
    JLabel[] imem;
    JLabel Mar;
    JLabel MAR;
    JLabel Pc; 
    JLabel PC; 
    JLabel currentIns;  

    JButton execute;
    JButton cycle;
    JButton reset;
    JButton load;
	JButton zeroData;
    JButton exit;

    JPanel[] regs;
    JPanel mem;
    JPanel Imem;
    JPanel Dmem;
	JPanel r;
    JPanel r2;
    JPanel buttons;
    JPanel MARpan;
    JPanel PCpan;
    JPanel regPan;

    JPanel right;

    Processor processor;

	/**
	* The main method of this isasim Simulator.
	*
	* This method creates a Processor object based on the 
	* configuration settings stored as constants in this class.
	*
	* A Gui window is then instantiated that will be tied to
	* that processor, and waits for user data.
	*
	* @param args will accepta filename string to compile
	* to instruction memory.
	**/
	public static void main(String[] args){

		Processor processor;
		if ( args.length < 1){
    		processor = new Processor(REGISTER_WIDTH, REGISTER_FILE_SIZE, INSTRUCTION_MEM_SIZE, DATA_MEM_SIZE,WORD_WIDTH, INSTR_FILE, DAT_FILE);
		}else{
			compile(args[0]);
			processor = new Processor(REGISTER_WIDTH, REGISTER_FILE_SIZE, INSTRUCTION_MEM_SIZE, DATA_MEM_SIZE,WORD_WIDTH, INSTR_FILE, DAT_FILE);
		}
		new Simulator(processor);

    }

	/**
	* Simulator Constructor.
	*	
	* Creates the Gui object for this simulation.
	* Displays the register file and values,
	* Data and Instruction memories,
	* PC and MAR values, and the
	* Current instruction that the processor 
	* will execute next.
	* 
	* @param p the Processor object associated with 
	* this Simulator
	*
	**/
	public Simulator(Processor p){
        processor = p;

        window = new JFrame("IsaSimulator");
        content = window.getContentPane();
        content.setLayout(new GridLayout(1,2));
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        r = new JPanel();
        regs = new JPanel[16];
        r.setLayout(new GridLayout(16,1));
        registers = new JLabel[16];
        registerName = new JLabel[16];
        for (int i = 0; i < 16; i++) {
            registers[i] = new JLabel(processor.regFile[i].getValue()+" | "+Utilities.binToInt(processor.regFile[i].getValue()));
            registers[i].setBorder(BorderFactory.createLineBorder(Color.black));
			registers[i].setOpaque(true);
			registers[i].setBackground(Color.WHITE);
            registerName[i] = new JLabel("Register &r"+i);
            registerName[i].setBorder(BorderFactory.createLineBorder(Color.black));
            regs[i] = new JPanel();
            regs[i].setLayout(new GridLayout(1,2) );
            regs[i].setBorder(BorderFactory.createLineBorder(Color.black));
            regs[i].add(registerName[i]);
            regs[i].add(registers[i]);
            r.add(regs[i]);
        }

        ButtonListener listener = new ButtonListener();
        execute = new JButton("Execute");
        cycle = new JButton("One Step");
        reset = new JButton("RESET");
        load = new JButton("Load instructions");
        exit = new JButton("EXIT");
		zeroData = new JButton("Clear Data");
        currentIns = new JLabel("Current Instruction: "+instString(processor.instructionMemory.read()));
        execute.addActionListener(listener);
        cycle.addActionListener(listener);
        reset.addActionListener(listener);
        load.addActionListener(listener);
        exit.addActionListener(listener);
		zeroData.addActionListener(listener);
        buttons = new JPanel();
        buttons.setLayout( new GridLayout(2,1) );
        JPanel b = new JPanel();
        b.setLayout(new GridLayout(2,3) );
        b.add(cycle);
        b.add(execute);
        b.add(reset);
        b.add(load);
        b.add(zeroData);
		b.add(exit);
        buttons.add(currentIns);
        buttons.add(b);

        r2 = new JPanel();
        r2.setLayout( new GridLayout(1,2) );

        Mar = new JLabel("Memory Access Register");
        Pc = new JLabel("Program Counter");
        MAR = new JLabel( processor.MAR.getValue()+" | "+Utilities.binToInt(processor.MAR.getValue()) );
        PC = new JLabel( processor.PC.getValue() +" | "+Utilities.binToInt(processor.PC.getValue()) );
        Mar.setBorder(BorderFactory.createLineBorder(Color.black));
        Pc.setBorder(BorderFactory.createLineBorder(Color.black));
        MAR.setBorder(BorderFactory.createLineBorder(Color.black));
        PC.setBorder(BorderFactory.createLineBorder(Color.black));

        MARpan = new JPanel();
        MARpan.setLayout(new GridLayout(3,1) );
        MARpan.add(Mar);
        MARpan.add(MAR);
        MARpan.add(new JLabel("Data Memory") );

        PCpan = new JPanel();
        PCpan.setLayout(new GridLayout(3,1) );
        PCpan.add(Pc);
        PCpan.add(PC);
        PCpan.add(new JLabel("Instruction Memory"));
        r2.add(MARpan);
        r2.add(PCpan);

        PCpan.setBorder(BorderFactory.createLineBorder(Color.black));
        MARpan.setBorder(BorderFactory.createLineBorder(Color.black));

        mem = new JPanel();
        mem.setLayout( new GridLayout(1,2) );
        Dmem = new JPanel();
        Dmem.setLayout( new GridLayout(128,1) );
        Imem = new JPanel();
        Imem.setLayout( new GridLayout(128,1) );
        dmem = new JLabel[128];
        imem = new JLabel[128];

        for (int i = 0; i < 128; i++ ){
            //add the memory values here;
            dmem[i] = new JLabel(i+" | "+processor.dataMemory.data[i]);
            imem[i] = new JLabel(i+" | "+processor.instructionMemory.data[i]);
			imem[i].setOpaque(true);
			imem[i].setBackground(Color.WHITE);
            Dmem.add(dmem[i]);
            Imem.add(imem[i]);
        }
		imem[0].setBackground(Color.GRAY);
        JScrollPane dscroll = new JScrollPane(Dmem);
        JScrollPane iscroll = new JScrollPane(Imem);
        dscroll.setAutoscrolls(true);
        iscroll.setAutoscrolls(true);
        iscroll.getVerticalScrollBar().setUnitIncrement(16);
        dscroll.getVerticalScrollBar().setUnitIncrement(16);
        mem.add(dscroll);
        mem.add(iscroll);
        mem.setBorder(BorderFactory.createLineBorder(Color.black));
        right = new JPanel();
        right.setLayout(new GridLayout(3,1) );


        right.add(r2);
        right.add(mem);
        right.add(buttons);

        r.setBorder(BorderFactory.createLineBorder(Color.black));
        right.setBorder(BorderFactory.createLineBorder(Color.black));

        content.add(r);
        content.add(right);
        window.setSize(640,650);
        window.setVisible(true);
    }
	/**
	* Button Listener inner class.
	*
	* This inner class is utilized by the simulator
	* to take appropriate action based on user 
	* input
	**/
    class ButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e){
            Component whichOne = (Component)e.getSource();
            if (whichOne == execute){
                do {
                    update();
                } while( processor.oneCycle() );
            }
            else if(whichOne == cycle){
                if(processor.oneCycle()) update();
            }else if(whichOne == reset){
                processor.reset();
                update();
            }else if (whichOne == load) {
                String path = JOptionPane.showInputDialog(window,"Enter the filename to compile:");
                if (path != null) {Simulator.compile(path);processor.reset();}
                update();
            }
			else if (whichOne == zeroData){
				processor.zeroData();
				update();
			}
            else if (whichOne == exit){
                System.out.println("Exiting...");
                processor.terminate();
                System.exit(0);
            }
        }
    }

	/**
	* Update the Gui display.
	*
	* Replace the displayed text of the 
	* registers, memories, and instruction
	* with the values currently stored by the
	* processor.
	**/
    private void update(){
        for (int i = 0; i < 16; i++){
            registers[i].setText(processor.regFile[i].getValue()+" | "+Utilities.binToInt(processor.regFile[i].getValue()));
			registers[i].setBackground(Color.WHITE);
        }
        for (int i = 0; i < 128; i++ ){
            dmem[i].setText(i+" | "+processor.dataMemory.data[i]);
            imem[i].setText(i+" | "+processor.instructionMemory.data[i]);
			imem[i].setBackground(Color.WHITE);
        }
        PC.setText(processor.PC.getValue()+" | "+Utilities.binToInt(processor.PC.getValue()));
		imem[Utilities.binToInt(processor.PC.getValue())].setBackground(Color.GRAY);
        MAR.setText(processor.MAR.getValue()+" | "+Utilities.binToInt(processor.MAR.getValue()));
        if ( Utilities.binToInt(processor.PC.getValue()) < processor.imemSize && Utilities.binToInt(processor.PC.getValue()) >= 0)
        currentIns.setText("Current Instruction: "+instString(processor.instructionMemory.read()));
        else currentIns.setText("Current Instruction: NONE");
    }
	
	/**
	* Convert a binary instruction to code.
	*
	* This method will create a string
	* based on the encoding scheme of this system
	* to provide a meaningful display of the
	* current instruction.
	*
	* @param instruction binary instruction data.	
	**/
     private String instString(String instruction){
            String ret = "";
            String opfunc = instruction.substring(0,4)+instruction.substring(12,16);
            int RA = Utilities.binToInt("0"+instruction.substring(4,8));
            int RB = Utilities.binToInt("0"+instruction.substring(8,12));
            int immed = Utilities.binToInt(instruction.substring(8,16));
            int jump = Utilities.binToInt(instruction.substring(4,16));

            switch (opfunc.substring(0,4) ){
                case "0000":
                    switch(opfunc.substring(4,8)){
                        case "1000": ret += "add";break;
                        case "0001": ret += "sub";break;
                        case "0010": ret += "nor";break;
                        default: return "NONE";
                    }
                    ret += " &r"+RA+", &r"+RB;
                    break;
                case "0001":
                    ret += "la &r"+RA+" "+immed;
                    break;
                case "0010":
                    ret += "mr &r"+RA+" "+immed;
                    break;
                case "0110":
                    ret += "mw &r"+RA+" "+immed;
                    break;
                case "0100":
                    ret += "addi &r"+RA+" "+immed;
                    break;
                case "0111":
                    ret += "beq &r"+RA+" "+immed;
                    break;
                case "1000":
                    ret += "j "+jump;
                    break;
                default:
                    return "NONE";
            }
            return ret;
    }

	/**
	* Convert an input file to binary instruction data.
	*
	* This method reads an input file and creates
	* instruction data based on that code, and writes
	* the instructions to the instruction memory file
	* associated with this simulator. Invalid instructions
	* will be skipped, and the file will be filled to capacity.
	* Instruction beyond this systems capacity will be discarded,	
	* and if the input file contains less istructions than the
	* system can handle, empty instructions will be included
 	* to fill the instruction memory file.
	*
	* @param filename the file to compile.	
	**/
	public static void compile(String filename){
		try {
			BufferedReader infi = new BufferedReader(new FileReader(filename) );
			BufferedWriter outfi = new BufferedWriter(new FileWriter(INSTR_FILE) );
			int lineCount = 0;
			while(infi.ready() && lineCount < INSTRUCTION_MEM_SIZE){		
				String line = infi.readLine();
				String[] parts = line.trim().split("\\s+");
				String writeLine = "";
				writeLine = "";
				if (parts.length == 0 || parts[0].startsWith("#")){
					//skip it, its a comment
				}
				else {
					switch(parts[0]){
						case "la":
							writeLine+="0001";
							writeLine+=Iinst(parts)+'\n';
							break;
						case "mw":
							writeLine+="0110";
							writeLine+=Iinst(parts)+"\n";
							break;
						case "mr":
							writeLine+="0010";
							writeLine+=Iinst(parts)+"\n";
							break;
						case "lw":
							if (parts.length < 4){
								//interpret as mr
								writeLine+="0010";
								writeLine+=Iinst(parts)+"\n";
								break;
							}
							else{
								writeLine+="0001";
								String[] iParts = {"",parts[2],parts[3]};
								writeLine+=Iinst(iParts)+'\n';
								String[] rParts = {"",parts[1],parts[3]};
								writeLine+="0010"+Iinst(rParts)+"\n";
							}
							break;
						case "sw":	
							if (parts.length < 4 ){
								//interpret as mw
								writeLine += "0110";
								writeLine += Iinst(parts)+"\n";
							} else{
								String[] iParts = {"",parts[2],parts[3]};
								String[] rParts = {"",parts[1],parts[3]};
								writeLine+="0001"+Iinst(iParts)+"\n";
								writeLine+="0110"+Iinst(rParts)+"\n";
							}
							break;
						case "addi":
							writeLine+="0100";
							writeLine+=Iinst(parts)+"\n";
							break;
						case "add":
							writeLine+="0000";
							writeLine+=Rinst(parts);
							writeLine+="1000\n";
							break;
						case "sub":
							writeLine+="0000";
							writeLine+=Rinst(parts);
							writeLine+="0001\n";
							break;
						case "nor":
							writeLine+="0000";
							writeLine+= Rinst(parts);
							writeLine+="0010\n";
							break;
						case "beq":
							writeLine+="0111";
							writeLine+=Iinst(parts)+"\n";
							break;
						case "j":
							writeLine+="1000";
							writeLine+=Utilities.intToBin(12,Integer.parseInt(parts[1]));
							break;
						default:	
							writeLine+="0000000000000000\n";
							break;

					}
					lineCount++;
					outfi.write(writeLine);
				}

			}
			while(lineCount < INSTRUCTION_MEM_SIZE){
				outfi.write("0000000000000000\n");
				lineCount++;
			}
			infi.close();
			outfi.close();
			BufferedWriter data = new BufferedWriter( new FileWriter(DAT_FILE) );
			for (int i = 0; i < 128; i++ ){
				data.write("0000000000000000\n");
			}
		}
		catch (IOException e){
			System.out.println("Compiler Failure: invalid input file");
		}
	}

	/**
	* Used by the compiler to interpret R-type instructions.
	*
	* @param parts the current line of code being compiled.
	* @return the binary register addresses for this instruction.
	**/
	public static String Rinst(String[] parts){
		if(!parts[1].startsWith("&r") || !parts[2].startsWith("&r")){
			System.out.println("ERROR: R-type instructions must include two");
			System.out.println("input registers as '&ra' and '&rb' where 'a'");
			System.out.println("and 'b' are the integers representing the registers.");
			return"";
		}
		else{
			String addr1 = Utilities.intToBin(4,Integer.parseInt(parts[1].substring(2)));
			String addr2 = Utilities.intToBin(4,Integer.parseInt(parts[2].substring(2)));
			return addr1+addr2;
		}

	}
	
	/**
	* Used by the compiler to interpret I-type instructions.
	*
	* @param parts the current line of code being compiled.
	* @return the binary register address as well as the binary immediate value.
	**/
	public static String Iinst(String[] parts){
		String addr = Utilities.intToBin(4,Integer.parseInt(parts[1].substring(2)));
		String immed;
		if (parts.length < 3){
			immed = "00000000";
		}else{
			immed = Utilities.intToBin(8,Integer.parseInt(parts[2]));
		}
		return addr+immed;
	}

}
