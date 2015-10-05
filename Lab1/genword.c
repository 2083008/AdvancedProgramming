#include <ctype.h>
#include <stdio.h>

int iswhitespace(int c);

int main()
{
	int c;
	int index = 0;
	int valid_word = 1;
	char word[12];

	c = getchar();
	while(c != EOF){

		if (iswhitespace(c)) 
		{
			if (valid_word) {
				word[index + 1] = 0; // nulls the string
				printf("%s\n", word);
			} 
			valid_word = 1;	
			index = 0;
			word[0] = 0;
		}
		else if (!isalpha(c)){
			valid_word = 0;
		}
		word[index] = c;
		index++;
		c = getchar();
	}
}

int iswhitespace(int c)
{
	return (c == ' ' || c == '\n' || c == '\t');
}