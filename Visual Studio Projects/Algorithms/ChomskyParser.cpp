//#include <stdio.h>
//#include <string.h>
//#include <ctype.h>
//
//const int MAXN = 100;
//const int MAXL = 100;
//const int MAX_TERMS = 6;
//const int MAX_NTERMS = 10;
//
//int N;
//int cterms = 0;
//int crules = 0;
//int L = 0;
//char terms[MAX_TERMS][2];
//char rules[MAXN][3];
//
//char data[MAXL][MAXL][MAX_NTERMS];
//
//void init()
//{
//	// read gramar
//	scanf("%d ", &N);
//	char line[6];
//	int i;
//	for (i = 0; i < N; i++)
//	{
//		gets(line);
//		if (isalpha(line[3]) == 0 || islower(line[3]) != 0)
//		{
//			terms[cterms][0] = line[3];
//			terms[cterms][1] = line[0];
//			cterms++;
//		}
//		else 
//		{
//			rules[crules][0] = line[0];
//			rules[crules][1] = line[3];
//			rules[crules][2] = line[4];
//			crules++;
//		}
//	}
//
//	// read word
//	char str[MAXL];
//	gets(str);
//	i = 0;
//	int j;
//	while (str[i] != '\0'&& i < MAXL)
//	{
//		for(j = 0; j < cterms; j++)
//			if (str[i] == terms[j][0]) {str[i] = terms[j][1]; break;}
//		data[L][L][0] = str[i];
//		data[L][L][1] = '\0';
//		L++;
//		i++;
//	}
//}
//
//int parent_nterms(const char A, const char B, char* terms, int count)
//{
//	int i, j;
//	bool b;
//	for (i = 0; i < crules; i++)
//	{
//		if (rules[i][1] == A && rules[i][2] == B)
//		{
//			b = true;
//			for (j = 0; j < count; j++)
//				if (rules[i][0] == terms[j]) 
//				{
//					b = false; break;
//				}
//			if (b) terms[count++] = rules[i][0];
//		}
//	}
//	terms[count] = '\0';
//	return count;
//}
//
//int nterms(char* p, char* q, char* terms, int count)
//{
//	int i=0,j=0;
//	while(p[i])
//	{
//		while(q[j])
//		{
//			count = parent_nterms(p[i], q[i], terms, count);
//			j++;
//		}
//		i++;
//	}
//	terms[count] = '\0';
//	return count;
//}
//
//void spread()
//{
//	int i, j, k, s, count = 0;;
//	for (k = 1; k <= L; k++)
//		for (i = 0, j = k; i < L-k && j < L; i++, j++)
//		{
//			count = 0;
//			for (s = 0; s < k; s++)
//				count = nterms(data[i][i+s], data[i+s+1][j], data[i][j], count);
//		}
//}
//
//void main(void)
//{
//	init();
//	spread();
//	int i = 0;
//	char* c = data[0][L-1];
//	while (c[i])
//	{
//		if (c[i] == rules[0][0])
//		{
//			printf("YES\n");return;
//		}
//		i++;
//	}
//	printf("NO\n");
//}