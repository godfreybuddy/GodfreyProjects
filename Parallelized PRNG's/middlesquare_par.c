/**
 * Parallel Version of Middle Square Method
 *
 *
 *
 * --How it Works--
 *  The Middle Square Method generates pseudorandom numbers through the
 *  following steps:
 *
 *  1) Take any number with 'n' digits, n being an even number
 *  2) Square this number
 *  3) If the square of the number is less than 2n, add leading zeros until
 *  	the result has 2n digits
 *  4) Take the middle n digits of the square- this is the next number in 
 *  	this sequence of pseudorandom numbers, and the next seed
 *
 * --Weaknesses of this Method--
 *  In its basic form, this method has never been practical enough for 
 *  real world use, for multiple reasons. Especially problematic is the 
 *  relatively small period of numbers that can be generated before
 *  this method begins repeating a small loop of the same values. As a general
 *  rule, for an 'n' digit seed, it is provable that the period can never be
 *  longer than 8^n values. 
 *
 *  Importantly, there is a version of this algorithm that solves these defects
 *  using  something called a Weyl sequence, which is designed to prevent
 *  reducing to zero and repetition. 
 *
 * --The Weyl Sequence--
 *  A Weyl sequence is generated based off of an integer 'k', which is 
 *  relatively prime to an integer modulus 'm'. 
 *  The sequence is all multiples of this integer (i.e. 0, k, 2k...).
 *  This sequence is equidistributed modulo m from the interval [0, m).
 *
 *  In practical application for this problem, the values in this sequence
 *  are added to the results of squaring before taking the middle values.
 *
 * --Changes in the Parallel form--
 *  To parallelize this method of generation, I added omp pragma calls to the for loop which generates
 *  the set of values. In order effectively partition the requested number of results, I gave each thread
 *  its own seed which was incremented from the original seed by 1. 
 *  The parallelized version of this program, though admittedly suffering from most of the same problems
 *  as the serial version, does have improved performance at higher digit values and number of requests. 
 *  The parallel version also has an interesting advantage in that it protects slightly from the problems
 *  of loop locks that the serial version would encounter when requesting a large number of values. It may be
 *  worth investigating if the Weyl sequence would further improve these results, but this program, even in a parallel
 *  form, will be a good baseline for a very poor prng to use for Monte Carlo problems.
 *
 * Ian Staton
 *
 * Parallel PRNG algorithms
 *
 * TODO Make main method return an array
 * TODO Add Weyl sequence
 */

#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <string.h>
#ifdef _OPENMP
# include <omp.h>
# define START_TIMER(X) double _timer_ ## X = omp_get_wtime();
# define STOP_TIMER(X) _timer_ ## X = omp_get_wtime() - (_timer_ ## X);
# define GET_TIMER(X) (_timer_ ## X)
#else
# include <sys/time.h>
  struct timeval _tv;
#define START_TIMER(X) gettimeofday(&_tv, NULL); \
    double _timer_ ## X = _tv.tv_sec+(_tv.tv_usec/1000000.0);
#define STOP_TIMER(X) gettimeofday(&_tv, NULL); \
    _timer_ ## X = _tv.tv_sec+(_tv.tv_usec/1000000.0) - (_timer_ ## X);
#define GET_TIMER(X) (_timer_ ## X)
#endif

//Timer structure taken from custom timing macros from lab 5


uint32_t seed = 0;     //public seed value
int seedlen = 0;  //public length of the seed

//comment out this to prevent debug output
//#define DEBUG


//Uncomment this to make use of concatenation
//CATOUT simply prints the values to stdout with no additional information. 
#define CATOUT

/**
 *  Generate the next value in the random sequence and returns it.
 *  This returned value is also set as the seed.
 */
uint32_t generatevalue(uint32_t seedval){
    char squarestring[1048];
    char temp[1048];
    char middle[1048];
    uint64_t square = seedval * seedval;
    #ifdef DEBUG
    printf("Seed is %u\nSquare is %lu\n", seedval, square);
    #endif

    sprintf(squarestring, "%lu", square);
    #ifdef DEBUG
    printf("Squarestring is %s\n", squarestring);
    #endif

    while( strlen(squarestring) < (seedlen * 2)){
        strcpy(temp, squarestring);
        sprintf(squarestring, "0%s", temp);
    }
    #ifdef DEBUG
    printf("Squarestring is %s\n", squarestring);
    #endif
	
    strncpy(middle, squarestring + (seedlen / 2), seedlen);
    uint32_t returnval = atoi(middle);
    return returnval;
}

int main(int argc, char* argv[]){

    //Argument error checking
    if(argc < 3)
    {
        printf("USAGE: middlesquare seed(int) iterations(int)");
        exit(EXIT_FAILURE);
    }


    //Ensure the length of the seed is even
    if( (strlen(argv[1]) % 2) != 0){
        printf("ERROR: Length of seed must be even.\n");
        exit(EXIT_FAILURE);
    }
    
    //Parse the command line
    seed = atoi(argv[1]);
    seedlen = strlen(argv[1]);
    int iterations = atoi(argv[2]);
    uint32_t* results;

    //Create a results array of size equal to how many values have been
    //requested
    results = malloc(sizeof(uint32_t) * iterations);

    START_TIMER(genvals);
    #ifdef _OPENMP
    #pragma omp parallel 
    #endif
    {
        //TODO Perhaps use Weyl sequence here? For now, just increment the seed by the thread's rank
	//The modulus here is to ensure that myseed has the same number of digits as the regular seed
        uint32_t myseed = (seed + omp_get_thread_num()) % ((seed * 10) - 1);
        //Generate values and print as we go
    #ifdef _OPENMP
        #pragma omp for 
    #endif
        for(int i = 0; i < iterations; i++){
            results[i] = generatevalue(myseed);
	    myseed = results[i];
            #ifdef DEBUG
            printf("Value is %u\n", results[i]);
	    #endif
        }
    }
    STOP_TIMER(genvals);

    #ifdef CATOUT 
    for(int j = 0; j < iterations; j++){
        printf("%u\n", results[j]);
    }
    #else
    printf("Time taken: %.6f\n", GET_TIMER(genvals));
    #endif
    //Free memory and exit
    free(results);

    return 0;

    
   
    
}
