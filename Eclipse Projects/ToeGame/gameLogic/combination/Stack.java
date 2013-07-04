package gameLogic.combination;

interface Stack {

    // Postavia element na vurha na steka.
    public void push(int item) throws StackException;

    // Izvlicha st-sta na elementa na vurha na steka.
    public int top() throws StackException;

    // Izvlicha st-sta na elementa na vurha na steka i go iztriva (elementa).
    public int pop() throws StackException;

}
