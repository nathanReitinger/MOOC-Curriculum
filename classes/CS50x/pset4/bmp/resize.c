/**
 * copy.c
 *
 * Computer Science 50
 * Problem Set 4
 *
 * Copies a BMP piece by piece, just because.
 */
       
#include <stdio.h>
#include <stdlib.h>

#include "bmp.h"

int main(int argc, char* argv[])
{

    // ensure proper usage
    if (argc != 4)
    {
        printf("Usage: ./copy n infile outfile\n");
        return 1;
    }
    
    int n = atoi(argv[1]);
    if (n < 0)
    {
        printf("Usage: ./copy n infile outfile\n");
        return 1;
    }
    
    if (n > 101)
    {
        printf("Usage: ./copy n [1-100] infile outfile\n");
        return 1;
    }

    // remember filenames
    char* infile = argv[2];
    char* outfile = argv[3];

    // open input file 
    FILE* inptr = fopen(infile, "r");
    if (inptr == NULL)
    {
        printf("Could not open %s.\n", infile);
        return 2;
    }

    // open output file
    FILE* outptr = fopen(outfile, "w");
    if (outptr == NULL)
    {
        fclose(inptr);
        fprintf(stderr, "Could not create %s.\n", outfile);
        return 3;
    }

    // read infile's BITMAPFILEHEADER
    BITMAPFILEHEADER bf;
    fread(&bf, sizeof(BITMAPFILEHEADER), 1, inptr);

    // read infile's BITMAPINFOHEADER
    BITMAPINFOHEADER bi;
    fread(&bi, sizeof(BITMAPINFOHEADER), 1, inptr);

    // ensure infile is (likely) a 24-bit uncompressed BMP 4.0
    if (bf.bfType != 0x4d42 || bf.bfOffBits != 54 || bi.biSize != 40 || 
        bi.biBitCount != 24 || bi.biCompression != 0)
    {
        fclose(outptr);
        fclose(inptr);
        fprintf(stderr, "Unsupported file format.\n");
        return 4;
    }
    
            int originalHeight = bi.biHeight;                                   //assigning my varialbles
            int originalWidth = bi.biWidth;
            //int originalbiSizeImage = bi.biSizeImage;
            //int originalbfSize = bf.bfSize;
            
            bi.biHeight *= n;
            bi.biWidth *= n;

            // determine padding for scanlines (new and old)
            int padding =  (4 - (originalWidth * sizeof(RGBTRIPLE)) % 4) % 4;
            int newPadding =  (4 - (bi.biWidth * sizeof(RGBTRIPLE)) % 4) % 4;
        
            // recalculate headers, bi and bf
            bi.biSizeImage = (((bi.biWidth * abs(bi.biHeight)) * sizeof(RGBTRIPLE)) + (abs(bi.biHeight)) * newPadding);
            bf.bfSize = (bi.biSizeImage) + 54;
    
    // write outfile's BITMAPFILEHEADER
    fwrite(&bf, sizeof(BITMAPFILEHEADER), 1, outptr);

   // write outfile's BITMAPINFOHEADER
    fwrite(&bi, sizeof(BITMAPINFOHEADER), 1, outptr);

    // iterate over infile's scanlines
    for (int i = 0, biHeight = abs(originalHeight); i < biHeight; i++)
    {
        for (int y = 0; y < n; y++)                                             // use to loop vertically
        {
            // iterate over pixels in scanline
            for (int j = 0; j < originalWidth; j++)
            {
            // temporary storage
            RGBTRIPLE triple;
    
            // read RGB triple from infile
            fread(&triple, sizeof(RGBTRIPLE), 1, inptr);
                    
            for (int q = 0; q < n; q++)                                         //write width n times
                {
                // write RGB triple to outfile
                fwrite(&triple, sizeof(RGBTRIPLE), 1, outptr);
                }
            }
        
        // loop over height                                                     // put before padding because we only need to repeat row n times
        if (n != 1 && y < n-1)
            {
            fseek(inptr, -(originalWidth * sizeof(RGBTRIPLE) + padding), SEEK_CUR);
            } 
        
        // skip over padding, if any
        fseek(inptr, padding, SEEK_CUR);
        
        // then add it back (to demonstrate how)
        for (int k = 0; k < newPadding; k++)
            {
            fputc(0x00, outptr);
            }
        }
    }

    // close infile
    fclose(inptr);

    // close outfile
    fclose(outptr);

    // that's all folks
    return 0;
}
