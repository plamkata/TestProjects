#include <stdio.h>

typedef unsigned size_t;
typedef int elem_t;

void swap(elem_t* list, size_t left, size_t right) {
    elem_t tmp = list[left]; 
    list[left] = list[right]; 
    list[right] = tmp;
}

size_t partition(elem_t* list, size_t begin, size_t end, size_t pivot) {
    size_t k = begin;
    swap(list, pivot, end);
    for (size_t i = begin; i < end; ++i) {
        if (list[i] < list[end]) {
            swap(list, i, k);
            ++k;
        }
    }
    swap(list, k, end);
    return k;
}

size_t select_pivot(elem_t* list, size_t begin, size_t end) {
    size_t mid = begin + (end - begin) / 2;
    
    if (list[begin] > list[end]) {
        if (list[mid] > list[begin]) {
            return begin;
        } else {
            return (list[mid] > list[end]) ? mid : end;
        }
    } else {
        if (list[mid] > list[end]) {
            return end;
        } else {
            return (list[mid] < list[begin]) ? begin : mid;
        }
    }
}

elem_t select(elem_t* list, size_t begin, size_t end, size_t pos) {
    if (begin == end) {
        return list[begin];
    }

    size_t pivot = select_pivot(list, begin, end);
    size_t new_pivot = partition(list, begin, end, pivot);

    if (begin + pos == new_pivot) return list[new_pivot];
    else if (begin + pos < new_pivot) return select(list, begin, new_pivot - 1, pos);
    else return select(list, new_pivot + 1, end, pos - new_pivot - 1);
}

elem_t orderStat(elem_t* list, size_t lsize, size_t i) {
    return select(list, 0, lsize - 1, i);
}

//int main() {
//    printf("Please enter number of elemnts: \n");
//    int N = 0;
//    scanf("%d", &N);
//
//    printf("\nPlease enter elements: \n");
//    elem_t* list = new elem_t[N];
//    for (size_t i = 0; i < N; ++i) {
//        scanf("%u", &list[i]);
//    }
//
//    while (true) {
//        printf("\nPlease enter a stat number: \n");
//        size_t stat_num = 0;
//        scanf("%d", &stat_num);
//        if (stat_num >= N) break;
//
//        elem_t stat_elem = orderStat(list, N, stat_num);
//
//        printf("\nThe %dth largest element is %u", stat_num, stat_elem);
//    }
//}