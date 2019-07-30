#!/bin/bash

echo "BEGINNING VALUE GENERATION"

echo "Seed is $1"
echo "Number of values is $2"
echo "Number of cores is $3"

echo "running make on each PRNG and the Test Suite"

echo "~Middle Square and LCG~"
make 

cd ./ThreeFry
echo "~ThreeFry~"
make
cd ..
cd ./TestSuite
echo "~TestSuite~"
make
cd ..

echo "Completed Makes"

echo "Generating values"

if [ $3 -lt 1 ]; then
    echo "Argument 3 was less than 1. Generating serial versions"
    echo "Middlesquare out to middlesquare_ser_$1_$2.txt"
    ./middlesquare_ser $1 $2 > results/middlesquare_ser_$1_$2.txt
    
    echo "LCG out to lcg_ser_$1_$2.txt"
    ./lcg_ser $1 $2 > results/lcg_ser_$1_$2.txt

    echo "Threefry"
    ./ThreeFry/threefry_ser $1 $2>results/threefry_ser_$1_$2.txt
else
    echo "Generating parallel version using $3 cores"
    echo "Middlesquare out to middlesquare_par_$1_$2_$3.txt"
    OMP_NUM_THREADS=$3 ./middlesquare_par $1 $2> results/middlesquare_par_$1_$2_$3.txt
    echo "LCG"
    OMP_NUM_THREADS=$3 ./lcg_par $1 $2>results/lcg_par_$1_$2_$3.txt
    echo "Threefry"
    OMP_NUM_THREADS=$3 ./ThreeFry/threefry_par $1 $2>results/threefry_par_$1_$2_$3.txt
fi



