#include <stdio.h>
#include <list>

const int MAX_NODES = 100;

namespace test_traversal {

template <typename T>
class Stack {
public:
    Stack(int size);
    ~Stack();

    bool IsEmpty();
    T Top();
    T Pop();
    void Push(T e);

private:
    T* data;
    int cur;
};

template <typename T>
Stack<T>::Stack(int size) : cur(-1) {
    this->data = new T[size];
}

template <typename T>
Stack<T>::~Stack() {
    delete [] this->data;
}

template <typename T>
bool Stack<T>::IsEmpty() {
    return this->cur == -1;
}

template <typename T>
T Stack<T>::Top() {
    // assert(this->cur != -1);
    return this->data[this->cur];
}

template <typename T>
T Stack<T>::Pop() {
    T top = this->data[this->cur];
    --this->cur;
    return top;
}

template <typename T>
void Stack<T>::Push(T e) {
    // assert(this->cur + 1 < size);
    this->data[++this->cur] = e;
}

struct Node {
    Node* left;
    Node* right;
    int value;
};

void InOrder(Node* n) {
    Stack<Node*> stack(MAX_NODES);
    
    while (!stack.IsEmpty() || n != NULL) {
        if (n != NULL) {
            stack.Push(n);
            n = n->left;
        } else {
            n = stack.Pop();
            printf("%d ", n->value);
            n = n->right;
        }
    }
}

void PreOrder(Node* n) {
    Stack<Node*> stack(MAX_NODES);
    stack.Push(n);

    while (!stack.IsEmpty()) {
        n = stack.Pop();

        if (n->right) stack.Push(n->right);
        if (n->left) stack.Push(n->left);
        
        printf("%d ", n->value);
    }
}

void PostOrder(Node* n) {
    Stack<Node*> stack(MAX_NODES);
    
    while (!stack.IsEmpty() || n != NULL) {
        if (n != NULL) {
            if (n->right) stack.Push(n->right);
            stack.Push(n);
            n = n->left;
        } else {
            n = stack.Pop();

            if (n->right && !stack.IsEmpty() && n->right == stack.Top()) {
                stack.Pop();
                stack.Push(n);
                n = n->right;
            } else {
                printf("%d ", n->value);
                n = NULL;
            }
        }
    }
}

Node* BuildThree(Node* root, std::list<int>* list, int n) {
    if (list->empty()) {
        return NULL;
    }

    Node* parent;
    
    bool switch_to_root = list->size() == n/2;
    if (switch_to_root) {
        parent = root;
        list->pop_front();
    } else {
        parent = new Node;
        parent->left = NULL;
        parent->right = NULL;
        parent->value = list->front();
        list->pop_front();
    }
    
    if (root == NULL) root = parent;

    if (parent->left == NULL) {
        if (list->empty() || list->front() > parent->value) {
            parent->left = NULL;
        } else {
            parent->left = BuildThree(root, list, n);
        }
    }

    if (parent->right == NULL) {
        if (list->empty() || list->front() < parent->value) {
            parent->right = NULL;
        } else {
            parent->right = BuildThree(root, list, n);
        }
    }
    
    return parent;
}

}

//int main() {
//    /*test_traversal::Stack<int> stack(MAX_NODES);
//    for (int i = 0; i < MAX_NODES; ++i) {
//        stack.Push(i);
//    }
//
//    for (int i = 0; i < MAX_NODES; ++i) {
//        int e = stack.Pop();
//        printf("%d ", e);
//    }
//    printf("\n");*/
//
//    int myints[] = {5, 4, 2, 1, 3, 6, 6, 8, 7, 9};
//    std::list<int> list (myints, myints + sizeof(myints) / sizeof(int) );
//
//    test_traversal::Node* root = test_traversal::BuildThree(NULL, &list, list.size());
//
//    printf("In-order traversal: \n");
//    test_traversal::InOrder(root);
//
//    printf("Pre-order traversal: \n");
//    test_traversal::PreOrder(root);
//
//    printf("Post-order traversal: \n");
//    test_traversal::PostOrder(root);
//
//    char c;
//    scanf("%c", &c);
//}