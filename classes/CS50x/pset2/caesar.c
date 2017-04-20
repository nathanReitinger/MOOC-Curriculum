#include <stdio.h>
#include <cs50.h>
#include <string.h>
#include <stdlib.h>

int main (int argc, string argv[]) // argc includes the number of arguments 
                                   // argv array stores words that user inputs
{
    
int key;                           //these need to be initalized before the "atio(argv[i])" otherwise you get segmentation error 
string s;  

if (argc != 2)
    {
    printf ("You should enter 2 arguments only!\n");
    return 1;
    }
else
    {
    key = atoi(argv[1]);          //this needs to be here and not in lines 10-12 because otherwise "key" expects an int and gets a string
    if (key == 0)  
        {
        printf ("Please give me a positive key!\n");
        return 1;
        }
    else 
        {
        // printf("Tell me a secret, plaintext passcode: "); ...This isn't expected by the CS50 tester, but the line helps humans 
        s = GetString();
        for (int i = 0, n = strlen(s); i < n ; i++)
            {
            if (s[i] >= 'a' && s[i] <= 'z')
                {
                s[i] = s[i] - 97;
                s[i] = (s[i] + key) % 26;
                printf ("%c", s[i]+97);
                }
                
            if (s[i] >= 'A' && s[i] <= 'Z')
                {
                s[i] = s[i] - 65;
                s[i] = (s[i] + key) % 26;
                printf ("%c", s[i]+65);
                }
            
            if (s[i] >= 32 && s[i] <= 64)
                {
                printf ("%c", s[i]);
                }
            }
        printf("\n");
        return 0; 
        }
    }
}
