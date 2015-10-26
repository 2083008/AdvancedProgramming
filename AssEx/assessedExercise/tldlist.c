#include <stdio.h>
#include <stdlib.h>
//#include "date.h"
#include "tldlist.h"


typedef struct tldlist{
    long size;
    Date begin;
    Date end;
    TLDNode *root;
}TLDList;

typedef struct tldnode{
    int data;
    char *hostname;
	TLDNode* leftchild
	TLDNode* rightchild
}TLDNode;

typedef struct tlditerator{

}TLDIterator;

int tldlist_insert(TLDList *tld, TLDNode *currentNode);

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
	if (date_compare(tld->begin,d) || (date_compare(d,tld->end))) // discard dates outside of begin and end
	{
		return 0;
	}

	new_element = malloc(sizeof(tldnode)); // allocate memory for the struct
	if (new_element == NULL){
		return 0; // return failed mallloc
	}
	new_element->hostname = hostname;
	new_element->date = d;

	
	if (tld->root == NULL){
		tld->root = new_element;
		return 1;
	} 

	if(tldlist_insert(tld,tld->root,new_element) 
	{
		tld->size++;
		return 1;
	}
	else
	{
		free (new_element);
		return 0;
	}

}

long tldlist_count(TLDList *tld)
{
	return tld->size;
}

TLDIterator *tldlist_iter_create(TLDList *tld)
{
	return NULL;
}

TLDNode *tldlist_iter_next(TLDIterator *iter)
{

}

void tldlist_iter_destroy(TLDIterator *iter)
{

}

char *tldnode_tldname(TLDNode *node)
{
	return node->hostname;
}

long tldnode_count(TLDNode *node)
{

}


int tldlist_insert(TLDList *tld, TLDNode *currentNode, TLDNode *insertNode)
{
	if (insertNode->data < insertNode->data)
	{
		if(currentNode->leftchild == NULL)
		{
			currentNode->leftchild = insertNode;
			return 1;
		}
		else 
		{
			tldlist_insert(tld,currentNode->leftchild,insertNode);
		}
		
	}
	if (insertNode->data > insertNode->data)
	{
		if(currentNode->rightchild == NULL)
		{
			currentNode->rightchild = insertNode;
			return 1;
		}
		else
		{
			tldlist_insert(tld,currentNode->rightchild,insertNode);
			
		}
	}
	return 0;
}