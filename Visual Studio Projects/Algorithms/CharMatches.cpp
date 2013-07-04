//#include <stdio.h>
//
//const int MAXN = 80;
//
//int aN, bN;
//char *a, *b;
//
//int** data;
//
//void spread1()
//{
//	int i, j;
//	for (i = 1; i < bN; i++)
//		for (j = 1; j < aN; j++)
//			if (a[j] == b[i])
//				data[i][j] = data[i-1][j-1] + 1;
//			else 
//				if (data[i][j-1] > data[i-1][j])
//					data[i][j] = data[i][j-1];
//				else
//					data[i][j] = data[i-1][j];
//}
//
//void find1(int i1, int j1)
//{
//	int i = i1, j = j1;
//	if ((i == 0) || (j == 0))
//	{
//		if (data[i][j] == 1) {printf("%c", a[j]); return;}
//		else return;
//	}
//	if (a[j] == b[i]) 
//	{
//		find1(i-1, j-1);
//		printf("%c", a[j]); 
//	}
//	else 
//		if (data[i-1][j] > data[i][j-1]) 
//			find1(i-1, j);
//		else 
//			find1(i, j-1);
//}
//
////void find1(int i1, int j1)
////{
////	int i = i1, j = j1;
////	while ((i > 0) && (j > 0))
////	{
////		if (b[i] == a[j])
////		{
////			printf("%c", b[i]);
////			i--; j--;
////		} 
////		else
////		{
////			if (data[i-1][j] > data[i][j-1]) 
////				i--;
////			else 
////				j--;
////		}
////	}
////	if (data[i][j] == 1)
////	{
////		printf("%c", a[j]);
////	}
////}
//
//void main(void)
//{
//	scanf("%d", &aN);
//	a = new char[aN];
//	scanf("%80s", a);
//	scanf("%d", &bN);
//	b = new char[bN];
//	scanf("%80s", b);
//
//	data = new int*[aN];
//	
//	int i;
//	for(i = 0; i < bN; i++)
//	{
//		data[i] = new int[aN];
//		if (a[0] == b[i])
//			data[i][0] = 1;
//		else
//			data[i][0] = 0;
//	}
//	for (i = 1; i < bN; i++)
//	{
//		if (a[i] == b[0]) 
//			data[0][i] = 1;
//		else 
//			data[0][i] = 0;
//	}
//
//	spread1();
//	printf("max subsequence length is: %d\n", data[bN-1][aN-1]);
//	find1(bN-1, aN-1);
//	printf("\n");
//}