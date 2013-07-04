#include <stdio.h>

namespace test_sort {

void swap(int* list, size_t left, size_t right) {
    int tmp = list[left]; 
    list[left] = list[right]; 
    list[right] = tmp;
}

// Merge sorted arrays a and b, into memory of a, using extra memory c
void merge(int* a, size_t asize, int* b, size_t bsize, int* c) {
    for (int i = 0; i < asize; ++i) {
        c[i] = a[i];
    }

    size_t i, j, k = 0;
    for (i = 0, j = 0; i < asize || j < bsize;) {
        if (i == asize) a[k++] = b[j++];
        else if (j == bsize) a[k++] = c[i++];
        else if (c[i] > b[j]) a[k++] = b[j++];
        else a[k++] = c[i++];
    }
}

void merge_sort(int* list, size_t left, size_t right, int* c) {
    if (left >= right) {
        return;
    }

    int mid = left + (right - left) / 2;
    merge_sort(list, left, mid, c);
    merge_sort(list , mid + 1, right, c);

    merge(list, mid + 1, list + mid + 1, right - mid, c);
}

void bottom_sort(int* list, size_t lsize, int* c) {
    size_t i = 0;
    for (size_t m = 1; m < lsize; m *= 2) {
        for (i = m; i + m < lsize; i += 2*m) {
            merge(list + i - m, m, list + i, m, c);
        }
        if (i < lsize) {
            merge(list + i - m, m, list + i, lsize - i, c);
        }
    }
}

}

//int main() {
//    printf("Enter number of elements: \n");
//    size_t size = 0;
//    scanf("%d", &size);
//
//    printf("Enter elements: \n");
//    int* list = new int[size];
//    for (size_t i = 0; i < size; ++i) {
//        scanf("%d", &list[i]);
//    }
//
//    // Sort the elements
//    int* c = new int[size];
//    //test_sort::merge_sort(list, 0, size - 1, c);
//    test_sort::bottom_sort(list, size, c);
//
//    printf("Merge Sorted array is: \n");
//    for (size_t i = 0; i < size; i++) {
//        printf("%d ", list[i]);
//    }
//    printf("\n");
//
//    delete [] list;
//    delete [] c;
//}