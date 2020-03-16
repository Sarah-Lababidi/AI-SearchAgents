# AI-SearchAgents

Search Agents are the simplest type of rational agents. They take the search problem as input and produce a solution to this problem.
A search problem is defined as a 5-tuple:<br>

<li>An initial state.
<li>A set of operators that the agent can perform.
<li>A state space.
<li>A path cost function.
<li>A goal test function.

In this project: 
<li>The initial state is a 5x5 grid where each cell has either Ironman, Thanos, stone or a warrior.
<li>The agent (Ironman) can move up, down, left, right, collect a stone, kill a warrior and snap Thanos.
<li>The state space is represented as a transition function that takes the current state and an operator as inputs and produces the result state.
<li>The path cost function calculates the damage of Ironman where damage = 5 when Ironman is near Thanos, 1 when he is near a warrior, 
3 when he collects a stone.
<li>The goal is to kill Thanos after collecting all stones with a total received damage<100.
<br>
<br>
Five search algorithms were tested namely, Breadth-First Search, Depth-First Search, Iterative-Deepening Search, Uniform Cost Search and
Greedy Search. Then, the total numbers of expanded nodes were compared.

----------------------------------------------------------------------------------------------------------------------------------------------------
To test the algorithms, we call the method solve, where:
solve(String grid, String strategy, boolean visualize)
1. grid has the following format: m,n;ix,iy;tx,ty;
s1,x,s1y,s2x,s2y,s3x,s3y,s4x,s4y,s5x,s5y,s6x,s6y;
w1x,w1y,w2x,w2y,w3x,w3y,w4x,w4y,w5x,w5y
where
* m and n represent the width and height of the grid respectively.
* ix and iy represent the x and y starting positions of Iron Man.
* tx and ty represent the x and y positions of Thanos;
* six,siy represent the x and y position of stone si.
* wix,wiy represent the x and y position of warrior wi.
2. strategy is a string indicating the search strategy to be applied:
* BF for breadth-first search,
* DF for depth-first search,
* ID for iterative deepening search,
* UC for uniform cost search,
* GRi for greedy search, with i={1, 2} distinguishing the two heuristics.
3. visualize is a boolean parameter that provides a visual presentation of the grid when set to true.
 
 The method returns a a String of the following format: plan;cost;nodes
 
 Example: 
 `String result = solve( "5,5;1,2;3,1;0,2,1,1,2,1,2,2,4,0,4,1;0,3,3,0,3,2,3,4,4,3", "BF", false);
  System.out.println("Using Breadth First Search \n" + result + "\n");`
![a1](https://user-images.githubusercontent.com/38692962/76722233-63098d80-674b-11ea-8194-6679c7b64c65.png)
The output is: 
`Using Breadth First Search 
up,collect,down,down,collect,up,left,collect,down,collect,left,kill,down,down,collect,right,collect,up,snap;56;19896`

## Credits
Inroduction to Artificial Intelligence, The German University in Cairo

