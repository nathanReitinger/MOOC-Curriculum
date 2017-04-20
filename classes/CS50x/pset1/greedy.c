#include<stdio.h>
#include<cs50.h>
#include<math.h>

int main (void) {
                    
float amount; 
float newamount;
int wholeamount;
int count = 0;

do
    {
        printf("What change is due? ");
        amount = GetFloat();
    }
    
while (amount<=0);

newamount = amount * 100;
wholeamount = round(newamount);
        
for (;wholeamount >= 25; wholeamount = wholeamount -25) 
            {
            count = count + 1;
            }
            
for (;wholeamount >= 10; wholeamount = wholeamount -10) 
            {
            count = count + 1;
            }
            
for (;wholeamount >= 5; wholeamount = wholeamount -5) 
            {
            count = count + 1;
            }
            
for (;wholeamount >= 1; wholeamount = wholeamount -1) 
            {
            count = count + 1;
            }

printf("%i\n", count);

return(0);
}
