#include <stdio.h>
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
//    printf("rand max is %d\n", RAND_MAX);

    #ifndef CATOUT
    FILE* output = fopen("out_lcg_par.txt", "w");
    #endif
    rseed = atoi(argv[1]);
    int size = atoi(argv[2]);
    int vals[size+200];
            
    #ifdef _OPENMP
    #pragma omp parallel 
    {   
      int myRank;
      int mySize;
      myRank =  omp_get_thread_num();
      mySize = size/omp_get_max_threads();
    
    
      int cap = mySize + (mySize*myRank);
      int myStart = (mySize*myRank);
    
      #pragma omp barrier
      START_TIMER(genvals);

      for (int i = myStart; i < cap; i++) {
        vals[i] = rand();
      }
      #pragma omp barrier
      STOP_TIMER(genvals);
    
    }
    for (int i = 0; i < size; i++) {
        #ifdef CATOUT
        printf("%d\n", vals[i]);
        #else
        fprintf(output, "%d\n", vals[i]);
        #endif
    }
    #endif
    #ifndef CATOUT
    printf("Time taken: %.6f\n", GET_TIMER(genvals));
    fclose(output);
    #endif
    return 0;
}
