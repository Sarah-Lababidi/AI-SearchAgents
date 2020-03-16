import java.util.*;

public class Main {
    public static int mGrid, nGrid;
    public static char[][] world;
    // To avoid repeated states(search complexity is O(1) in Hashmaps)
    private static HashMap<String, State> visited;
    public static boolean existVisited = false;


    public static int IDS_depth;
    public static TreeNode currentNode;
    public static TreeNode goal;

    public static int visitedSize;
    public static char[][] solutionGrid;

    private static Problem problem;
    public static Queue<TreeNode> nodes;

    public static void main(String [] args) {
        String result = solve( "5,5;1,2;3,1;0,2,1,1,2,1,2,2,4,0,4,1;0,3,3,0,3,2,3,4,4,3", "BF", false);
        System.out.println("Using Breadth First Search \n" + result + "\n");
        result = solve( "5,5;1,2;3,1;0,2,1,1,2,1,2,2,4,0,4,1;0,3,3,0,3,2,3,4,4,3", "DF", false);
        System.out.println("Using Depth First Search \n" + result + "\n");
        result = solve( "5,5;1,2;3,1;0,2,1,1,2,1,2,2,4,0,4,1;0,3,3,0,3,2,3,4,4,3", "UC", false);
        System.out.println("Using Uniform Cost Search \n" + result + "\n");
        result = solve( "5,5;1,2;3,1;0,2,1,1,2,1,2,2,4,0,4,1;0,3,3,0,3,2,3,4,4,3", "ID", false);
        System.out.println("Using Iterative Deepening Search \n" + result + "\n");
        result = solve( "5,5;1,2;3,1;0,2,1,1,2,1,2,2,4,0,4,1;0,3,3,0,3,2,3,4,4,3", "GR1", false);
        System.out.println("Using Greedy Search with heuristic function 1 \n" + result + "\n");
        result = solve( "5,5;1,2;3,1;0,2,1,1,2,1,2,2,4,0,4,1;0,3,3,0,3,2,3,4,4,3", "GR2", false);
        System.out.println("Using Greedy Search with heuristic function 2 \n" + result + "\n");
    }

    public static String solve(String grid, String strategy, boolean visualize){
        // Generate the 2d world array
        generateWorld(grid);
        // formulate the problem
        problem = generateProblem();
        // get the goal node
        goal = generalSearch(problem, strategy);
        if(goal==null){
            return "There is no solution";
        } else{
            // backtrack to get the plan, cost, number of nodes expanded
            int cost = goal.getCost();
            int expandedNodes = visitedSize;
            List<String> planList = new ArrayList<>();

            TreeNode node = goal;
            if(visualize){
                visualize(goal);
            }
            while(node.getParent()!=null){
                planList.add(node.getOperator().toString().toLowerCase());
                node = node.getParent();
            }
            Collections.reverse(planList);
            String plan = String.join(",", planList);
            return plan+";"+cost+";"+expandedNodes;
        }
    }

