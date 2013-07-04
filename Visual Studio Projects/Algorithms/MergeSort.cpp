// Пламен Василев Александров, сп. Компютърни науки, 2 курс, ф.н. 80050
// compiled on Visual Studio .NET 2003

#include <stdio.h>

// result remains in A; using TMP as a temporary storage
//void merge(int* A, int N, int* B, int M, int* TMP)
//{
//	int i, j, k = 0;
//	for (i = 0; i < N; i++) TMP[i] = A[i];
//
//	for (i = 0, j = 0; i < N || j < M;)
//	{
//		if (i == N) A[k++] = B[j++];
//		else if (j == M) A[k++] = TMP[i++];
//		else if (TMP[i] >= B[j]) A[k++] = B[j++];
//		else A[k++] = TMP[i++];
//	}
//}

//void mergeSort(int* A, int N, int* TMP)
//{
//	if (N == 1 || N == 0) return;
//	mergeSort(A, N/2, TMP);
//	mergeSort(A+N/2, N-N/2, TMP);
//	merge(A, N/2, A+N/2, N-N/2, TMP);
//}
//
//void mergeSort(int* A, int N)
//{
//	int* tmp = new int[N];
//	mergeSort(A, N, tmp);
//	delete [] tmp;
//}

//void mergeSort(int* A, int N)
//{
//	int s = 1, i;
//	int* tmp = new int[N];
//	while (s < N)
//	{
//		for (i = s; i + s <= N; i += 2*s)
//			merge(A + (i-s), s, A + i, s, tmp);
//
//		if (i <= N)
//			merge(A + (i-s), s, A + i, N - i, tmp);
//		s = 2*s;
//	}
//	delete [] tmp;
//}

//void main()
//{
//	int N = 11;
//	int A[] = {8, 6, 1, 2, 9, 7, 5, 6, 3, 9, 4};
//	printf("A: \n");
//	int i;
//	for (i = 0; i < N; i++) printf("%d, ", A[i]);
//	printf("\n");
//	mergeSort(A, N);
//	printf("sorted: \n");
//	for (i = 0; i < N; i++) printf("%d, ", A[i]);
//	printf("\n");
//}