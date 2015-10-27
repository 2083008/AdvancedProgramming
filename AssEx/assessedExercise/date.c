#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "date.h"

struct date {
    int day;
    int month;
    int year;
};

/*
 * date_create creates a Date structure from `datestr`
 * `datestr' is expected to be of the form "dd/mm/yyyy"
 * returns pointer to Date structure if successful,
 *         NULL if not (syntax error)
 */
Date *date_create(char *datestr){
    int day, month, year;

    Date *new_date = malloc (sizeof (Date));

    if (new_date == NULL) return NULL;
    sscanf(datestr, "%d/%d/%d", &day, &month, &year);

    new_date->day = day;
    new_date->month = month;
    new_date->year = year;

    return new_date;
}

/*
 * date_duplicate creates a duplicate of `d'
 * returns pointer to new Date structure if successful,
 *         NULL if not (memory allocation failure)
 */
Date *date_duplicate(Date *d){

    Date *duplicate_date = malloc(sizeof (Date));

    if (duplicate_date == NULL) return NULL;

    duplicate_date->day = d->day;
    duplicate_date->month = d->month;
    duplicate_date->year = d->year;

    return duplicate_date;
}

/*
 * date_compare compares two dates, returning <0, 0, >0 if
 * date1<date2, date1==date2, date1>date2, respectively
 */
int date_compare(Date *date1, Date *date2){

    if (date1->year > date2->year) return 1;
    else if (date1->year < date2->year) return -1;

    if (date1->month > date2->month)
        return 1;
    else if (date1->month < date2->month)
        return -1;
    else if (date1->day > date2->day)
        return 1;
    else if (date1->day < date2->day)
        return -1;
    return 0;

}

/*
 * date_destroy returns any storage associated with `d' to the system
 */
void date_destroy(Date *d){
    free(d);
}
