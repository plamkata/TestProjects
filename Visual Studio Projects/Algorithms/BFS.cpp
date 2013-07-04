//#include <stdio.h>
//
//const int MAXN = 80;
//int N, M;
//int G[MAXN][MAXN];
//int r;
//
//int top = 0, end = 0;
//int queue[MAXN];
//
//int k = 0;
//int t[MAXN];
//bool visited[MAXN];
//
//void clear()
//{
//	top = end = 0;
//}
//
//bool empty()
//{
//	return top == end;
//}
//
//void push(int x)
//{
//	queue[end] = x;
//	end = (++end)%MAXN;
//}
//
//int pop()
//{
//	int x = queue[top];
//	top = (++top)%MAXN;
//	return x;
//}
//
//void BFS()
//{
//	int i, x, y;
//	clear();
//	push(r); visited[r] = true; t[r] = -1;
//	while(!empty())
//	{
//		x = pop();
//		for (i = 1; i <= G[x][0]; i++)
//		{
//			y = G[x][i];
//			if (!visited[y]) 
//			{
//				push(y); visited[y] = true; t[y] = x;
//			}
//		}
//	}
//}
//
//void main(void)
//{
//	// Вход: 
//	// N M // брой върхове, брой ребра
//	// a b // ребро - начален, краен връх
//	// ... // ребро - начален, краен връх
//	// r   // корен
//	scanf("%d", &N);
//	scanf("%d", &M);
//	int i, j, k = 0;
//	for (i = 0; i < N; i++)
//	{
//		G[i][0] = 0;
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
//		G[j][0]++;
//		G[j][G[j][0]] = i;
//		k++;
//	}
//	scanf("%d", &r);
//	BFS();
//	for (i = 0; i < N; i++)
//	{
//		printf("%d", t[i]);
//	}
//}