//#include <stdio.h>
//
//const int MAXN = 80;
//const float MAXWEIGHT = 1000000.0;
//int N, M;
//int G[MAXN][MAXN];
//float w[MAXN*MAXN];
//
//float d[MAXN];
//int p[MAXN];
//bool visited[MAXN];
//
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
//	while (parent > 0)
//	{
//		if (d[h.data[parent]] > d[h.data[index]])
//		{
//			tmp = h.data[parent]; h.data[parent] = h.data[index]; h.data[index] = tmp;
//			index = parent;
//			parent = parent/2;
//		} 
//		else
//			break;
//	}
//	return index;
//}
//
//int floatDown(int ind)
//{
//	int tmp;
//	while (2*ind < h.size && 
//		(d[h.data[2*ind]] < d[h.data[ind]] || d[h.data[2*ind+1]] < d[h.data[ind]]))
//	{
//		if (d[h.data[2*ind]] < d[h.data[2*ind+1]])
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
//	
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
////// Min pokriwa6to dyrwo po Dijkstra (spisyk na sysedi) (iterativno)
////// O(N*N + 2*M)
////void dijkstra(int src)
////{
////	p[src] = -1; d[src] = 0;
////	int u = src, v, i;
////	float minD;
////	while (u != -1)
////	{
////		// visit
////		visited[u] = true;
////		// relax all childs of u
////		for (i = 1; i <= G[u][0]; i++)
////		{
////			v = G[u][i];
////			if (d[u] + w[u*N + v] < d[v])
////			{
////				d[v] = d[u] + w[u*N + v]; p[v] = u;
////			}
////		}
////
////		// find the nearest unvisited neighbour
////		minD = MAXWEIGHT;
////		v = -1;
////		for (i = 1; i <= G[u][0]; i++)
////		{
////			if (!visited[G[u][i]] && d[G[u][i]] < minD)
////			{
////				minD = d[v];
////				v = G[u][i];
////			}
////		}
////		u = v;
////	}
////}
//
//// Min pokriwa6to dyrwo po Dijkstra (spisyk na sysedi)(heap)
//// O((M + N)*log N)
//void dijkstra(int src)
//{
//	p[src] = -1; d[src] = 0; 
//	clear(); push(src);
//	int u = src, v, i;
//	while (!isEmpty())
//	{
//		u = pop(); 
//		//visited[u] = true;
//		// relax all childs of u
//		for (i = 1; i <= G[u][0]; i++)
//		{
//			v = G[u][i];
//			if (d[u] + w[u*N + v] < d[v])
//			{
//				d[v] = d[u] + w[u*N + v]; p[v] = u;
//				//if (!visited[v]) 
//					push(v);
//				// no need to check if it was visited
//				// since you are making relaxation
//			}
//		}
//		
//		u = top();// find next min
//	}
//}
//
//void main(void)
//{
//	scanf("%d", &N);
//	scanf("%d", &M);
//	int i, u, v;
//	float weight;
//
//	for (i = 0; i < N; i++)
//	{
//		d[i] = MAXWEIGHT;
//		visited[i] = false;
//	}
//
//	for (i = 0; i < N*N; i++)
//	{
//		w[i] = MAXWEIGHT;
//	}
//
//	for (i = 0; i < M; i++)
//	{
//		scanf("%d %d %f", &u, &v, &weight);
//		G[u][0]++;
//		G[u][G[u][0]] = v;
//		w[u*N + v] = weight;
//
//		G[v][0]++;
//		G[v][G[v][0]] = u;
//		w[v*N + u] = weight;
//	}
//
//	int start = 0;
//	scanf("%d", &start);
//
//	dijkstra(start);
//
//	for (i = 0; i < N; i++)
//		printf("%d", p[i]);
//}