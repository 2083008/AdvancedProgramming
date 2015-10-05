#include <ctype.h>
#include <stdio.h>

int iswhitespace(int c);

int main()
{
	int c;
	int wc = 0;
	int valid_word = 1;

	c = getchar();

	while(c != EOF){
		if (iswhitespace(c)) 
		{
			if (valid_word){
				wc++;
			} 
			valid_word = 1;	
		}
		else if (!isalpha(c)){
			valid_word = 0;
		}
		c = getchar();
	}
	
	printf("Word Count --> %d\n", wc);
}

int iswhitespace(int c)
{
	return (c == ' ' || c == '\n' || c == '\t');
}