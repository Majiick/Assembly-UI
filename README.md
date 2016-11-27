# Assembly-UI

Takes in any* PE32 Windows executable and draws a function call graph of the assembly contained within.

This project uses the disassembling engine Capstone and takes in input files from a 32-bit executable protector I made https://github.com/Majiick/DPro .
Basically the protector maps the executable in virtual memory so that the assembly can be interpreted correctly, and this project uses that mapped binary file.
I also use the generated function imports to fill in some of the function names that are imported in this project.

The project starts interpreting assembly at the entry point of the executable and looks for function defining instructions such as ret and call.
I tested a couple of executables, some work and some don't because for example there's 'mov eax, 0x1000; call eax;' which I don't look for.
And also some assemblies won't follow this function control flow structure, but most will.

# Points of interest in the code:
	Plenty of collection filters and lambdas.
	Hashmaps.
	Try-Catches.
	Class Binary is a singleton.
	File IO in FileReader.
	Enums.
	Interface and extend in Mnemonic_Redirection_Calculator.
	Collections comparator in Code_Block.
	equals override in Mnemonic_Redirection_Calculator Valid_Instruction.
	