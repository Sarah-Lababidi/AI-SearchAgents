import java.util.HashSet;

public class EndGame extends Problem {

    public EndGame(State initialState){
        super(initialState);
    }

//    ---------------------------------- Getters and setters -----------------------------------------------------------

    public State getInitialState() {
        return this.initialState;
    }

    public void setInitialState(State initialState) {
        this.initialState = initialState;
    }

//    ---------------------------------- Transition, path cost, goal test functions ------------------------------------

    public State transitionFunction(State state, Operator operator){
        // current Iron man location
        String[] partsIron = state.getIron().split(",");
        int xIron = Integer.parseInt(partsIron[0]);
        int yIron = Integer.parseInt(partsIron[1]);

        // current Thanos Location
        String[] partsThanos = state.getThanos().split(",");
        int xThanos = Integer.parseInt(partsThanos[0]);
        int yThanos = Integer.parseInt(partsThanos[1]);


        // current Stones (list of strings)
        HashSet<String> stonesLocations = new HashSet<>(state.getStones());

        // current Warriors
        HashSet<String> warriorsLocations = new HashSet<String>(state.getWarriors());

        int left, right, up, down;

        // xIron for the row, yIron for the column
        left = yIron-1;
        right = yIron+1;
        up = xIron-1;
        down = xIron+1;


        switch (operator){
            // We also need to check if he is moving to Thanos cell and he is able to kill him -> allow movement
            case UP:
                if(((up==xThanos)&&(yIron== yThanos)) && stonesLocations.isEmpty() && up>=0){
                    xIron = up;
                }
                // check if the upper cell has warriors, Thanos or beyond the wall
                if(!(up<0 || ((up==xThanos) &&(yIron== yThanos)) || warriorsLocations.contains(up+","+yIron))){
                    xIron = up;
                }
                break;
            case DOWN:
                if(((down==xThanos)&&(yIron== yThanos)) && stonesLocations.isEmpty() && down< Main.mGrid){
                    xIron = down;
                }
                // check if the below cell has warriors, Thanos or beyond the wall
                if(!(down>=Main.mGrid || ((down==xThanos)&&(yIron== yThanos)) || warriorsLocations.contains(down+","+yIron))){
                    xIron = down;
                }
                break;
            case RIGHT:
                if(((xIron==xThanos)&&(right== yThanos)) && stonesLocations.isEmpty() && right<Main.nGrid){
                    yIron = right;
                }
                // check if the right cell has warriors, Thanos or beyond the wall
                if(!(right>=Main.nGrid || ((xIron==xThanos)&&(right== yThanos)) || warriorsLocations.contains(xIron+","+right))){
                    yIron = right;
                }
                break;
            case LEFT:
                if(((xIron==xThanos)&&(left== yThanos)) && stonesLocations.isEmpty() && left>=0){
                    yIron = left;
                }
                // check if the upper cell has warriors, Thanos or beyond the wall
                if(!(left<0 || ((xIron==xThanos)&&(left== yThanos)) || warriorsLocations.contains(xIron+","+left))){
                    yIron = left;
                }
                break;
            case COLLECT:
                if(stonesLocations.contains(xIron+","+yIron)){
                    stonesLocations.remove(xIron+","+yIron);
                }
                break;
            case KILL:
                if (warriorsLocations.contains(xIron+","+left)){
                    warriorsLocations.remove(xIron+","+left);
                }
                if(warriorsLocations.contains(xIron+","+right)){
                    warriorsLocations.remove(xIron+","+right);
                }
                if (warriorsLocations.contains(up+","+yIron)){
                    warriorsLocations.remove(up+","+yIron);
                }
                if(warriorsLocations.contains(down+","+yIron)){
                    warriorsLocations.remove(down+","+yIron);
                }
                break;
            case SNAP:
                if(((xIron==xThanos)&&(yIron==yThanos)) && stonesLocations.isEmpty()){
                    xThanos = -10;
                    yThanos = -10;
                }
                break;
            default:
        }
        String iron = xIron+","+yIron;
        String thanos = xThanos+","+yThanos;

        State resultState = new State(iron, thanos, stonesLocations, warriorsLocations);
        return resultState;
    }

    public int pathCost(State oldState, Operator o){

        int damage = 0;
        State state = transitionFunction(oldState, o);
//        ------------------------------------------Damage caused by collect or kill------------------------------------
        damage += (oldState.getStones().size() - state.getStones().size())*3;
        damage += (oldState.getWarriors().size() - state.getWarriors().size())*2;


//        --------------------------------------------Damage from adjacent cells----------------------------------------
        // current Iron man location
        String[] partsIron = state.getIron().split(",");
        int xIron = Integer.parseInt(partsIron[0]);
        int yIron = Integer.parseInt(partsIron[1]);

        // current Thanos Location
        String[] partsThanos = state.getThanos().split(",");
        int xThanos = Integer.parseInt(partsThanos[0]);
        int yThanos = Integer.parseInt(partsThanos[1]);

        // current Warriors
        HashSet<String> warriorsLocations = new HashSet<>(state.getWarriors());


        int left, right, up, down;

        // xIron for the row, yIron for the column
        left = yIron-1;
        right = yIron+1;
        up = xIron-1;
        down = xIron+1;

        // check Thanos
        if(((xIron==xThanos)&&(yIron==yThanos)) || ((xIron==xThanos)&&(left==yThanos)) || ((xIron==xThanos)&&(right==yThanos)) ||
                ((up==xThanos)&&(yIron==yThanos)) || ((down==xThanos)&&(yIron==yThanos))){
            damage+=5;
        }

        // check warriors (If the action was snap-> stop calculating the surrounding damage)
        if(warriorsLocations.contains(xIron+","+left)&&xThanos!=-10){
            damage+=1;
        }
        if(warriorsLocations.contains(xIron+","+right)&&xThanos!=-10){
            damage+=1;
        }
        if(warriorsLocations.contains(up+","+yIron)&&xThanos!=-10){
            damage+=1;
        }
        if(warriorsLocations.contains(down+","+yIron)&&xThanos!=-10){
            damage+=1;
        }
        return damage;
    }

    public boolean goalTest(State state){
//        current Thanos Location
        String[] partsThanos = state.getThanos().split(",");
        int xThanos = Integer.parseInt(partsThanos[0]);
        int yThanos = Integer.parseInt(partsThanos[1]);
//        current Stones (list of strings)
        HashSet<String> stonesLocations = new HashSet<>(state.getStones());

//        Check if iron man has: 1)-killed Thanos. 2)-collected all stones
        if(stonesLocations.isEmpty() && ((xThanos==-10)&&(yThanos==-10))){
            return true;
        } else {
            return false;
        }
    }
}

