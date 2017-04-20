// Nathan Reitinger
// Input of name, in string type, prints out string's initials

#include <stdio.h>
#include <cs50.h>
#include <string.h>

int main (void) 

{
    string s = GetString();
    char space = 32;                                 
    
    for (int i = 0, n = strlen(s); i < n; i++)
        {
            if (i == 0 && s[i] >= 'a' && s[i] <= 'z')                 //prints out uppercase if character is lower             
                printf("%c", s[0] - ('a' - 'A'));                     //applies to character "0" which is first
                
            if (i == 0 && s[i] >= 'A' && s[i] <= 'Z')                 //prints out uppercase if character is upper             
                printf("%c", s[0]);
                
                
            if (s[i] == space && s[i+1] >= 'a' && s[i+1] <= 'z')      //applies to any character following a space
                printf("%c", s[i+1] - ('a' - 'A')); 
                
            if (s[i] == space && s[i+1] >= 'A' && s[i+1] <= 'Z')
                printf("%c", s[i+1]); 
        }
    printf("\n");
    return (0);
}