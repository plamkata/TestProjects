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
//	return 0 == end;
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
//// DFS walking from root towords depth leaves
//// but pops from depth leaves towards root.
//// this will work for topological sorting of a dag.
//void DFS()
//{
//	int i, x, y;
//	clear();
//	push(r); visited[r] = true; t[r] = -1;
//	while(!empty())
//	{
//		x = top();
//		for (i = G[x][0]; i >= 1; i--)
//		{
//			y = G[x][i];
//			if (!visited[y]) {
//				push(y); visited[y] = true; t[y] = x;
//			}
//		}
//		if (x == top()) x = pop(); 
//	}
//}
//
////// DFS walking by edges from the root. This dosen't work
////// for topological sorting since leaves are not the first
////// that come out from the stack.
////void DFS()
////{
////	int i, x, y;
////	clear();
////	push(r);
////	while(!empty())
////	{
////		x = pop(); visited[x] = true; t[k++] = x; 
////		for (i = 1; i <= G[x][0]; i++)
////		{
////			y = G[x][i];
////			if (!visited[y]) push(y);
////		}
////	}
////}
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
//	DFS();
//	for (i = 0; i < N; i++)
//	{
//		printf("%d", t[i]);
//	}
//}