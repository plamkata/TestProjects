//#include <stdio.h>
//
//const int MAXN = 100;
//unsigned data[MAXN][MAXN];
//
//void init() 
//{
//	int i, j;
//	for (i = 0; i < MAXN; i++)
//		for (j = 0; j < MAXN; j++)
//		{
//			if (j == 0 || i == j) data[i][j] = 1;
//			else data[i][j] = 0;
//		}
//	data[0][0] = 1;
//}
//
//unsigned binom(int n, int m)
//{
//	int i, j;
//	for (i = 1; i < n+1; i++)
//	{
//		for (j = 1; j < m+1 && j < i; j++)
//		{
//			if (data[i][j] == 0)
//				data[i][j] = data[i-1][j] + data[i-1][j-1];
//		}
//	}
//	if (m > n/2)
//		return data[n][n-m];
//	else
//		return data[n][m];
//}

//int binom(int n, int m)
//{
//	if (m == 0 || n == m) return 1;
//	return binom(n-1, m) + binom(n-1, m-1);
//}

//void main()
//{
//	int i = 100, j = 56;
//	init();
//	printf("(%d, %d) = %u", i, j, binom(i, j));
//}