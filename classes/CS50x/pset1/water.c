#include<stdio.h>
#include<cs50.h>

int main(void) {


printf("Please enter how many minutes it takes you to shower: ");
int x = GetInt();

printf ("bottles: %d\nminutes: %d\n", x, 192 *x / 16);

}


