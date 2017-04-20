/**
 * fifteen.c
 *
 * Computer Science 50
 * Problem Set 3
 *
 * Implements Game of Fifteen (generalized to d x d).
 *
 * Usage: fifteen d
 *
 * whereby the board's dimensions are to be d x d,
 * where d must be in [DIM_MIN,DIM_MAX]
 *
 * Note that usleep is obsolete, but it offers more granularity than
 * sleep and is simpler to use than nanosleep; `man usleep` for more.
 */
 
#define _XOPEN_SOURCE 500

#include <cs50.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

// constants
#define DIM_MIN 3
#define DIM_MAX 9

// board
int board[DIM_MAX][DIM_MAX];

// dimensions
int d;

// prototypes
void clear(void);
void greet(void);
void init(void);
void draw(void);
bool move(int tile);
bool won(void);

int main(int argc, string argv[])
{
    // ensure proper usage
    if (argc != 2)
    {
        printf("Usage: fifteen d\n");
        return 1;
    }

    // ensure valid dimensions
    d = atoi(argv[1]);
    if (d < DIM_MIN || d > DIM_MAX)
    {
        printf("Board must be between %i x %i and %i x %i, inclusive.\n",
            DIM_MIN, DIM_MIN, DIM_MAX, DIM_MAX);
        return 2;
    }

    // open log
    FILE* file = fopen("log.txt", "w");
    if (file == NULL)
    {
        return 3;
    }

    // greet user with instructions
    greet();

    // initialize the board
    init();

    // accept moves until game is won
    while (true)
    {
        // clear the screen
        clear();

        // draw the current state of the board
        draw();

        // log the current state of the board (for testing)
        for (int i = 0; i < d; i++)
        {
            for (int j = 0; j < d; j++)
            {
                fprintf(file, "%i", board[i][j]);
                if (j < d - 1)
                {
                    fprintf(file, "|");
                }
            }
            fprintf(file, "\n");
        }
        fflush(file);

        // check for win
        if (won())
        {
            printf("ftw!\n");
            break;
        }

        // prompt for move
        printf("Tile to move: ");
        int tile = GetInt();
        
        // quit if user inputs 0 (for testing)
        if (tile == 0)
        {
            break;
        }

        // log move (for testing)
        fprintf(file, "%i\n", tile);
        fflush(file);

        // move if possible, else report illegality
        if (!move(tile))
        {
            printf("\nIllegal move.\n");
            usleep(500000);
        }

        // sleep thread for animation's sake
        usleep(500000);
    }
    
    // close log
    fclose(file);

    // success
    return 0;
}

/**
 * Clears screen using ANSI escape sequences.
 */
void clear(void)
{
    printf("\033[2J");
    printf("\033[%d;%dH", 0, 0);
}

/**
 * Greets player.
 */
void greet(void)
{
    clear();
    printf("WELCOME TO GAME OF FIFTEEN\n");
    usleep(2000000);
}

/**
 * Initializes the game's board with tiles numbered 1 through d*d - 1
 * (i.e., fills 2D array with values but does not actually print them).  
 */
void init(void)
{
    // TODO
int i, j; 
int count = d*d-1;

for (i = 0; i < d; i++)
    {
    for (j = 0; j < d; j++)
        {
        board[i][j] = count;                                                    //can't iterate with your own numbers, must use board's values
        count--;                                                                //starting at the highest value, tiles must decrease from there
        
        if (((d & 1) == 0) && (board[i][j] == 2))                               //if the tiles are even, flip tile 2
            {
            board[i][j] = board[i][j] - 1;
            }
        else if  (((d & 1) == 0) && (board[i][j] == 1))                         //if the tiles are even, flip tile 1
            {
            board[i][j] = board[i][j] + 1;
            }
        else                                                                    //if the tiles are odd, keep the values the same
            {
            board[i][j] = board[i][j];
            }
        }
    }
}

/**
 * Prints the board in its current state.
 */
void draw(void)
{
    // TODO
int i, j; 

for (i = 0; i < d; i++)
    {
    for (j = 0; j < d; j++)
        {
        if (board[i][j] > 0)
            {
            printf("%4d", board[i][j]);
            }
        if (board[i][j] == 0)
            {
            printf ("   X");
            }
        }
    printf("\n");
    }

}

/**
 * If tile borders empty space, moves tile and returns true, else
 * returns false.                                                               // i up and down ------- j left to right
 */                                                                             //  0
bool move(int tile)                                                             //0 8, 7, 6,     8, 7, 6     8, 7, 6,    8, 7, 6,   1, 2, 3,   
{                                                                               //  5, 4, 3,     5, X, 4     X, 5, 4,    5, 4, 3,   4, 5, 6, 
                                                                                //  2, 1, X      3, 2, 1     3, 2, 1     2, X, 1    7, 8, 0
int i = 0; 
int j = 0;


    for (i = 0; i < d; i++)
        {
        for (j = 0; j < d; j++)
            {
            if (board[i][j] == tile)                                            //if the board's index is the same as the tile picked
                {

                if ((i+1 <= d-1) && (board[i+1][j] == 0))                       //when moving up, as long as the tile is within the board (i+1 <= d-1) and the blank space is above the tile (board[i+1][j]==0)
                    {
                    board[i][j] = 0;
                    board[i+1][j] = tile;
                    return true;
                    }
                else if (i-1 >= 0 && board[i-1][j] == 0)                        //when moving down, as long as the tile is not moved below 0 (i-1) and there is a blank space below the tile (board[i-1][j]==0))
                    {
                    board[i][j] = 0;
                    board[i-1][j] = tile;
                    return true;
                    }
                else if ((j + 1 <= d - 1) && (board[i][j+1] == 0))              //when moving left (descending values), as long as the tile is within the board's numbers (j + 1 <= d - 1) and there is blank space to the right of tile (board[i][j+1] == 0)
                    {
                    board[i][j] = 0; 
                    board[i][j+1] = tile; 
                    return true; 
                    }
                else if (j-1 >= 0 && board[i][j-1] == 0)                        //when moving right (descending values), as long as the tile is not moved beyond 0 (j-1 >= 0) and there is a blank space to the right of the tile (board[i][j-1] == 0)
                    {
                    board[i][j] = 0;
                    board[i][j-1] = tile; 
                    return true;
                    }
                else 
                    {
                    break;
                    }
                }
            }
        }   
return false;
}


                    
/**
 * Returns true if game is won (i.e., board is in winning configuration), 
 * else false.
 */
bool won(void)
{
    // TODO   int end = board[0 + (d*d)][0 + (d*d)];

int i = 0; 
int j = 0;
int count = 1;


    for (i = 0; i < d; i++)
        {
        for (j = 0; j < d; j++)
            {
            if (board[i][j] == count || (count = d*d-1 && board[i][j+1] == 0))  //be specific, use the (board[i][j+1] == 0)) rather than (count == d*d-1) alone
                {
                count++;
                }
            if (count == d*d)
                {
                return true;
                }
            }
        }
return false;
}
