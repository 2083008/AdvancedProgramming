#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "date.h"
#include "tldlist.h"

struct tldnode{
    char *hostname;
    int height;
    long count;
    TLDNode *left_child;
    TLDNode *right_child;
    TLDNode *parent;
};

struct tldlist{
    long count;
    Date *begin;
    Date *end;
    TLDNode *root;
};

struct tlditerator{
    TLDList *tree;
    TLDNode *last_visited;
};


TLDNode *create_node(char*hostname);
char *hostname_strip(char *hostname);
TLDNode *get_leftmost_node(TLDNode *node);
void node_destroy(TLDNode *node);
int insert(TLDNode *current_node, TLDNode *insert_node);

//return the height of the node
int height(TLDNode *node)
{
    if (node == NULL)
        return 0;
    return node->height;
}

/*
 * tldlist_create generates a list structure for storing counts against
 * top level domains (TLDs)
 *
 * creates a TLDList that is constrained to the `begin' and `end' Date's
 * returns a pointer to the list if successful, NULL if not
 */
TLDList *tldlist_create(Date *begin, Date *end)
{
    TLDList *new_tldlist = malloc(sizeof (TLDList));
    if (new_tldlist == NULL)
		return NULL;

	printf("TLDList Create!\n");

    new_tldlist->root = NULL;
    new_tldlist->begin = begin;
    new_tldlist->end = end;
    new_tldlist->count = 0;

    return new_tldlist; // returns NULL if malloc failed
}

/*
 * tldlist_destroy destroys the list structure in `tld'
 *
 * all heap allocated storage associated with the list is returned to the heap
 */
void tldlist_destroy(TLDList *tld)
{
	node_destroy(tld->root);
}

void node_destroy(TLDNode *node)
{
	if (node == NULL)
		return;
	node_destroy(node->left_child);
	node_destroy(node->right_child);
	free(node);
}

TLDNode *create_node(char *hostname)
{
	printf("Create node!\n");
	TLDNode *new_element;
	char* stripped_hostname = (char *)malloc(sizeof(char));
	
	new_element = malloc(sizeof (TLDNode)); // allocate memory for the struct
	if (new_element == NULL)
		return 0; // return failed mallloc

	new_element->left_child = NULL;
	new_element->right_child = NULL;
	new_element->parent = NULL;
	new_element->count = 1;
	stripped_hostname = hostname_strip(hostname);
	new_element->hostname = stripped_hostname;

	return new_element;
}

int tldlist_add(TLDList *tld, char *hostname, Date *d)
{
	// if d < begin || d > end    return 0;
	if (date_compare(d,tld->begin) == -1 || date_compare(d, tld->end) == 1) //check in date bounds
	{
		return 0; //if not in date bounds return
	}

	TLDNode *new_element = create_node(hostname);
	printf("This is the hostname %s\n", new_element->hostname);
	if (tld->root == NULL)										// empty tree so insert at root
	{					
		tld->root = new_element;
		tld->count++;
		return 1;
	}
	else if (insert(tld->root, new_element)){

		tld->count++;
		return 1;
	}
	return 0; 
}

int insert(TLDNode *root, TLDNode *insert_node) //case statements??
{
	TLDNode* cursor = root;
	TLDNode* prev = NULL;
	int goingLeft = 0;

	while (cursor != NULL)
	{
		prev = cursor;
		if(strcmp(cursor->hostname, insert_node->hostname) == 0)
		{
			cursor->count++; // TODO free node here
			printf("count of this tld is %ld\n", cursor->count);
			free(insert_node);
			return 1;
		}
		else if(strcmp(insert_node->hostname, cursor->hostname) < 0)
		{
			cursor = cursor->left_child; // go left
			goingLeft = 1;
		}
		else if(strcmp(insert_node->hostname, cursor->hostname) > 0) 
		{
			cursor = cursor->right_child;
			goingLeft = 0;
		}
	}

	if (goingLeft)
	{
		prev->left_child = insert_node;
		return 1;
	}
	else
	{
		prev->right_child = insert_node;
		return 1;
	}

} // return 0?
	


/*
 * tldlist_count returns the number of successful tldlist_add() calls since
 * the creation of the TLDList
 */
long tldlist_count(TLDList *tld)
{
	printf("tldlist Count %ld\n",tld->count);
	return tld->count;
}

/*
 * tldlist_iter_create creates an iterator over the TLDList; returns a pointer
 * to the iterator if successful, NULL if not
 */
TLDIterator *tldlist_iter_create(TLDList *tld)
{
	TLDIterator *new_iter;
	printf("New Iterator\n");
	new_iter = malloc(sizeof(TLDIterator));
	new_iter->last_visited = NULL;
	new_iter->tree = tld;
	return new_iter;
}

/*
 * tldlist_iter_destroy destroys the iterator specified by `iter'
 */
void tldlist_iter_destroy(TLDIterator *iter)
{
	free(iter);
}

/*
 * tldnode_tldname returns the tld associated with the TLDNode
 */
char *tldnode_tldname(TLDNode *node)
{
	printf("in here oo\n");
	return node->hostname;
}

/*
 * tldnode_count returns the number of times that a log entry for the
 * corresponding tld was added to the list
 */
long tldnode_count(TLDNode *node)
{
	return node->count;
}

/*
 * tldlist_iter_next returns the next element in the list; returns a pointer
 * to the TLDNode if successful, NULL if no more elements to return
 */
TLDNode *tldlist_iter_next(TLDIterator *iter){
	printf("Iter next \n");

	if (iter->last_visited == NULL)
	{
		iter->last_visited = get_leftmost_node(iter->tree->root);
		printf("Last visited\n");
		return iter->last_visited;
	}
	else if (iter->last_visited->right_child != NULL)
	{
		iter->last_visited = get_leftmost_node(iter->last_visited->right_child);
		return iter->last_visited;
	}
	else // else we need to go up the tree
	{
		TLDNode *parent;
		parent = iter->last_visited->parent;
		while (iter->last_visited != NULL && parent->right_child == iter->last_visited )
		{
			iter->last_visited = parent;
			parent = parent->parent;
		}
		return parent;
	}

}

TLDNode *get_leftmost_node(TLDNode *node)
{
	while (node->left_child != NULL)
	{
		node = node->left_child;
	}
	return node;
}

char *hostname_strip(char *hostname) 
{
	char* stripped_string = (char*)malloc(sizeof(char));
	int temp = 0;
	int i;
	int length = strlen(hostname);
	i = strlen(hostname);

	for( i; hostname[i-1] != '.'; i-- ) // find the index of the last '.'
	{
		temp++;
	}
	strncpy(stripped_string, hostname+(length-temp),temp);
    i = 0; // string to lower
    for( i; stripped_string[i]; i++)
    {
		stripped_string[i] = tolower(stripped_string[i]);
	}

	return stripped_string;
}


//left left case   two nodes on the left of first unbalanced node


