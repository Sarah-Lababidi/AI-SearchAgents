import java.util.HashSet;

public class TreeNode implements Comparable<TreeNode>{
    TreeNode parent;
    State state;
    Operator operator;
    int cost;
    int depth;

    // To set the evaluation function according to the strategy
    public static String strategy = "";

    public TreeNode(TreeNode parent, State state, Operator operator, int cost, int depth){
        this.parent = parent;
        this.state = state;
        this.operator = operator;
        this.cost = cost;
        this.depth = depth;
    }

//    -------------------------------------------- Getters --------- ---------------------------------------------------

    public TreeNode getParent() {
        return parent;
    }

    public State getState() {
        return state;
    }

    public Operator getOperator() {
        return operator;
    }

    public int getCost() {
        return cost;
    }

    public int getDepth() {
        return depth;
    }
    public int getF(){
        int f;
        switch (strategy){
            case "BF":
                f = 0;
                break;
            case "DF":
                f = 0;
                break;
            case "ID":
                f = 0;
                break;
            case "UC":
                f = cost;
                break;
            case "GR1":
                f = h1(this.state);
                break;
            case "GR2":
                f = h2(this.state);
                break;
            case "AS1":
                f = h1(this.state)+cost;
                break;
            case "AS2":
                f = h2(this.state)+cost;
                break;
            default:
                f=0;
        }
        return f;
    }
    // To order the priority queue
    @Override
    public int compareTo(TreeNode n){
        if(getF()==n.getF())
            return 0;
        else if(getF()>n.getF())
            return 1;
        else
            return -1;
    }

    public int h1(State s){
        // number of remaining stones*3 (damage of remaining stones)+ 10(adjacent and at Thanos)
        return s.getStones().size()*3 +10;
    }
    public int h2(State s){
        int damage = 0;
        String left, right, up, down;
        String[] partsStones;
        int xStone, yStone;
        HashSet<String> stones = new HashSet<>(s.getStones());
        HashSet<String> warriors = new HashSet<>(s.getWarriors());
        // Get the damage from the warriors adjacent to the stones
        for(String stone: stones){
            partsStones = stone.split(",");
            xStone = Integer.parseInt(partsStones[0]);
            yStone = Integer.parseInt(partsStones[1]);
            left = xStone+","+(yStone-1);
            right = xStone+","+(yStone+1);
            up = (xStone-1)+","+yStone;
            down = (xStone+1)+","+yStone;
            if(warriors.contains(left)){
                damage+=1;
            }
            if(warriors.contains(right)){
                damage+=1;
            }
            if(warriors.contains(up)){
                damage+=1;
            }
            if(warriors.contains(down)){
                damage+=1;
            }        }
        // damage from stones and Thanos
        damage +=stones.size()*3;
        damage+=10;
        return damage;
    }

}