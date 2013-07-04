//#include <stdio.h>
//
//const unsigned MAXN = 100;
//
//unsigned N;
//float height[MAXN];
//float inteligence[MAXN];
//
//unsigned count[MAXN];
//unsigned indices[MAXN];
//float intsum[MAXN];
//
//void init()
//{
//	scanf("%u", &N);
//	unsigned i;
//	float f;
//	for (i = 0; i < N; i++)
//	{
//		scanf("%f", &f);
//		height[i] = f;
//	}
//	for (i = 0; i < N; i++)
//	{
//		scanf("%f", &f);
//		inteligence[i] = f;
//	}
//}
//
//unsigned find_best(unsigned ind)
//{
//	
//	unsigned i, best = ind;
//	unsigned bestCount = 0;
//	float bestIntel = 0.0;
//	for (i = ind+1; i < N; i++)
//	{
//		if (height[i] >= height[ind] && 
//			bestCount <= count[ind] && 
//			bestIntel < intsum[i])
//		{
//			bestCount = count[ind];
//			bestIntel = intsum[i];
//			best = i;
//		}
//	}
//	return best;
//}
//
//void spread()
//{ 
//	unsigned i, best;
//	for (i = N-1; i >= 0 && i < N; i--)
//	{
//		best = find_best(i);
//		if (best == i)
//		{
//			count[i] = 1;
//			indices[i] = -1;
//			intsum[i] = inteligence[i];
//		} 
//		else
//		{
//			count[i] = count[best]+1;
//			indices[i] = best;
//			intsum[i] = inteligence[i] + intsum[best];
//		}
//	}
//}
//
//void printRemaining()
//{
//	unsigned maxCount = 0;
//	float maxIntel = 0.0; 
//	unsigned i, maxInd;
//	for (i = 0; i < N; i++)
//	{
//		if (maxCount <= count[i] && maxIntel < intsum[i])
//		{
//			maxCount = count[i];
//			maxIntel = intsum[i];
//			maxInd = i;
//		}
//	}
//
//	while (maxInd != -1)
//	{
//		printf("%u ", maxInd);
//		maxInd = indices[maxInd];
//	}
//	printf("\n");
//	
//}
//
//void main(void)
//{
//	init();
//	spread();
//	printRemaining();
//}
