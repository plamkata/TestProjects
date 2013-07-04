#include <stdio.h>

const int MAXN = 80;
int graph[MAXN][MAXN];

int p[MAXN];

void main(void)
{

}
//#include <vector>
//#include <algorithm>
//
//int dijkstra(const vector<vector<int>> &graph, int source, int dest) 
//{
//	typedef pair<int,int> path;
//	priority_queue<path, vector<path>, greater<path>> q;
//	vector<bool> b(graph.size(), false);
//
//	q.push(make_pair(0, source));	
//	while (!q.empty()) {
//		int w = q.top().first();
//		int u = q.top().second();
//		q.pop();
//		if(!b[u]) {
//			if(u == dest) return w;
//			b[u] = true;
//			FOR(e child of u) 
//				if(!b[e]) q.push(make_pair(w+graph[u][e], e));
//		}
//	}
//	return -1;
//} 