    public static void visualize(TreeNode goal){
        // we want to have a stack of strings that represent the states from initial state to the goal state following LIFO
        Stack<String> solution = new Stack<>();
        TreeNode node = goal;
        while(node.getParent()!=null){
            solution.push(State.getKey(node.getState()));
            node = node.getParent();
        }
        solutionGrid = new char [mGrid][nGrid];
        printArray(world);
        System.out.println("------------------------------------------------------------------------------------------");
//        each string is of the form ironman;thanos; stones seperated with -; warriors seperated with -
        while(!solution.isEmpty()){
            String s = solution.pop();
            // Grid Coordinates in part0
            String[] parts = s.split(";");

            // Iron man position in part0
            String [] IronMan = parts[0].split(",");
            int IronManX = Integer.parseInt(IronMan[0]);
            int IronManY = Integer.parseInt(IronMan[1]);
            solutionGrid[IronManX][IronManY] = 'I';

            // Thanos position in part1
            String [] Thanos = parts[1].split(",");
            int ThanosX = Integer.parseInt(Thanos[0]);
            int ThanosY = Integer.parseInt(Thanos[1]);
            if(ThanosX!= -10 && ThanosY!=-10){
                solutionGrid[ThanosX][ThanosY] = 'T';
            }

            int x,y;
            // Stones' positions in part2
            if(parts[2].length()!=0){
                String [] stones = parts[2].split("-");
                for(int i=0; i<stones.length;i++){
                    String [] stone = stones[i].split(",");
                    if(stone.length!=0){
                        x = Integer.parseInt(stone[0]);
                        y = Integer.parseInt(stone[1]);
                        solutionGrid [x][y] = 'S';
                    }
                }
            }

            // Warriors' positions in part3
            if(parts[3].length()!=0){
                String [] warriors = parts[3].split("-");
                if(warriors.length!=0){
                    for(int i=0; i<warriors.length;i++){
                        String [] warrior = warriors[i].split(",");
                        if(warrior.length!=0){
                            x = Integer.parseInt(warrior[0]);
                            y = Integer.parseInt(warrior[1]);
                            solutionGrid[x][y] = 'W';
                        }
                    }
                }
            }
            printArray(solutionGrid);
            clearArray(solutionGrid);
            System.out.println("------------------------------------------------------------------------------------------");
            }
        }

    public static void printArray(char[][] array){
        for(int i=0; i<array.length; i++){
            for(int j=0; j<array[i].length; j++){
                System.out.print(array[i][j]);
            }
            System.out.println();
        }
    }

    public static void clearArray(char[][]array){
        for(int i=0; i<array.length; i++){
            for(int j=0; j<array[i].length; j++){
                array[i][j]=' ';
            }
        }
    }

    public static void generateWorld(String grid){
        String[] parts = grid.split(";");

        // Grid Coordinates in part0
        String [] gridCoordinates = parts[0].split(",");
        mGrid = Integer.parseInt(gridCoordinates[0]);
        nGrid = Integer.parseInt(gridCoordinates[1]);
        world = new char [mGrid][nGrid];

        // Iron man position in part1
        String [] IronMan = parts[1].split(",");
        int IronManX = Integer.parseInt(IronMan[0]);
        int IronManY = Integer.parseInt(IronMan[1]);
        world[IronManX][IronManY] = 'I';

        // Thanos position in part2
        String [] Thanos = parts[2].split(",");
        int ThanosX = Integer.parseInt(Thanos[0]);
        int ThanosY = Integer.parseInt(Thanos[1]);
        world[ThanosX][ThanosY] = 'T';

        // Stones' positions in part3
        int x,y;
        String [] stones = parts[3].split(",");
        for(int i=0; i<stones.length;i+=2){
            x = Integer.parseInt(stones[i]);
            y = Integer.parseInt(stones[i+1]);
            world [x][y] = 'S';
        }

        // Warriors' positions in part4
        String [] warriors = parts[4].split(",");
        for(int i=0; i<warriors.length;i+=2){
            x = Integer.parseInt(warriors[i]);
            y = Integer.parseInt(warriors[i+1]);
            world [x][y] = 'W';
        }
    }

    public static Problem generateProblem(){
        int xIron=0, yIron=0, xThanos=0, yThanos=0;
        HashSet<String> stonesLocations = new HashSet<String>();
        HashSet<String> warriorsLocations = new HashSet<String>();


        for (int i = 0; i < mGrid; i++) {
            for (int j = 0; j < nGrid; j++) {
               switch (world[i][j]){
                   case 'I':
                       xIron = i;
                       yIron = j;
                       break;
                   case 'T':
                       xThanos = i;
                       yThanos = j;
                       break;
                   case 'S':
                       stonesLocations.add(i+","+j);
                       break;
                   case 'W':
                       warriorsLocations.add(i+","+j);
                       break;
                   case '\u0000':
                       break;
                   default:
               }
            }
        }
        String iron = xIron+","+yIron;
        String thanos = xThanos+","+yThanos;
        State initialState = new State(iron, thanos, stonesLocations, warriorsLocations);

        problem = new EndGame(initialState);
        return problem;
    }

