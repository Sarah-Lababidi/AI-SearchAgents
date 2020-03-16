import java.util.HashSet;

public class State {
    String iron;
    String thanos;
    HashSet<String> stones;
    HashSet<String> warriors;

    public State(String iron, String thanos, HashSet<String> stones, HashSet<String> warriors) {
        this.iron = iron;
        this.thanos = thanos;
        this.stones = stones;
        this.warriors = warriors;
    }

    //    ----------------------------------------------- Getters ------------------------------------------------------------

    public String getIron() {
        return iron;
    }

    public String getThanos() {
        return thanos;
    }

    public HashSet<String> getStones() {
        return stones;
    }

    public HashSet<String> getWarriors() {
        return warriors;
    }

//    ---------------------------------------------- Methods -----------------------------------------------------------
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        State state = (State) obj;

        if(iron.equals(state.getIron()) &&
           thanos.equals(state.getThanos()) &&
           stones.equals(state.getStones()) &&
           warriors.equals(state.getWarriors()) ) {
            return true;
        } else {
            return false;
        }
    }


    public static void printState(State s){
        System.out.println("Iron Man Location is at: ("+s.getIron()+"). \n Thanos Location is at: ("+
                s.getThanos()+"). \n Stones locations are: ("+s.getStones()+"). \n Warriors locations are: ("+
                s.getWarriors()+"). ");
    }
    public static String getKey(State state){
        String key = state.getIron()+";"+state.getThanos()+";"+String.join("-", state.getStones())+";"+String.join("-", state.getWarriors());
        return key;
    }

}
