//#include <stdio.h>
//
//int N;
//float* p;
//float** data;
//bool* visited;
//
//void spread() 
//{
//	int i, j;
//	for (i = 0, j = 1; i < N - 1; i++, j++)
//		if (data[i][j-1] + data[i+1][j] > p[i])
//			data[i][j] = p[i];
//		else 
//			data[i][j] = data[i][j-1] + data[i+1][j];
//
//	int k, t;
//	float min;
//	for (k = 2; k < N; k++)
//		for (i = 0, j = k; i < N - k; i++, j++)
//		{
//			min = data[i][i] + data[i+1][j];
//			for (t = 1; t < j - i; t++)
//				if (data[i][i+t] + data[i+t+1][j] < min)
//					min = data[i][i+t] + data[i+t+1][j];
//			data[i][j] = min;
//		}
//}
//
//bool equal(float a, float b)
//{
//	return a-b < 0.0001 && a-b > -0.0001;
//}
//
//void find(int i, int j) 
//{
//	if(i == j) 
//	{
//		if (!visited[i]) printf("%d \n", i); 
//		visited[i] = 1; return;
//	}
//	if (i == j-1) 
//		if (data[i][j] == p[i]) 
//		{
//			if (!visited[i]) printf("%d %d \n", i, j); 
//			visited[i] = 1; visited[j] = 1; 
//			return;
//		}
//		else return;
//	int k;
//	for (k = 0; k < j-i; k++)
//		if (equal(data[i][k] + data[i+k+1][j], data[i][j])) 
//		{
//			find(i, k);
//			find(i+k+1, j);
//		}
//
//}
//
//void main(void) 
//{
//	scanf("%d", &N);
//	p = new float[N-1];
//	data = new float*[N];
//	visited = new bool[N];
//	
//	int i;
//	for (i = 0; i < N; i++)
//		visited[i] = 0;
//	for (i = 0; i < N; i++)
//	{
//		data[i] = new float[N];
//		scanf("%f", &data[i][i]);
//	}
//	for (i = 0; i < N-1; i++) 
//		scanf("%f", &p[i]);
//	
//	spread();
//	printf("The optimum time is: %f", data[0][N-1]);
//	printf("One possible solution is: \n");
//	find(0, N-1);
//}