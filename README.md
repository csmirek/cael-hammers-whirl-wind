This project is for testing the feasibility of using this method of password retrieval.

The basic setup is to test various facets of the algorithm in a threaded manner.

This is done by creating a synchronized set of possible passwords and having multiple (20) threads run the getPW function in parallel and checking for duplicated passwords.

Currently this is done for single machines, ie. meant to be run on large multicore servers.  As I understand some more about Java I will think about creating a distributed system aspect to scale up to much larger tasks.

Current code tests the algorithm for the situation where all inputs are known except the first integer input.

***This code base will be the only one with a commented algorithm/generateFile class

    Current inputs are:
    string input 1 = "yahoo"
    string input 2 = "xz62rP"
    integer input 1 = ***testing***
    integer input 2 = 0
    integer input 3 (length) = 15

###Edits
    *10/22/2012
    Current code now tests when only the length and first string input are known for 25 million combinations
    Algorithm changes to complement the multiple unknown inputs for added security
    *10/25/2012
    Current code allows multiple testing types: iLoop, jLoop, and threeLoop
    iLoop tests the first numerical input from 0-30 million
    jLoop tests the second numerical input from 0-30 million
    threeLoop tests when only the length and first string input are known, this has 6 supported testing types: alpha, ani, alpha20, alpha40, and num
   	alpha, ani, and num test 25 million combinations each
   	alpha20 and alpha40 test 20 million combinations each