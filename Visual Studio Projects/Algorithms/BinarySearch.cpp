#include <stdio.h>

int binarySearchLeft(int* A, int N, int x)
{
	if (A[0] > x || A[N-1] < x) return -1;
	int left = 0, right = N, i = N/2, result = -1;
	while (left < right) 
	{
		if (A[i] == x && (i == 0 || x > A[i-1]))
		{result = i; break;}
		else if (i == left) break;
		else if (A[i] >= x) right = i;
		else if (A[i] < x) left = i;
		i = (left + right)/2;
	}
	return result;
}

int binarySearchRight(int* a, int n, int x)
{
	if (a[0] > x || a[n-1] < x) return -1;
	int left = 0, right = n, i = right/2, result = -1;
	while (left < right) 
	{
		if (a[i] == x && (i == n-1 || x < a[i+1]))
		{result = i; break;}
		else if (i == left) break;
		else if (a[i] > x) right = i;
		else if (a[i] <= x) left = i;
		i = (right + left)/2;
	}
	return result;
}

//int i, res = 0;
//
//int binarySearchRight(int* A, int N, int x)
//{
//	i = N/2;
//	if (A[i] < x && i+1 == N) return -1;
//	if (A[i] == x && (i+1 == N || x < A[i+1])) return res + i;
//	if (i == 0) return -1;
//	if (A[i] > x) return binarySearch(A, i, x);
//	if (A[i] < x || A[i+1] == x) {
//		res += i;
//		return binarySearch(A + i, N - i, x);
//	}
//}

int binarySearch(int* A, int N, int x) 
{
	int left = 0, right = N, i = N/2, index = -1;
	while (left < right)
	{
		if (A[i] == x) {index = i; break;}
		else if (i == left || i == right-1) break;
		else if (A[i] > x) right = i;
		else if (A[i] < x) left = i;
		i = (right + left)/2;
	}
	return index;
}

//int res = 0; 
//// works fine
//int binarySearch(int* a, int n, int x)
//{
//	if (a[n/2] == x) return res + n/2;
//	if (n == 1) return -1;
//	if (a[n/2] > x) return binarySearch(a, n/2, x);
//	if (a[n/2] < x) {
//		res += n-n/2; 
//		return binarySearch(a+n/2, n-n/2, x);
//	}
//}

//void main()
//{
//	int N = 10;
//	int A[] = {1, 2, 2, 5, 6, 6, 7, 7, 8, 9};
//	printf("A: ");
//	int i, x=2;
//	for (i = 0; i < N; i++) printf("%d, ", A[i]);
//	printf("\n");
//	printf("binarySearchLeft %d: %d", x, binarySearchLeft(A, N, x)); 
//	printf("\n");
//	printf("binarySearchRight %d: %d", x, binarySearchRight(A, N, x)); 
//
//}