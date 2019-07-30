/**
 * dartsLab.c
 *
 * Adapted from the Darts Lab
 */

#include <limits.h>
#include <stdio.h>
#include <stdlib.h>

long total_darts = 0;           // dart count
long darts_in_circle = 0;       // number of hits

void throw_darts(long total_darts, long alg, char* fileName, long cores)
{
    unsigned long seed = 0;
    unsigned long hits = 0;

    char* outputFN = malloc(sizeof(char)*100);
    FILE* input;
    FILE* output;
    unsigned long  mod = 100000000;
    double divisor =      99999999;

    switch(alg) {
        case 1:
            input = fopen(fileName, "r");
            sprintf(outputFN, "results/dartResults/darts_lcg_%lu_%lu.txt", total_darts, cores);
            output = fopen(outputFN, "w");
            break;
        case 2:
            input = fopen(fileName, "r");
            sprintf(outputFN, "results/dartResults/darts_mid_%lu_%lu.txt", total_darts, cores);
            output = fopen(outputFN, "w");
            mod =    10000;
            divisor = 9999;
            break;
        case 3:
            input = fopen(fileName, "r");
            sprintf(outputFN, "results/dartResults/darts_fry_%lu_%lu.txt", total_darts, cores);
            output = fopen(outputFN, "w");
            mod =    10000000000000;
            divisor = 9999999999999;
            break;
        default: printf("Not an algorithm\n"); return;
    }

    for (long dart = 0; dart < total_darts; dart++) {
        // throw a dart by generating a random (x,y) coordinate pair
        //
        fscanf(input, "%lu", &seed); 
        seed = seed % mod;
        double x = (double)seed / (double)divisor;
//        fprintf(output, "%lu == %lu\n", seed, ULONG_MAX);
        fscanf(input, "%lu", &seed);
        seed = seed % mod;
        double y = (double)seed / (double)divisor;
//        fprintf(output, "%lu == %lu\n", seed, ULONG_MAX);
        double dist_sq = x*x + y*y;

        // update hit tracker
        if (dist_sq <= 1.0) {
	    hits++;
        }
    }

    fprintf(output, "Hits: %lu\n", hits);
    double pi_est = 4 * hits / ((double) total_darts);

    switch(alg) {
        case 1: 
            fprintf(output, "Estimated pi: %e using Algorithm LCG", pi_est);
            break;
        case 2:
            fprintf(output, "Estimated pi: %e using Algorithm Middle Square", pi_est);
            break;
        case 3:
            fprintf(output, "Estimated pi: %e using Algorithm ThreeFry", pi_est);
            break;
        default: printf("How'd you get here???"); break;
    }

    free(outputFN);
    fclose(input);
    fclose(output);
    return;
}

int main(int argc, char* argv[])
{
    // check and parse command-line arguments
    if (argc != 5) {
        printf("Usage: %s <num-darts> <PRNG-algorithm> <random-values-file> <num-cores>\n", argv[0]);
        exit(EXIT_FAILURE);
    }
    total_darts = strtoll(argv[1], NULL, 10);
    long alg = strtol(argv[2], NULL, 10);
    char* srcName = argv[3];
    long cores = strtol(argv[4], NULL, 10);

    // simulate dart throws
    throw_darts(total_darts, alg, srcName, cores);

    return EXIT_SUCCESS;
}