    public static TreeNode generalSearch(Problem problem, String strategy){
        TreeNode.strategy = strategy;
        State initialState = problem.getInitialState();
        TreeNode root = makeNode(null, initialState, null, 0, 0);
        nodes = new LinkedList<>();
        visited = new HashMap<>();
        nodes.add(root);
        State rootState = root.getState();
        visited.put(State.getKey(rootState), rootState);

        while(true){
            if(nodes.isEmpty()){
                if(strategy.equals("ID")){
                    IDS_depth++;
                    // start over
                    nodes.removeAll(nodes);
                    visitedSize +=visited.size();
                    visited.clear();
                    nodes.add(root);
                } else {
                    return null;
                }
            } else {
                currentNode = nodes.remove();
                State currentNodeState = currentNode.getState();
                if(!visited.containsKey(State.getKey(currentNodeState))){
                    visited.put(State.getKey(currentNodeState), currentNodeState);
                }

                if(problem.goalTest(currentNodeState)){
                    visitedSize +=visited.size();
                    visited.clear();
                    nodes.clear();
                    return currentNode;
                } else {
                    Queue<TreeNode> expanded = expand(currentNode);
                    nodes = queuingFunction(nodes, expanded, strategy);
                }
            }
        }
    }

    public static TreeNode makeNode(TreeNode parent, State state, Operator operator, int cost, int depth){
        TreeNode node = new TreeNode(parent, state, operator, cost, depth);
        return node;
    }

    public static Queue<TreeNode> expand(TreeNode node){
        State state = node.getState();
        int cost = node.getCost();
        int depth = node.getDepth();
        int damage =0;

        Queue<TreeNode> nodes = new LinkedList<>();
        existVisited = false;


        // get all possible states from all operators
        for(Operator o: Operator.values()) {
            State newState = problem.transitionFunction(state, o);
            damage = problem.pathCost(state, o);
            TreeNode newNode = makeNode(node, newState, o, cost+damage, depth+1);

            existVisited = false;


            // make sure that the state doesn't exist in visited
            if(visited.containsKey(State.getKey(newState))){
                existVisited = true;
            }

            // Add node
            if (newNode.getCost()<100 && !existVisited) {
                nodes.add(newNode);
            }
        }
        return nodes;
    }

    public static Queue<TreeNode> queuingFunction(Queue<TreeNode> nodes, Queue<TreeNode> newNodes, String strategy){
        Queue<TreeNode> result = new LinkedList<>();
        switch(strategy){
            case "BF":
                result = BFS(nodes, newNodes);
                break;
            case "DF":
                result = DFS(nodes, newNodes);
                break;
            case "UC":
                result = UCS_GR_AS(nodes, newNodes);
                break;
            case "ID":
                result = IDS(nodes, newNodes);
                break;
            case "GR1":
                result = UCS_GR_AS(nodes, newNodes);
                break;
            case "GR2":
                result = UCS_GR_AS(nodes, newNodes);
                break;
            default:
        }
        return result;
    }

    public static Queue<TreeNode> BFS(Queue<TreeNode> nodes, Queue<TreeNode> newNodes){
        nodes.addAll(newNodes);
//        System.out.println("After Nodes = "+ nodes.size());
        return nodes;
    }

    public static Queue<TreeNode> DFS(Queue<TreeNode> nodes, Queue<TreeNode> newNodes){
        Queue<TreeNode> result = new LinkedList<>();
        result.addAll(newNodes);
        result.addAll(nodes);
        return result;
    }

    public static Queue<TreeNode> IDS(Queue<TreeNode> nodes, Queue<TreeNode> newNodes){
        // enqueue only if we have not reached the depth limit
        if(currentNode.getDepth()<IDS_depth){
            Queue<TreeNode> result = new LinkedList<>();
            result.addAll(newNodes);
            result.addAll(nodes);
            return result;
        }
        else{
            return nodes;
        }
    }

    public static Queue<TreeNode> UCS_GR_AS(Queue<TreeNode> nodes, Queue<TreeNode> newNodes){
        PriorityQueue<TreeNode> t = new PriorityQueue<TreeNode>();
        t.addAll(nodes);
        t.addAll(newNodes);
        return t;
    }
}
