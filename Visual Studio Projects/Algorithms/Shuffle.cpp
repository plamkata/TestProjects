#include <stdio.h>
#include <stdlib.h>
#include <time.h>

void swap(int *a, int *b)
{
    int tmp = *a;
    *a = *b;
    *b = tmp;
}

void shuffle(int *a, int n)
{
    for (int i = 0; i < n - 1; ++i) 
    {
        int j = i + rand() % (n - i);
        swap(&a[i], &a[j]);
    }
}

void print(int a[], int n)
{
    for (int i = 0; i < n; i++) 
    {
        printf("%d ", a[i]);
    }
    printf("\n");
}

int main(int argc, int *argv[])
{
    int a[] = {1, 5, 3, 8, 9, 4};
    int size = 6;

    srand(time(NULL));
    for (int i = 0; i < 30; ++i) {
        shuffle(a, size);
        print(a, size);
    }
}