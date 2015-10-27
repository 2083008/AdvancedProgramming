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
TLDNode *get_leftmost_node(TLDNode *node);

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
/*
	if (tld->root != 0)
	{
		destroy_tree(tld->leftchild);
		destroy_tree(tld->rightchild);
		free ( tld );
	}
*/
}

TLDNode *create_node(char *hostname)
{
	TLDNode *new_element;
	new_element = malloc(sizeof (TLDNode)); // allocate memory for the struct
	if (new_element == NULL)
		return 0; // return failed mallloc
	new_element->left_child = NULL;
	new_element->right_child = NULL;
	new_element->parent = NULL;

	//hostname = hostname_strip(hostname);
	new_element->hostname = hostname;
	return new_element;
}

char* hostname_strip(char *hostname) //TODO
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
	//printf("%s\n",stripped_string);
	return stripped_string;
}

int tldlist_add(TLDList *tld, char *hostname, Date *d)
{
	if (!(date_compare(d,tld->begin) && date_compare(tld->end,d))) //check in date bounds
	{
		return 0; //if not in date bounds return
	}

	TLDNode *new_element = create_node(hostname);

	if (tld->root == NULL)
	{ // empty tree so insert at root
		tld->root = new_element;
		tld->count++;
		return 1;
	}
	if(insert(tld->root, new_element)) // if successfully inserted
	{
		tld->count++;
		return 1;	
	}
	return 0;
}

int insert(TLDNode *current_node, TLDNode *insert_node) //case statements??
{
	if(strcmp(current_node->hostname, insert_node->hostname) == 0) // current_node == insert_node
	{
		current_node->count++;
		free(insert_node); // this node represented in count so free it
		return 1;
	}
	if(strcmp(current_node->hostname, insert_node->hostname) == -1) // insert_node < current node
	{
		if (current_node->left_child == NULL) // found insertion point
		{
			current_node->left_child = insert_node;
			insert_node->count++;
			return 1;
		}
		insert(current_node->left_child,insert_node); //recurse left
	}
	if(strcmp(current_node->hostname, insert_node->hostname) == 1)
	{
		if (current_node->right_child == NULL)
		{
			current_node->right_child = insert_node;
			insert_node->count++;
			return 1;
		}
		insert(current_node->right_child,insert_node);
	}
	return 0;
}

/*
 * tldlist_count returns the number of successful tldlist_add() calls since
 * the creation of the TLDList
 */
long tldlist_count(TLDList *tld)
{
	return tld->count;
}

/*
 * tldlist_iter_create creates an iterator over the TLDList; returns a pointer
 * to the iterator if successful, NULL if not
 */
TLDIterator *tldlist_iter_create(TLDList *tld)
{
	TLDIterator *new_iter;
	new_iter = malloc(sizeof(TLDIterator));
	new_iter->last_visited = NULL;
	new_iter->tree = tld;
	return new_iter;
}


/*
 * tldnode_tldname returns the tld associated with the TLDNode
 */
char *tldnode_tldname(TLDNode *node);

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

    if (iter->last_visited == NULL) {  // go left if l
        iter->last_visited = get_leftmost_node(iter->tree->root);
        return iter->last_visited;
    }

    if (iter->last_visited->right_child != NULL) {      // if you have a right subtree, return furthest left child
        iter->last_visited = get_leftmost_node(iter->last_visited->right_child);
        return iter->last_visited;
    }

    while (iter->last_visited->parent != NULL) 
    {      // if parent is to the right, it's the sucessor.
        if (iter->last_visited->parent->left_child == iter->last_visited)
	{
            iter->last_visited = iter->last_visited->parent;
            return iter->last_visited;
    	}
        iter->last_visited = iter->last_visited->parent;
    }

  return NULL;    // else, return null

}

TLDNode *get_leftmost_node(TLDNode *node)
{
	while (node->left_child != NULL)
	{
		node = node->left_child;
	}
	return node;
}


//left left case   two nodes on the left of first unbalanced node


