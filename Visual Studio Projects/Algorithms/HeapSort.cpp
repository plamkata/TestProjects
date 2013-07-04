//#include <stdio.h>
//
//typedef struct bheap
//{
//	int* data;// data[0] is not used
//	int size;// size points to the last element
//} heap;
//
//// returns the new index of the element
//int float_up(heap h, int ind)
//{
//	int tmp;
//	while (h.data[ind] > h.data[ind/2] && ind > 1)
//	{
//		tmp = h.data[ind];
//		h.data[ind] = h.data[ind/2];
//		h.data[ind/2] = tmp;
//		ind = ind/2;
//	}
//	return ind;
//}
//
//// takes elment at ind and finds its place in the heap
//int float_down(heap h, int ind)
//{
//	int tmp;
//	while (2*ind < h.size && 
//		(h.data[ind] < h.data[2*ind] || h.data[ind] < h.data[2*ind + 1]))
//	{
//		if (h.data[2*ind] < h.data[2*ind+1])
//		{
//			tmp = h.data[ind]; h.data[ind] = h.data[2*ind+1]; h.data[2*ind+1] = tmp;
//			ind = 2*ind+1;
//		}
//		else 
//		{
//			tmp = h.data[ind]; h.data[ind] = h.data[2*ind];	h.data[2*ind] = tmp;
//			ind = 2*ind;
//		}
//	}
//
//	if (2*ind == h.size && h.data[2*ind] > h.data[ind])
//	{
//		tmp = h.data[ind]; h.data[ind] = h.data[2*ind]; h.data[2*ind] = tmp;
//		ind = 2*ind;
//	}
//	return ind;
//}
//
//// retuns the index where x is added
//int add(heap h, int x)
//{
//	h.size++;
//	h.data[h.size] = x;
//	return float_up(h, h.size);
//}
//
//int top(heap h) 
//{
//	int result = h.data[1];
//	h.data[1] = h.data[h.size--];
//	float_down(h, 1);
//	return result;
//}
//
//// makes a heap
//void heapify(heap h) 
//{
//	int i;
//	for (i = h.size/2; i >= 1; i--)	float_down(h, i);
//}
//
//void heapSort(heap h) 
//{
//	heapify(h);
//	int n = h.size, tmp;
//	while (h.size >= 2)
//	{
//		tmp = h.data[1];
//		h.data[1] = h.data[h.size];
//		h.data[h.size] = tmp;
//
//		h.size--;
//		float_down(h, 1);
//	}
//	h.size = n;
//}
//
//void main()
//{
//	int A[] = {8, 6, 1, 2, 9, 7, 5, 6, 3, 9, 4};
//	int N = 11;
//	printf("A: \n");
//	int i;
//	for (i = 0; i < N; i++) printf("%d, ", A[i]);
//	printf("\n");
//	
//	heap h;
//	h.data = (A-1);
//	h.size = N;
//	heapSort(h);
//
//	printf("sorted: \n");
//	for (i = 0; i < N; i++) printf("%d, ", A[i]);
//	printf("\n");
//}