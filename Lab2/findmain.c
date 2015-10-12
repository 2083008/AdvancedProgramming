#include <stdio.h>
#include <string.h>

#define MAXLINE 1000
/* maximum input line length */

//char pattern[] = "ould";
/* pattern to search for */
/* print all lines from standard input that match pattern */
int main(int argc, char *argv[])
{
	int optional_arg = 0;
	char pattern[20];
	
	if (argc < 2 || argc > 4) {
		printf("Usage: find [-x] pattern [pattern]\n");
		return 1;
	}
	if (strcmp(argv[1],"-x") == 0 && argc == 3){
		optional_arg = 1;
		strcpy(pattern, argv[2]);
		printf("optional arg set\n");
	} else if (argc == 2){
		printf("optional not set\n");
		strcpy(pattern, argv[1]);
	} else {
		printf("refer to usage!\n");
		return 1;
	}

	//printf("%s\n",pattern);
	char line[MAXLINE];
	
	int found = 0;
	while (fgets(line, MAXLINE, stdin) != NULL)
		if (strstr(line, pattern) != NULL) {
			if (!optional_arg){
				printf("%s", line);
			} 
			found++;
		} else if (optional_arg) {
			printf("%s", line);
		}
	printf("%i\n",found);
	return found;
}
