//#include <stdio.h>
//
//const int MAXN = 80;
//int N, M;
//int G[MAXN][MAXN];
//int r;
//
//int end = 0;
//int stack[MAXN];
//
//int k = 0;
//int t[MAXN];
//bool visited[MAXN];
//
//void clear()
//{
//	end = 0;
//}
//
//bool empty()
//{
//	return end == 0;
//}
//
//void push(int x)
//{
//	++end;
//	stack[end] = x;
//}
//
//int pop()
//{
//	int x = stack[end];
//	end--;
//	return x;
//}
//
//int top()
//{
//	return stack[end];
//}
//
//void DFS()
//{
//	int i, x, y;
//	clear();
//	push(r); visited[r] = true;
//cycle: while(!empty())
//	{
//		x = top();
//		for (i = G[x][0]; i >= 1; i--)
//		{
//			y = G[x][i];
//			if (!visited[y]) {
//				push(y); visited[y] = true;
//			}
//		}
//		if (x == top())
//			{x = pop(); t[k++] = x;} 
//	}
//}
//
//int root()
//{
//	r = N;
//	N++;
//	int i, k;
//	for (i = 0; i < N-1; i++)
//	{
//		if (G[i][G[i][0]+1] == 0)
//		{
//			k = ++G[N-1][0];
//			G[N-1][k] = i;
//
//			k = ++G[i][G[i][0]+1];
//			G[i][G[i][0]+1+k] = r;
//
//			M++;
//		}
//	}
//	return r;
//}
//
//void main(void)
//{
//	// Вход: 
//	// N M // брой върхове, брой ребра
//	// a b // ребро - начален, краен връх
//	// ... // ребро - начален, краен връх
//	scanf("%d", &N);
//	scanf("%d", &M);
//	int i, j, k = 0, p;
//	for (i = 0; i < N+1; i++)
//	{
//		for (j = 0; j < N+3; j++)
//			G[i][j] = 0;
//		visited[i] = false;
//	}
//	while (k < M) 
//	{
//		scanf("%d", &i);
//		scanf("%d", &j);
//
//		G[i][0]++;
//		G[i][G[i][0]] = j;
//		
//		k++;
//	}
//	for (i = 0; i < N; i++)
//	{
//		for (j = 1; j <= G[i][0]; j++)
//		{
//			p = G[i][j];
//			k = ++G[p][G[p][0] + 1];
//			G[p][G[p][0] + k + 1] = i;
//		}
//	}
//	root();
//	DFS();
//	for (i = 0; i < N-1; i++)
//	{
//		printf("%d", t[i]);
//	}
//}