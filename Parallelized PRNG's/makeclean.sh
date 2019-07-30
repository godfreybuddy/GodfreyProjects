#!/bin/bash

echo "Cleaning"
make clean
cd TestSuite 
make clean
cd ../ThreeFry 
make clean
cd ../results
rm *.txt
cd ..
