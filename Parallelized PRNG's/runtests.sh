#!/bin/bash

echo "Beginning tests!"

for alg in results/*.txt; do
    echo "$alg"
    TestSuite/prng_a $alg > "$alg"_results.txt
done

echo "Done with tests"
