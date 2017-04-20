/**
 * recover.c
 *
 * Computer Science 50
 * Problem Set 4
 *
 * Recovers JPEGs from a forensic image.
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <cs50.h>


int main(void)
{
    FILE* inptr = fopen("card.raw", "r");
    FILE* outptr = NULL;                                                        //at first, outptr is set to NULL, waiting to be filled
    
    if (inptr == NULL)
        {
        printf("Could not open card.raw.\n");
        return 0;
        }
    
    unsigned char buffer[512];
    int count = 0;                                                              //use for the conditional opening of new files
    char title[8];                                                              //in conjunction with the sprintf function
    
    //continue until the EOF
    while (fread(&buffer, sizeof(buffer), 1, inptr) == 1) //(!feof(inptr))
        {
        if (buffer[0] == 255 && buffer[1] == 216 && buffer[2] == 255 && (buffer[3] >= 224 || buffer[3] <= 239))
            {
                if (count == 0)                                                 //here, we need to open a new file because our magic numbers were found
                    {
                    sprintf(title, "%03d.jpg", count);
                    outptr = fopen(title, "a");
                    count++;
                    }
                else                                                            //if this isn't the first time through, we need to close the previous outptr
                    {
                    fclose (outptr);
                    sprintf(title, "%03d.jpg", count);
                    outptr = fopen(title, "a");
                    count++;
                    }

            }
        if (count > 0)                                                          //if no file is opened, no file will be written
            {                                                                   //if a file is opened, the buffer will be written into that outptr
            fwrite (&buffer, 512, 1, outptr);                                   //if a file is open, and ther are no new magic numbers, the file continues to write
            }
        }

    fclose(inptr); 
    fclose(outptr);
    
}

