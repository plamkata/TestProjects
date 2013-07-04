#include <stdio.h>

namespace test_sequences {

// X[k] - is the array of integers
// M[j] - stores the index k of the smallest value X[k] of an increasing subsequence with length j
// P[k] - stores the previouse element before X[k] in the longest subsequence
// X[M[0]], X[M[1]], X[M[2]], ... is non-decreasing

int Max(int left, int right) {
    return (left > right) ? left : right;
} 

int BinarySearch(int* M, int length, int* X, int i) {
    int result = 0;

    int left = 0;
    int right = length;
    while (left <= right) {
        int mid = left + (right - left) / 2;

        if (X[M[mid]] < X[i]) {

            if (mid == length || X[i] <= X[M[mid + 1]]) {
                return mid;
            } else {
                left = mid + 1;
            }
        } else {
            right = mid - 1;
        }
    }

    return 0;
}

int FindSubsequence(int* X, int size, int* P) {
    int length = 0;
    int* M = new int[size + 1];
    // M[j] stores the position k of the smallest value X[k], such that there is 
    // an increasing subsequence of length j ending at X[k] on the range k ≤ i
    M[0] = -1; // unused, P[i] = -1 indicates no previous sequence 

    for (int i = 0; i < size; ++i) {
        // binary search the largest possible j, such that X[M[j]] < X[i] or set to 0
        int j = BinarySearch(M, length, X, i);
        P[i] = M[j];
        if (j == length || X[i] < X[M[j + 1]]) {
            M[j + 1] = i;
            length = Max(j + 1, length);
        }            
    }

    int last_ind = M[length];
    delete [] M;
    return last_ind;
}

void Print(int* X, int* P, int j) {
    if (j == -1) return;

    Print(X, P, P[j]);
    printf("%d ", X[j]);
}

}

//int main() {
//    printf("Enter number of elements: \n");
//    int size;
//    scanf("%d", &size);
//
//    printf("Enter elements: \n");
//    int* X = new int[size];
//    for (int i = 0; i < size; ++i) {
//        scanf("%d", &X[i]);
//    }
//
//    // find longest subsequence
//    int* P = new int[size];
//    int j = test_sequences::FindSubsequence(X, size, P);
//
//    printf("Longest increasing subsequence is: \n");
//    test_sequences::Print(X, P, j);
//    printf("\n");
//
//    delete [] P;
//    delete [] X;
//}