//#include <stdio.h>
//
//int N;
//float* data;
//int* indices;
//int** counts;
//
//void findIncr()
//{
//	int i, j, max, maxInd;
//	for (i = N-2; i >=0; i--)
//	{
//		max = 0; maxInd = -1;
//		for (j = i+1; j < N; j++)
//		{
//			if (data[i] <= data[j] && counts[0][j] > max)
//			{
//				max = counts[0][j]; maxInd = j;
//			}
//		}
//		counts[0][i] = max+1;
//		counts[1][i] = maxInd;
//	}
//
//	// find the first max number among counts
//	max = 0; maxInd = -1;
//	for (i = 0; i < N; i++)
//		if (counts[0][i] > max)
//		{
//			max = counts[0][i];
//			maxInd = i;
//		}
//
//	while (maxInd != -1)
//	{
//		indices[maxInd] = 1;
//		maxInd = counts[1][maxInd];
//	}
//}
//
//void findDecr()
//{
//	int i, j, max, maxInd;
//	for (i = N-2; i >=0; i--)
//	{
//		max = 0; maxInd = -1;
//		for (j = i+1; j < N; j++)
//		{
//			if (data[i] >= data[j] && counts[0][j] >= max)
//			{
//				max = counts[0][j]; maxInd = j;
//			}
//		}
//		counts[0][i] = max+1;
//		counts[1][i] = maxInd;
//	}
//
//	// find the last max number among counts
//	max = 0; maxInd = -1;
//	for (i = 0; i < N; i++)
//		if (counts[0][i] >= max)
//		{
//			max = counts[0][i];
//			maxInd = i;
//		}
//
//	while (maxInd != -1)
//	{
//		if (indices[maxInd] == 1)
//			indices[maxInd] = 2;
//		else 
//			indices[maxInd] = -1;
//		maxInd = counts[1][maxInd];
//	}
//}
//
//
//void findDecrIndices()
//{
//	int i, j, max, maxInd;
//	for (i = N-1; i >= 0; i--)
//	{
//		max = 0; maxInd = -1;
//		for (j = i+1; j < N; j++)
//		{
//			if (counts[0][j] >= max && 
//				(indices[i] == 2 || indices[j] == 2 || indices[i] >= indices[j]))
//			{
//				max = counts[0][j]; maxInd = j;
//			}
//		}
//		counts[0][i] = max+1;
//		counts[1][i] = maxInd;
//	}
//
//	// find the first max number among counts
//	max = 0; maxInd = -1;
//	for (i = 0; i < N; i++) 
//	{
//		if (counts[0][i] >= max)
//		{
//			max = counts[0][i];
//			maxInd = i;
//		}
//	}
//
//	i = 0;
//	while (maxInd != -1)
//	{
//		printf("%d ", maxInd); 
//		maxInd = counts[1][maxInd];
//	}
//	//i = 0;
//	//while (maxInd != -1)
//	//{
//	//	while (i < maxInd && i < N)
//	//	{
//	//		printf("%d ", i);
//	//		i++;
//	//	}
//	//	i = maxInd + 1;
//	//	maxInd = counts[1][maxInd];
//	//}
//	//while (i < N)
//	//{
//	//	printf("%d ", i);
//	//	i++;
//	//}
//}
//
//void main(void) 
//{
//	scanf("%d", &N);
//	data = new float[N];
//	int i;
//	for (i = 0; i < N; i++)
//	{
//		scanf("%f", &data[i]);
//	}
//
//	indices = new int[N];
//	for (i = 0; i < N; i++)
//		indices[i] = 0;
//
//	counts = new int*[N];
//	counts[0] = new int[N];
//	counts[1] = new int[N];
//
//	counts[0][N-1] = 1;// count
//	counts[1][N-1] = -1;// prev. index
//
//	findIncr();
//	findDecr();
//	findDecrIndices();
//	printf("\n");
//}