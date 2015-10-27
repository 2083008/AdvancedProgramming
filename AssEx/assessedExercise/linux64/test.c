#include <stdio.h>
#include <string.h>
#include <stdlib.h>

char* hostname_strip(char *hostname);
int main(void){

	printf("%s\n", hostname_strip("glasgowasdfasdfasdfasdf.a.us"));
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
	printf("%s\n",stripped_string);
	return stripped_string;
}