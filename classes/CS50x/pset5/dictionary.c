/**
 * dictionary.c
 *
 * Computer Science 50
 * Problem Set 5
 *
 * Implements a dictionary's functionality.
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <cs50.h>
#include <stdbool.h>
#include <ctype.h>
#include "dictionary.h"

int wordsLoaded = 0;  
node* hashtable[SIZE];

unsigned int hash_index(const char* key)                                        //taken from Reddit. See https://m.reddit.com/r/cs50/comments/24b3sj/hash_function_httpsstudycs50nethashtables_question/
{
     // initialize index to 0
     int index = 0;   

     // sum ascii values
     for (int i = 0; i < strlen(key); i++)
     {
         index += toupper(key[i]);
     }
     return index % SIZE;
 }
 

/**
 * Returns true if word is in dictionary else false.
 */
bool check(const char* word)
{
    unsigned int location = hash_index(word);                                   //find out where to look in our buckets
    node *crawler = malloc(sizeof(node));                                       //if I do not malloc crawler and wordcopy, I receive errors in check50--most basic words and substrings
    crawler = hashtable[location];                                              //go to the right bucket
    
    char* wordcopy = malloc(sizeof(LENGTH+1));                                  //dictionary is lowercase only, can't change constants like word, so copy it over--tolower only changes upper
    for (int i = 0; i < strlen(word); i++)
        {
        wordcopy[i] = tolower(word[i]);
        }
    
    while (crawler != NULL)                                                     //until we reach the end of a list
        {
        int match = strcmp (wordcopy, crawler->word);                           //compare the passed word with the dictionary's word
        if (match == 0)                                                         //0 means the two are the same
            return true;
        if (match != 0)                                                         //not 0 means we need to keep looking or the word is not listed
            {
            crawler = crawler->next;
            }
        }

return false;
}

/**
 * Loads dictionary into memory.  Returns true if successful else false.
 */
bool load(const char* dictionary)
{
    char word[LENGTH + 1];
    for (int i = 0; i < SIZE; i++)
        hashtable[i] = NULL;
    
    FILE* file = fopen(dictionary, "r");
    
        if (file == NULL)                                                       //check for dictionary                                                  
            {
            return false;
            }
  
        while (fscanf(file, "%s\n", word) != EOF)                               //don't use while (!feof(file)) because it will count words twice for dictionary counter                                
        {
        node *new_Node = malloc(sizeof(node));                                  //prepare our space
        
            if (new_Node == NULL)                                               //check for space loss
                {
                printf("You are out of memory!\n");                             
                return false;
                }
        
        //fgets(word,LENGTH,file);                                              //no need for this since we scanned already                                              
        //fscanf(file,"%s",new_Node -> word);                                   
        strcpy(new_Node->word, word);
        wordsLoaded++;
        unsigned int index = hash_index(word);                                  //hash function passing
        
        
        if (hashtable[index] == NULL)                                           //hased value back to hashtable, which if hasthable's spot is empty, that object can be placed there
            {
            hashtable[index] = new_Node;
            new_Node->next = NULL;
            }
        else
            {
            new_Node->next = hashtable[index];                                  //if hashtable's value is not empty, there is already an object there. So we have to add another node
            hashtable[index] = new_Node;
            }
        }
fclose(file);
return true;
}



/**
 * Returns number of words in dictionary if loaded else 0 if not yet loaded.
 */
unsigned int size(void)
{
    return wordsLoaded;
}

/**
 * Unloads dictionary from memory.  Returns true if successful else false.
 */
bool unload(void)
{
    node *freedom;
    
    for (int i = 0; i < SIZE; i++)
        {
        freedom = hashtable[i];
        while (freedom != NULL)
            {
            node *temp = freedom;
            freedom = freedom->next; 
            free(temp);
            }
        free(freedom);
        }
        free(freedom);
return true;
}
