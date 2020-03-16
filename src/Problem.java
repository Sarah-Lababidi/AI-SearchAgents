public abstract class Problem {
    public State initialState;
    public Operator operator;
    public Problem(State initialState){
        this.initialState = initialState;
    }

//    ---------------------------------- Getters and setters -----------------------------------------------------------

    public State getInitialState() {
        return this.initialState;
    }

    public void setInitialState(State initialState) {
        this.initialState = initialState;
    }

//    ---------------------------------- Transition, path cost, goal test functions ------------------------------------

    public abstract State transitionFunction(State state, Operator operator);

    public abstract int pathCost(State oldState, Operator o);

    public abstract boolean goalTest(State state);
}

