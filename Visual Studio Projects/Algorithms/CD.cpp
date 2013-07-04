//#include <stdio.h>
//
//const int MAXN = 100;
//const int MAXM = (MAXN*(MAXN-1))/2;
//
//int N, M;
//int G[MAXN][MAXN];
//
//int level[MAXN];
//bool visited[MAXN];
//
//int queue[MAXN];
//int s = 0, e = 0;
//
//void empty()
//{
//	s = e = 0;
//}
//
//bool isEmpty()
//{
//	return s == e;
//}
//
//void push(int x)
//{
//	queue[e] = x;
//	e++;
//	e = e%MAXN;
//}
//
//int get()
//{
//	return queue[s];
//}
//
//int pop()
//{
//	int x = queue[s];
//	s++;
//	s = s%MAXN;
//	return x;
//}
//
//void clear()
//{
//	int i;
//	for (i = 0; i < N; i++)
//	{
//		level[i] = 0;
//		visited[i] = false;
//	}
//	empty();
//}
//
//int BFS(int src) 
//{
//	clear();
//	push(src); visited[src] = true; level[src] = 0;
//	int v, u, i;
//	while(!isEmpty())
//	{
//		v = pop();
//		for (i = 1; i <= G[v][0]; i++)
//		{
//			u = G[v][i];
//			if (!visited[u]) 
//			{
//				push(u); visited[u] = true; level[u] = level[v]+1;
//			}
//		}
//	}
//	return level[v];
//}
//
//void main(void)
//{
//	scanf("%d %d", &N, &M);
//	int i;
//	for (i = 0; i < N; i++)
//	{
//		G[i][0] = 0;
//		level[i] = 0;
//		visited[i] = false;
//	}
//	int n, m;
//	for (i = 0; i < M; i++)
//	{
//		scanf("%d %d", &n, &m);
//		G[n][0]++;
//		G[n][G[n][0]] = m;
//		G[m][0]++;
//		G[m][G[m][0]] = n;
//	}
//
//	int count, min = MAXN;
//	for (i = 0; i < N; i++)
//	{
//		count = BFS(i);
//		if (min > count) min = count;
//	}
//	printf("%d", min);
//	printf("\n");
//}