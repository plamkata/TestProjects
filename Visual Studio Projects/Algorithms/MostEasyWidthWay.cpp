//// Definirame 6iro4ina(propuskliwost) na pyt po sledniq na4in:
//// P = (V0,V1,V2,...Vk-1,Vk), ako P' = (V0,V1,V2,...Vk-1)
//// to w(P) = (w(P') + w(Vk-1,Vk))/2, pri k>1
//// i  w(P) = w(Vk-1,Vk), pri k=1;
//
//#include <stdio.h>
//
//const int MAXN = 100;
//const int MAXM = (MAXN*(MAXN-1))/2;
//
//int N;
//int M;
//int G[MAXN][MAXN];
//float w[MAXN*MAXN];
//
//int p[MAXN];
//float d[MAXN];
//bool visited[MAXN];
//
//int initG()
//{
//	scanf("%d", &N);
//	scanf("%d", &M);
//	int i;
//	for (i = 0; i < N; i++)
//	{
//		G[i][0] = 0;
//		d[i] = 0;
//		visited[i] = false;
//	}
//	int n, m;
//	float width;
//	for (i = 0; i < M; i++)
//	{
//		scanf("%d %d %f", &n, &m, &width);
//		G[n][0]++;
//		G[n][G[n][0]] = m;
//		w[n*N+m] = width;
//		G[m][0]++;
//		G[m][G[m][0]] = n;
//		w[m*N+n] = width;
//	}
//
//	int r;
//	scanf("%d", &r);
//	return r;
//}
//
//void print()
//{
//	int i;
//	for (i = 0; i < N; i++)
//	{
//		printf("%d", p[i]);
//	}
//	printf("\n");
//}
//
////// obiknowen dijkstra sys spisyk na sysedite
////// O(N*N + 2*M)
////void dijkstra(int src)
////{
////	p[src] = -1;
////	d[src] = 0.0;
////	int u = src, v, max, i;
////	float maxWidth;
////	while (u != -1)
////	{
////		visited[u] = true;
////		for (i = 1; i <= G[u][0]; i++)
////		{
////			v = G[u][i];
////			if (!visited[v] && d[v] < (d[u] + w[v*N+u])/2)
////			{
////				if (u == src)
////					d[v] = w[v*N+u];
////				else 
////					d[v] = (d[u] + w[v*N+u])/2; 
////				p[v] = u;
////			}
////		}
////
////		max = -1;
////		maxWidth = 0;
////		for (i = 1; i <= G[u][0]; i++)
////		{
////			v = G[u][i];
////			if (d[v] > maxWidth && !visited[v])
////			{
////				maxWidth = d[v]; max = v;
////			}
////		}
////		u = max;
////	}
////}
//
/////////////// re6enie s heap /////////////////
//typedef struct bheap
//{
//	int size;
//	int data[MAXN];
//} heap;
//
//heap h;
//
//int floatUp(int index)
//{
//	int parent = index/2, tmp;
//	while (parent > 0 && d[h.data[parent]] < d[h.data[index]])
//	{
//		tmp = h.data[parent]; h.data[parent] = h.data[index]; h.data[index] = tmp;
//		index = parent;
//		parent = parent/2;
//	}
//	return index;
//}
//
//int floatDown(int ind)
//{
//	int tmp;
//	while (2*ind < h.size && 
//		(d[h.data[2*ind]] > d[h.data[ind]] || d[h.data[2*ind+1]] > d[h.data[ind]]))
//	{
//		if (d[h.data[2*ind]] > d[h.data[2*ind+1]])
//		{
//			tmp = h.data[ind]; h.data[ind] = h.data[2*ind]; h.data[2*ind] = tmp;
//			ind = 2*ind;
//		}
//		else 
//		{
//			tmp = h.data[ind]; h.data[ind] = h.data[2*ind+1]; h.data[2*ind+1] = tmp;
//			ind = 2*ind+1;
//		}
//	}
//	if (2*ind == h.size && d[h.data[2*ind]] > d[h.data[ind]])
//	{
//		tmp = h.data[ind]; h.data[ind] = h.data[2*ind]; h.data[2*ind] = tmp;
//		ind = 2*ind;
//	}
//	return ind;
//}
//
//void clear()
//{
//	h.size = 0;
//}
//
//bool isEmpty()
//{
//	return h.size == 0;
//}
//
//void push(int x)
//{
//	h.size++;
//	h.data[h.size] = x;
//	floatUp(h.size);
//}
//
//int top()
//{
//	return h.data[1];
//}
//
//int pop()
//{
//	int x = h.data[1];
//	h.data[1] = h.data[h.size];
//	h.size--;
//	floatDown(1);
//	return x;
//}
//
//void dijkstra(int src)
//{
//	clear();
//	p[src] = -1;
//	d[src] = 0.0;
//	push(src);
//	int u = src, v, i;
//	while (!isEmpty())
//	{
//		u = pop();
//		visited[u] = true;
//		for (i = 1; i <= G[u][0]; i++)
//		{
//			v = G[u][i];
//			if (!visited[v] && d[v] < (d[u] + w[u*N+v])/2)
//			{
//				if (u == src)
//					d[v] = w[v*N+u];
//				else 
//					d[v] = (d[u] + w[v*N+u])/2; 
//				p[v] = u;
//				push(v);
//			}
//		}
//	}
//}
//
//void main(void)
//{
//	int r = initG();
//	dijkstra(r);
//	print();
//}