#include <stdio.h>
#include <stdlib.h>
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

#define CATOUT

/* always assuming int is at least 32 bits */
int rand();
int rseed = 0;
 
inline void ssrand(int x) {
    rseed = x;
}
 
 
inline int rand() {
    return rseed = (rseed * 1103515245 + 12345) & RAND_MAX;
}
 
int main(int argc, char* argv[]) {
    int i;
//    printf("rand max is %d\n", RAND_MAX);
    rseed = atoi(argv[1]);
    int size = atoi(argv[2]);

    #ifndef CATOUT
    FILE* output = fopen("out_lcg_ser.txt", "w");
    #endif
    START_TIMER(genvals);
    for (i = 0; i < size; i++) {
        #ifdef CATOUT
		printf("%d\n", rand());
	#else
        fprintf(output, "%d\n", rand());
        #endif
    }
    STOP_TIMER(genvals);
 
    #ifndef CATOUT
        printf("Time taken: %.6f\n", GET_TIMER(genvals));
        fclose(output);
    #endif
    return 0;
}
