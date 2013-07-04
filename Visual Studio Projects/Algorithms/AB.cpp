//#include <stdio.h>
//#include <stdlib.h>
//
//unsigned const int MAXN = 100000;
//unsigned const int MAXM = 100000;
//unsigned const int MAXK = 7000000;
//
//unsigned** results = new unsigned*[MAXK];
//int raw = 0;
//
//unsigned A[MAXN], B[MAXM];
//unsigned M, N, K;
//
//unsigned indices[MAXN];
//unsigned elements[MAXN];
//
//typedef struct heap {
//	unsigned data[MAXN+1];
//	unsigned size;
//} bheap;
//
//bheap h;
//
//unsigned float_up(unsigned ind) 
//{
//	unsigned result = ind, tmp;
//	while (result > 1 && elements[h.data[result]] < elements[h.data[result/2]])
//	{
//		tmp = h.data[result]; h.data[result] = h.data[result/2]; h.data[result/2] = tmp;
//		result = result/2;
//	}
//	return result;
//}
//
//unsigned float_down(unsigned ind)
//{
//	unsigned result = ind, tmp;
//	while (2*result < h.size && 
//		(elements[h.data[result]] > elements[h.data[2*result]] || 
//		elements[h.data[result]] > elements[h.data[2*result+1]]))
//	{
//		if (elements[h.data[2*result]] > elements[h.data[2*result+1]])
//		{
//			tmp = h.data[2*result+1]; h.data[2*result+1] = h.data[result]; h.data[result] = tmp;
//			result = 2*result+1;
//		} 
//		else
//		{
//			tmp = h.data[2*result]; h.data[2*result] = h.data[result]; h.data[result] = tmp;
//			result = 2*result;
//		}
//	}
//
//	if (2*result == h.size)
//	{
//		if (elements[h.data[2*result]] < elements[h.data[result]])
//		{
//			tmp = h.data[2*result]; h.data[2*result] = h.data[result]; h.data[result] = tmp;
//			result = 2*result;
//		}
//	}
//	return result;
//}
//
//void clear()
//{
//	h.size = 0;
//}
//
//bool is_empty()
//{
//	return h.size == 0;
//}
//
//void push(unsigned index)
//{
//	h.size++;
//	h.data[h.size] = index;
//	float_up(h.size);
//}
//
//unsigned top()
//{
//	unsigned result = h.data[1];
//	return result;
//}
//
//unsigned pop()
//{
//	unsigned result = h.data[1];
//	h.data[1] = h.data[h.size];
//	h.size--;
//	float_down(1);
//	return result;
//}
//
//void solve(unsigned* a, unsigned n, unsigned* b, unsigned m)
//{
//	unsigned start = 0, end, count = 1, minInd;
//	
//	//printf("%u ", a[0]*b[0]);
//	results[raw][count] = a[0]*b[0];
//	count++;
//
//	indices[0] = 1;
//	elements[0] = a[0]*b[indices[0]];
//	push(0);
//	
//	indices[1] = 0;
//	elements[1] = a[1]*b[indices[1]];
//	push(1);
//	
//	end = 2;
//	while (count <= K) 
//	{
//		minInd = top();	
//
//		//printf("%u ", elements[minInd]);
//		results[raw][count] = elements[minInd];
//		count++;
//		
//		indices[minInd]++;
//		if (indices[minInd] < m)
//		{
//			elements[minInd] = a[minInd]*b[indices[minInd]];
//			float_down(1);
//		}
//		else
//		{
//			pop();
//			start++;
//		}
//
//		if (indices[minInd] == 1 && end < n) 
//		{
//			indices[minInd+1] = 0;
//			elements[minInd+1] = a[minInd+1]*b[indices[minInd+1]];
//			push(minInd+1);
//			end++;
//		}
//	}
//	results[raw][0] = K;
//}
//
//int compare(const void *a, const void *b)
//{
//	return (int)(((unsigned *)a)[0] >= ((unsigned *)b)[0]);
//}
//
//int main(void)
//{
//	unsigned i, j;
//	
//	scanf("%u", &N);
//	scanf("%u", &M);
//	scanf("%u", &K);
//	while (M != 0 || N != 0 || K != 0) {
//		results[raw] = new unsigned[MAXK];
//
//		for (i = 0; i < N; i++)
//			scanf("%u", &A[i]);
//			
//		for (i = 0; i < M; i++)
//			scanf("%u", &B[i]);
//
//		qsort((void *)A, (size_t)N, sizeof(unsigned), compare);
//		qsort((void *)B, (size_t)M, sizeof(unsigned), compare);
//
//		if (N > M)
//			solve(A, N, B, M);
//		else
//			solve(B, M, A, N);
//
//		raw++;
//		clear();
//		scanf("%u", &N);
//		scanf("%u", &M);
//		scanf("%u", &K);
//	}
//
//	for (i = 0; i < raw; i++)
//	{
//		printf("\n");
//		for (j = 1; j < results[i][0]; j++)
//			printf("%u ", results[i][j]);
//	}
//	printf("\n");
//
//}