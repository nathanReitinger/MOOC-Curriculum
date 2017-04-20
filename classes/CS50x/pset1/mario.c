#include <stdio.h>
#include <cs50.h>
 
int main(void)
{
    int i, rows, spaces, pound;
  
    do
    {
    printf("height:");
    rows = GetInt();
    }
    while ((rows < 0) || (rows > 23));
    
    for (i = 1; i <= rows; i++) 
    {
        for (spaces = (rows - i); spaces > 0; spaces--)
        {
        printf(" "); 
        }
        for (pound = 0; pound < (i + 1); pound++)
        {   
        printf("#"); 
        }
 
    printf("\n");
    }
return 0;
}
    
    
    
