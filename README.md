# cs470prngs FINAL VERSION
Repository for group project for CS 470 of Spring 2019

URL FOR THE REPOSITORY:
https://github.com/CFHistory/cs470prngs.git

LIST OF FILES
	
EarlySemester - this folder contains all of the files from deliverables throughout the semester
	
	Monte Carlo Simulation Writeup.docx - our formal project proposal
		
		PRNG_PosterRoughDraft.pptx- our final poster
	
		discussion.pdf - Our initial idea proposal
		
		survey.txt - a mid-project survey
	
	TestSuite- this folder contains all of the files needed to test the output of a PRNG algorithm
	
		Makefile- this makefile handles creating the prng_a executable
	
		prng_a.c - this is the c file for our testing executable, which runs a comprehensive test on a text file containing values that it is provided. See RUNNING OUR TEST SUITE below for specific directions.
		
		repetition.c - this is the c file for the repetition test functionality
		
		repetition.h - this is the header file for the repetition test funcitonality
	
	ThreeFry- this folder contains all of the files needed to run the threefry PRNG algorithm
	
		features - this folder contains dependencies for Threefry, which were provided from the random123 repository referred to in our works cited.
		
		Makefile - this makefile creates our threefry_par and threefry_ser executables
		
		ThreeFry Basic Use- provided by random123, details how to use threefry
		
		array.h - a header file from random123 that facilitates threefry
		
		threefry.h - a header file from random123 that facilitates threefry
		
		threefry_par.c - the c file for the threefry parallel implementation
		
		threefry_ser.c - the c file for the threefry serial implementation
	
	results- this folder is where all the results from running generatevalues.sh and runtests.sh are placed
		
		dartresults- this folder contains specific results for the monteDarts program
		
		There are a large number of value and result file in this folder, the syntax of these files is detailed later in the GENERATING VALUES section. We have provided an example testing suite as an example of how our testing suite works, but it is recommended to generate a fresh set of values. 
	
	Makefile- this Makefile handles making lcg, middlesquare, monteStock and monteDarts
	
	README.md- this file!
	
	dartsLAb.c- the original darts Lab file which our Darts Monte Carlo file was based on
	
	generatevalues.sh- this bash script makes all the algorithms in the directory and generates values using each. See the GENERATING VALUES section for how to use this
	
	lcg.c - the basic version of the lcg algorithm. not used
	
	lcg_par.c- the parallel version of LCG
	
	lcg_ser.c the serial version of LCG that is used for testing
	
	makeclean.sh - this bash script runs make clean on all files in this project, as well as removing all files from the results directory
	
	middlesquare_par.c - the parallel version of middlesquare
	
	middlesquare_ser.c - the serial version of middlesquare
	
	monteCarloStock.c - the Monte Carlo simulation for stock prices. 
	
	monteDarts.c - the Monte Carlo simulation for using darts to calculate pi.
	
	results.ods - a spreadsheet containing three sheets, with the results of running our testing suite detailed for each algorithm we're using
	
	runtests.sh - a bash script to run our testing executable on every results file in our results directory
	

IMPORTANT
	Before running any tests, it is highly recommended that you run ./makeclean.sh in the main directory to clear out old result sets and past unneeded executables. 

OVERVIEW
	We have three different prng algorithms we use to generate random values. They are detailed as follows in the section below.

	Middlesquare
		Middlesquare works by taking the provided seed, squaring it, and using the middle n values (n being the number of digits in the original seed) as the next value in the sequence, and thus the next seed. It is not used practically in any sort of computing, and never has been. Middlesquare is our control, used as a basis for what a very terrible PRNG algorithm looks like 
		Importantly, middlesquare only works with seeds that have even numbers of digits.
		The middlesquare executables are in the main directory, they are middlesquare_par and middlesquare_ser for the parallel and serial versions, respectively.

	LCG
		LCG generates new values using a provided seed and an internal, calculated modulus. It is a commonly used PRNG algorithm, with many other algorithms being based off of it. It does not improve in usability much through parallelization, however.
		The LCG executables are in the main directory, they are lcg_par and lcg_ser, for the parallel and serial versions, respectively. 

	Threefry
		Threefry is an algorithm that is designed to work with parallelization. It uses both a seed, and an internal counter, to make distributing the task of generating values possible to parallelize. It should be noted that, unlike our other two algorithms, Threefry has a supporting library, and thus exits in its own folder. 
		The Threefry executables are in the ThreeFry folder, they are threefry_par and threefry_ser for the parallel and serial versions, respectively. 
	
To run any of our algorithms manually, be sure to run make in the directory in which the related .c files exit, and then run the middlesquare_par, threefry_par, or lcg_par (or, if you wish, the related \_ser file), in the following format:
	./{algorithm}_par {seed} {number of values}
	
This will print the results of the algorithm to standard out, which allows you to concatenate it to a file easily with '> out.txt' 

Note that we have provided bash scripts to serve this function for you, detailed below.

GENERATING VALUES
	By default, the final deliverable version of this project comes with the CATOUT flag enabled in each .c file, and the DEBUG flag disabled. 
	We provide a generatevalues bash script which can generate a suite of results values, outputting a file to the results directory for each algorithm of our three. To run this script, simply type in the following on the command line:
	./generatevalues.sh X Y Z
	
	where X is the seed used to generate values, Y is the number of values to generate, and Z is the number of cores to use. Providing a number of cores less than 1 will cause the script to use the serial versions of each algorithm. 
	
	This script will output a .txt file for each algorithm, in the format of {algorithm}_par_{seed}_{number of values}_{number of cores}.txt, or {algorithm}_ser_{seed}_{number of values}.txt for a serial file. For example, if you run generate values with a seed of 1234, 1000 values, and 4 cores, you will see three text files in the results directory, in the format {lcg, middlesquare or threefry}_par_1234_1000_4.txt. 
	
	There are certain restrictions for our generatevalues script, which are listed below.
	1) The seed must have an even number of digits to work properly with middlesquare
	2) The number of values you provide must be evenly divisible by the number of cores you provide
	
	It is also recommended to use powers of 2 for your number of cores; 1, 2, 4, 8, 16, 32, etc.
    
	

RUNNING OUR TEST SUITE
	Once you have a number of value text files in your results directory, all you have to do is run the ./runtests.sh script in the main directory. This script automatically runs our prng_a testing executable with all of the .txt files in the results folder. The result of running this script is that for each file in that folder, a file will be produced with an identical name, with \_results.txt appended to the end. 
	
IMPORTANT: running the results script when you already have results in the folder is not supported, and will cause errors. 


If you wish to manually run tests, you can simply run our prng_a executable on a given text file. Given an output file out.txt, which contains only a string of random numbers delimited by newline characters, you can run the test from the main folder by running
	./TestSuite/prng_a out.txt
This will print the results of our tests to standard out. 

CLEANING UP

If at any time you need or wish to clean up the project folder, simply run ./makeclean.sh in the main directory. This will make clean on all our algorithms and our test suite, as well as remove all content from the results folder. 

TIMING

Given the extensive timing tests that we have run on our algorithms, we do not believe that timing is essential to understanding our project. However, if you would like to report the time of any of our algorithms, simply ensure that at the top of the related .c file, the CATOUT define is commented, and run make manually in the directory of the given algorithm you wish to test. 

