package gameLogic.combination;

class Node{
    Node next;
    int data;
}

/**
 * Ordinary stack with dinamic allocation.
 */
final class StackDinamic
        implements Stack{

    private Node top = null;

    public void push(int item){
        Node n = new Node();
        n.data = item;
        n.next = top;
        top = n;
    }

    public int top() throws StackException {
        if(top == null) throw new StackException("gameLogic.combination.Stack is empty!");
        return top.data;
    }

    public int pop() throws StackException {
        if(top == null) throw new StackException("gameLogic.combination.Stack is empty!");
        int temp = top.data;
        top = top.next;
        return temp;
    }

    public void copyIn(StackDinamic newStack){
        newStack.top = getNext(top);
    }

    private Node getNext(Node n){
        if(n==null) {return null;}
        Node nn = new Node();
        nn.next = getNext(n.next);
        nn.data = n.data;
        return nn;
    }

}


