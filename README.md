##Tool for convert DTMC matrix to IMC matrix


1) Main class 
	
	src\DTMCtoIMC\DtmcToImc.java

2) Libraries 
	
	lib\

3) Store input DTMC matrix text file into the
    
	src\DTMCtoIMC\inputs

4) CLI commands

		  usage: Command line options
 		-d <arg>    Value for Delta
 		-f <arg>    Name of DTMC text file
 		-h,--help
 		-i <arg>    Name for IMC text file

4.1) Example command line for convert DTMC matrix to IMC matrix (If Delta is provided by the user e.g: delta = 0.001) 

	-f DTMC_File_Name -i output_File_name -d 0.001

4.2) Example command line for convert DTMC matrix to IMC matrix (If Delta is not provided by the user) 

	-f DTMC_File_Name -i output_File_name 

5) All the output IMC matrix text files go to 
	
	src\DTMCtoIMC\output