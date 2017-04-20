#include <stdio.h>
#include <cs50.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>

int main (int argc, string argv[]) // argc includes the number of arguments 
                                   // argv array stores words ('lockers') that user inputs
{
string s; 
string key;
key = argv[1];
    
if (argc > 2 || argc < 2)
    {
    printf ("You should enter one alphabetical key!\n");
    return 1;
    }

for (int i = 0, y = 0, n = strlen(key); i < n; i++)                         //this loop is needed so ./vigenere returns an error and no segmentation fault
    {
    if (isalpha(key[i]))
        {
        y++;
        }
    else  
        {
        printf("You messed up big time!\n");
        return 1;
        }
    } 

// printf("Tell me something to encrypt: ");                                //the phrase should be taken out for CS50's te
s = GetString();
key = argv[1];
int j = strlen(key);
    
            for (int i = 0, r = 0, n = strlen(s); i < n; i++)               //this for loop checks the indexes in the key and phrase  
                {
                if (s[i] >= 32 && s[i] <= 64)                               //excludes non-alphabetical characters
                    {
                    printf ("%c", s[i]);
                    }
                if (s[i] >= 'a' && s[i] <= 'z') 
                    {
                    s[i] = s[i] - 97;
                    if (key[r] >= 'a' && key[r] <= 'z')                     //adjusts for capital or lowercase letters
                        {key[r] = key[r] - 97; }
                    else {key[r] = key[r] - 65; }
                    s[i] = (s[i] + key[r%j]) % 26;                          //the r variable must be indepndent from the for loop so that it does not count a non-alphabetical character
                    printf ("%c", s[i] + 97);
                    r++;
                    }
                if (s[i] >= 'A' && s[i] <= 'Z') 
                    {
                    s[i] = s[i] - 65;
                    if (key[r] >= 'A' && key[r] <= 'Z')                     //adjusts for capital or lowercase letters
                        {key[r] = key[r] - 65;}
                    else {key[r] = key[r] - 97;}
                    s[i] = (s[i] + key[r%j]) % 26; 
                    printf ("%c", s[i] + 65);
                    r++;
                    }
                
                }
        
printf("\n");
return 0;
}