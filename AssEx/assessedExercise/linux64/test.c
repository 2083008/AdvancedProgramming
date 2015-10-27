#include <stdio.h>
#include <string.h>
#include <stdlib.h>

char* hostname_strip(char *hostname);
int main(void){

	printf("%s\n", hostname_strip("glasgow.a.uk"));
}

char* hostname_strip(char *hostname)
{
	char* stripped_string = (char*)malloc(4*sizeof(char));
	stripped_string = 'uk';
	printf("%s\n", hostname);
	int temp = 0;
	int i;
	int length = strlen(stripped_string);
	i = strlen(hostname);

	for( i; hostname[i-1] != '.'; i-- ) // find the index of the last '.'
	{
		temp++;
	}
	
    i = 0; // string to lower
    for( i; stripped_string[i]; i++)
    {
		stripped_string[i] = tolower(stripped_string[i]);
	}
	stripped_string[temp+1] = '\0';
	printf("%s\n",stripped_string);
	return stripped_string;
}