#include <stdio.h>

namespace test_heap {

class MinHeap {
public:
    MinHeap(size_t max_size);
    ~MinHeap();

    int Top() const;
    int Pop();
    size_t Add(int e);

    void Sort();
    void Print() const;

private:
    void Heapify();

    size_t FloatUp(size_t pos);
    size_t FloatDown(size_t pos);
    void Swap(size_t i, size_t j);

private:
    size_t size;
    int* data; // data[0] is unused
};


MinHeap::MinHeap(size_t max_size) {
    this->size = 0;
    this->data = new int[max_size + 1];
}

MinHeap::~MinHeap() {
    this->size = 0;
    delete [] this->data;
}

int MinHeap::Top() const {
    return this->data[1];
}

int MinHeap::Pop() {
    int min = this->data[1];
    
    Swap(1, this->size);
    -- this->size;
    FloatDown(1);

    return min;
}

size_t MinHeap::Add(int e) {
    this->data[++this->size] = e;
    return FloatUp(this->size);
}

void MinHeap::Sort() {
    size_t esize = this->size;
    Heapify();
    while (this->size > 1) {
        Swap(1, this->size);

        --this->size;
        FloatDown(1);
    }
    this->size = esize;
}

void MinHeap::Print() const {
    for (size_t i = 1; i <= this->size; ++i) {
        printf("%d ", this->data[i]);
    }
    printf("\n");
}

void MinHeap::Heapify() {
    for (size_t ind = this->size / 2; ind >= 1; --ind) {
        FloatDown(ind);
    }
}

size_t MinHeap::FloatUp(size_t pos) {
    while (pos > 1 && this->data[pos] < this->data[pos / 2]) {
        Swap(pos, pos / 2);
        pos = pos / 2;
    }
    return pos;
}

size_t MinHeap::FloatDown(size_t pos) {
    while (2 * pos < this->size && (this->data[pos] > this->data[2 * pos] || 
            this->data[pos] > this->data[2 * pos + 1])) {

        if (this->data[2 * pos] < this->data[2 * pos + 1]) {
            Swap(pos, 2 * pos);
            pos = pos * 2;
        } else {
            Swap(pos, 2 * pos + 1);
            pos = pos * 2 + 1;
        }
    }

    if (2 * pos == this->size && this->data[pos] > this->data[2 * pos]) {
        Swap(pos, 2 * pos);
        pos = 2 * pos;
    }

    return pos;
}

void MinHeap::Swap(size_t i, size_t j) {
    int tmp = this->data[i];
    this->data[i] = this->data[j];
    this->data[j] = tmp;
}

}

//int main() {
//    printf("Enter number of elements: \n");
//    size_t size;
//    scanf("%d", &size);
//
//    printf("Enter elements: \n");
//    test_heap::MinHeap min_heap(size);
//    for (size_t i = 0; i < size; ++i) {
//        int element;
//        scanf("%d", &element);
//        min_heap.Add(element);
//    }
//
//    // Sort the elements
//    min_heap.Sort();
//
//    printf("Merge Sorted array is: \n");
//    min_heap.Print();
//    printf("\n");    
//}