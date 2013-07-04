// Пламен Василев Александров, сп. Компютърни науки, 2 курс, ф.н. 80050
// compiled on Visual Studio .NET 2003

#include <stdio.h>

// used for n <= 5
int median(int* a, int n)
{
	if (n == 1) return a[0];
	int i, j, tmp;
	for (i = n; i > (n-1)/2; i--)
	{
		for (j = 0; j+1 < i; j++)
			if (a[j] > a[j+1]) 
			{
				tmp = a[j]; a[j] = a[j+1]; a[j+1] = tmp;
			}
	}
	if (n%2 == 0) return (a[n/2] > a[n/2-1]) ? a[n/2] : a[n/2-1];
	return a[n/2];
}

int selection(int* a, int N, int i, int* R1, int* R3)
{
	if (i < 0 || i >= N) return -1;
	int medCount = (N%5 == 0) ? N/5 : N/5 + 1;
	int* medians = new int[medCount];
	int j;
	for (j = 0; j < N/5; j++)
		medians[j] = median(a+5*j, 5);
	if (medCount > N/5) medians[medCount-1] = median(a+N-N%5, N%5);

	int x;
	if (medCount > 5)
		x = selection(medians, medCount, medCount/2, R1, R3);
	else 
		x = median(medians, medCount);
	delete [] medians;

	int n1 = 0, n2 = 0, n3 = 0;
	for (j = 0; j < N; j++)
	{
		if (a[j] < x) {R1[n1] = a[j]; n1++;}
		else if (a[j] == x) {n2++;}
		else {R3[n3] = a[j]; n3++;}
	}

	if (n1 > i) return selection(R1, n1, i, R1, R3);
	if (n1 + n2 >= i) return x;
	else return selection(R3, n3, i-n1-n2, R1, R3);
}

void merge(int* A, int N, int* B, int M, int* TMP)
{
	int i, j, k = 0;
	for (i = 0; i < N; i++) TMP[i] = A[i];

	for (i = 0, j = 0; i < N || j < M;)
	{
		if (i == N) A[k++] = B[j++];
		else if (j == M) A[k++] = TMP[i++];
		else if (TMP[i] >= B[j]) A[k++] = B[j++];
		else A[k++] = TMP[i++];
	}
}

void mergeSort(int* A, int N)
{
	int s = 1, i;
	int* tmp = new int[N];
	while (s < N)
	{
		for (i = s; i + s <= N; i += 2*s)
			merge(A + (i-s), s, A + i, s, tmp);

		if (i <= N)
			merge(A + (i-s), s, A + i, N - i, tmp);
		s = 2*s;
	}
	delete [] tmp;
}

int OrderStatistic(int* a, int N, int i)
{
	if (N < 80) 
	{
		mergeSort(a, N);
		return a[i];
	}
	else 
	{
		int* R1 = new int[N];
		int* R3 = new int[N];
		int x = selection(a, N, i, R1, R3);
		delete [] R1;
		delete [] R3;
		return x;
	}
}

//void main()
//{
//	int N = 11;
//	int A[] = {9, 6, 8, 2, 1, 7, 5, 6, 3, 9, 4};
//	printf("A: \n");
//	int i;
//	for (i = 0; i < N; i++) printf("%d, ", A[i]);
//	printf("\n");
//	printf("median: %d\n", median(A, 5));
//	
//	int x = 5;
//	printf("%d-th element: %d", x, OrderStatistic(A, N, x));
//}