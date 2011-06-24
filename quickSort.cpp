#include <stdio.h>

void quickSort( int[], int, int);
int partition( int[], int, int);


void main()
{
	int a[] = { 7, 12, 1, -2, 0, 15, 4, 11, 9};

	int i;
	printf("\n\nq array is:   0  1   2   3  4   5  6  7   8");
	printf("\n\nF array is:  ");
	for(i = 0; i < 9; ++i)
		printf(" %d ", a[i]);
    printf("\n\n");
	quickSort( a, 0, 8);

	printf("\n\nE array is:  ");
	for(i = 0; i < 9; ++i)
		printf(" %d ", a[i]);

}



void quickSort( int a[], int l, int r)
{
   int j;

   if( l < r )
   {
   	// divide and conquer
        j = partition( a, l, r);
       quickSort( a, l, j-1);
       quickSort( a, j+1, r);
   }
	
}



int partition( int a[], int l, int r) {
   int pivot, i, j, t;
   pivot = a[l];
   printf("\npivot=%d",a[l]);
   i = l; j = r+1;
		
   while( 1)
   {
   	do ++i; while( a[i] <= pivot && i <= r );
   	do --j; while( a[j] > pivot );
   	if( i >= j )
   	{
        printf("\ni=%d,j=%d break",i,j);
        break;
    }

   	t = a[i]; a[i] = a[j]; a[j] = t;
   	printf("\ni=%d,j=%d",i,j);
   	printf("\na array is:  ");
	for(i = 0; i < 9; ++i)
		printf(" %d ", a[i]);
   }
   t = a[l]; a[l] = a[j]; a[j] = t;
   printf("\n\nl=%d,j=%d",l,j);
   printf("\nb array is:  ");
	for(i = 0; i < 9; ++i)
		printf(" %d ", a[i]);
   return j;
}

