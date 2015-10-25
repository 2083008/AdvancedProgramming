#include <stdio.h>
#include <stdlib.h>
#include "date.h"
#include "tldlist.h"


typedef struct tldlist{
    int size;
    Date begin;
    Date end;
    tldnode *root;
}TLDList;

typedef struct tldnode{
    int data;
    //int height;
    tldnode *leftchild, *rightchild //require struct here?
}TLDNode;

typedef struct tlditerator{

}TLDIterator;

TLDList *tldlist_create(Date *begin, Date *end)
{
    new_tldlist = malloc(sizeof(tldlist));

    new_root = malloc(sizeof(tldnode));

    new_root->date = NULL;
    new_root->leftchild = NULL;
    new_root->rightchild = NULL;

    new_tldlist->root = new_root;

    return new_tldlist;
}

void tldlist_destroy(TLDList *tld) 
{
	if (tld->root != 0)
	{
		destroy_tree(tld->leftchild);
		destroy_tree(tld->rightchild);
		free ( tld );
	}

}

int tldlist_add(TLDList *tld, char *hostname, Date *d)
{
	new_element = malloc(sizeof(tldnode)); // allocate memory for the struct

	if (new_element == NULL){
		return 0; // return failed mallloc
	}
	if (tld->root == NULL){
		tld->root = new_element;
	} 

	if(d)
}