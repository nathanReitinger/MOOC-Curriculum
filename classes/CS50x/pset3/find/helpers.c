/**
 * helpers.c
 *
 * Computer Science 50
 * Problem Set 3
 *
 * Helper functions for Problem Set 3.
 */
       
#include <cs50.h>
#include <stdio.h>
#include "helpers.h"

/**
 * Returns true if value is in array of n values, else false.
 */
bool search(int value, int values[], int n)
{
    
int beginning = 0;
int end = n-1;

while (n > 0)
    {
        {
        int middle = (beginning+end) / 2;
        
        if (values[middle] == value) 
            {
            printf("You found a match!\n");
            printf("The match is: %d\n", values[middle]);
            return true;
            }
        if (values[middle] > value)  
            {
            end = middle - 1;
            n--;
            }
        if (values[middle] < value)
            {
            beginning = middle + 1;
            n--;
            }
        }
    }
printf("whoops\n");
return false;
}

/**
 * Sorts array of n values.
 */
 
void sort(int values[], int n)
{
    // TODO: implement an O(n^2) sorting algorithm

int temp;
int counter = 1;

if (counter != 0) 
    {
    for (int i = 0; i < n; i++)
        {
        for (int x = i+1; x < n; x++)
            {
            if (values[i] > values[x])
                {
                temp = values[x];
                values[x] = values[i];
                values[i] = temp;
                counter++;
                }
            }
        }
    }
}