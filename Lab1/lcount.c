#include <stdio.h>
#include <string.h>



int readline(char line[], int max); 
int writeline(const char line[]);

int main() 
{
	int count = 0;

	char line[10];
	while (readline(line,10) != 0)
	{
		count = count+1;
	}

	printf("Number of lines --> %d\n", count);
	
}

/* readline: read a line from standard input, return its length or 0 */
int readline(char line[], int max)
{
   	if (fgets(line, max, stdin) == NULL)
        return 0;
	else
		return strlen(line); 
}

/* writeline: write line to standard output, return number of chars written */
int writeline(const char line[])
{
      fputs(line, stdout);
      return strlen(line);
}