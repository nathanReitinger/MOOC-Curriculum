/**
 * generate.c
 *
 * Computer Science 50
 * Problem Set 3
 *
 * Generates pseudorandom numbers in [0,LIMIT), one per line.
 *
 * Usage: generate n [s]
 *
 * where n is number of pseudorandom numbers to print
 * and s is an optional seed
 */
 
#define _XOPEN_SOURCE

#include <cs50.h>
#include <stdio.h>
#include <stdlib.h> // this header is necessary to use the rand() and srand() functions
#include <time.h> // the header of time is necessary because of the use of "time" in line 45

// constant--has defined a ceiling called LIMIT which is an integer of 65536
#define LIMIT 65536

int main(int argc, string argv[])
{
    // TODO: if the user entered anything but 2 or 3 command-line arguments, complain by printing the below statement
    if (argc != 2 && argc != 3)
    {
        printf("Usage: generate n [s]\n");
        return 1;
    }

    // TODO: convert the string found in the first command-line argument following "./generate" into an integer
    int n = atoi(argv[1]);

    // TODO: if the user entered two command-line arguments following "./generate"
    // then 
    if (argc == 3)
    {
        srand48((long int) atoi(argv[2])); // returns a number using a user-supplied seed, found in argc3 (will produce same random numbers)
    }
    else // if the user entered only one command-line arguemnt following "./generate" then generate semi-random numbers
    {
        srand48((long int) time(NULL)); // returns a number using a seed of the current time
    }

    // TODO: generates n sets of random numbers based on argv[1], see line 35
    for (int i = 0; i < n; i++)
    {
        printf("%i\n", (int) (drand48() * LIMIT));
    }

    // success
    return 0;
}