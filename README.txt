ECE 0142 Final Project. Summer 2017

Created by: Josh Eaton	Date: 06/22/2017
						Last Updated: 07/19/2017


	
To run the simulator:
	- from the 'Simulator' directory,
		javac isasim/Simulator.java
		java isasim.Simulator
	--Note: The instruction memory will be pre-loaded
		with the contents of 'sample.isa'.
		This can be changed either using the load
		instruction feature of the Gui or by running the 
		command with a filename. 
			-Ex. "java isasim.Simulator [newfilename].isa"


This project utilizes the isasim package that can be found
in this directory to simulate the single-cycle datapath 
that is described in the other documentation provided
with this project.

The isasim package is written in Java, and utilizes the 
modularity of Object-Oriented Programming to simulate
the different units of the system as individual classes
that are centrally located in the "Processor" class.

The 'doc' directory included with this project
includes javadoc-style documentation for the classes
utilized by the simulatior. The javadoc files are html, and can
be viewed starting from doc/isasim/package-summary.html

The package also provides a GUI within the Simulator module that displays
information relevant to the functional units of this system.

Aside from the isasim package itself, this directory also includes a
sample program written in this systems code, which can be compiled
by the "Simulator" module to be run by the "Processor".
These files are named with a ".isa" extension. The provided sample
demonstrates each of the functions of this instruction set.

The ".dat" files stored in this directory are utilized by the isasim
package to store/restore memory states of the system.
