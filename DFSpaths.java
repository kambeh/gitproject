import java.util.Arrays;
import java.util.Scanner;
//import org.apache.commons.lang3.StringUtils;

/*************************************************************************
 *  Compilation:  javac DFSpaths.java
 *  Execution:    java DFSpaths G s
 *  Dependencies: EdgeWeightedGraph.java Stack.java StdOut.java
 *  Data files:   http://algs4.cs.princeton.edu/41undirected/tinyCG.txt
 *
 *  Run depth first search on an undirected EdgeWeightedGraph.
 *  Runs in O(E + V) time.
 *
 *  %  java DFSpaths.java tinyCG.txt
 *  6 8
 *  0: 2 1 5 
 *  1: 0 2 
 *  2: 0 1 3 4 
 *  3: 5 4 2 
 *  4: 3 2 
 *  5: 3 0 
 *
 *  % java DFSpaths tinyCG.txt 0
 *  0 to 0:  0
 *  0 to 1:  0-2-1
 *  0 to 2:  0-2
 *  0 to 3:  0-2-3
 *  0 to 4:  0-2-3-4
 *  0 to 5:  0-2-3-5
 *
 *************************************************************************/

/**
 *  The <tt>DFSpaths</tt> class represents a data type for finding
 *  paths from a source vertex <em>s</em> to every other vertex
 *  in an undirected EdgeWeightedGraph.
 *  <p>
 *  This implementation uses depth-first search.
 *  The constructor takes time proportional to <em>V</em> + <em>E</em>,
 *  where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 *  It uses extra space (not including the EdgeWeightedGraph) proportional to <em>V</em>.
 *  <p>
 *  For additional documentation, see <a href="/algs4/41graph">Section 4.1</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *  
 *  @program modified from DepthFirstPaths.java
 *  @at http://algs4.cs.princeton.edu/41undirected/
 *  @modified by Kam Beh
 *  @last modified May, 2015
 */
public class DFSpaths {
    private boolean[] marked;    // marked[v] = is there an s-v path?
    private int[] edgeTo;        // edgeTo[v] = last edge on s-v path
    private double[] cost;		 // store cost of each edge
    private final int s;         // source vertex
    protected static final String lookup[] = {"A","B","C","D","E","F"};		//simple loop up table

    /**
     * Computes a path between <tt>s</tt> and every other vertex in EdgeWeightedGraph <tt>G</tt>.
     * @param G the EdgeWeightedGraph
     * @param s the source vertex
     */
    public DFSpaths(EdgeWeightedGraph G, int s) {
        this.s = s;
        edgeTo = new int[G.V()];
        marked = new boolean[G.V()];
        cost = new double[G.E()];
        dfs(G, s);
    }

    // depth first search from v
    private void dfs(EdgeWeightedGraph G, int vertex) {
        marked[vertex] = true;
        for (Edge e : G.adj(vertex)) {
            int v = e.either();
            int w = e.other(v);
            double wt = e.weight();
            
            if (!marked[w]) {
                edgeTo[w] = v;
                cost[w] = wt;
                dfs(G, w);            
            }
        }
    }

    /**
     * Is there a path between the source vertex <tt>s</tt> and vertex <tt>v</tt>?
     * @param v the vertex
     * @return <tt>true</tt> if there is a path, <tt>false</tt> otherwise
     */
    public boolean hasPathTo(int v) {
        return marked[v];
    }

    /**
     * Returns a path between the source vertex <tt>s</tt> and vertex <tt>v</tt>, or
     * <tt>null</tt> if no such path.
     * @param v the vertex
     * @return the sequence of vertices on a path between the source vertex
     *   <tt>s</tt> and vertex <tt>v</tt>, as an Iterable
     */
    public Iterable<Integer> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<Integer> path = new Stack<Integer>();
        for (int x = v; x != s; x = edgeTo[x])
            path.push(x);
        path.push(s);
        return path;
    }
    
    public double[] getCost()
    {
    	return cost;
    }
    public double getCostbyEdge(int numEdge, int edge)
    {
    	if(edge < numEdge){
    		return cost[edge];
    	}
    	return 0;
    }
    
    public void prtPath(String from, String to, double dist) {
    	System.out.printf("From %s to %s (%.2f)", from, to, dist);
    }
    public static void prtNoPath(String from, String to) {
    	System.out.printf("%s to %s has no path\n", from, to);
    }
    public void prtRoutes(Iterable<Integer> itr, int from, int to, int numedges, double weight) {
        for (int u : itr) {
            if (u == s) StdOut.print(lookup[u]);
            else        StdOut.print(" => " + lookup[u]);
        }
        StdOut.printf(" => %d\n", (int)weight); 	
    }
    public double distTo(Iterable<Integer> itr, int numedges) {
    	double distance=0;
    	
    	for (int u : itr) {
    		distance += getCostbyEdge(numedges, u);    		
    	}
        return distance;
    }    

    /**
     * Unit tests the <tt>DFSpaths</tt> data type.
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedGraph G = new EdgeWeightedGraph(in);
        DFSpaths dfs;	//depth first search

        int x = 0;
        int y = 0;
        int latency = 0;
        int count = 2;
        double distance = 0;
        boolean isValid = true;
    	String from = "", to = "", line = "";
    	String[] str = new String[10];
        
    	Scanner scanner = new Scanner(System.in);
    	line = scanner.nextLine();
    	
    	while(!line.equals("quit") && !line.isEmpty()){
	        try { 
	        	//count = StringUtils.countMatches(line, " ");
	        	if(count != 2){
	        		System.out.println("Invalid input format.");
	        	}
	        	else{
		        	str = line.split(" ");
		        	
		        	from = str[0].toUpperCase();
		        	to = str[1].toUpperCase();
		        	
		        	try {
		        		latency = Integer.parseInt(str[2]);
		        		isValid = true;
		        	} catch (NumberFormatException e) {
		        	    System.out.println("Invalid input format.");
		        	    isValid = false;
		        	}
		        	
	        	    x = Arrays.asList(lookup).indexOf(from);
	        	    y = Arrays.asList(lookup).indexOf(to);		        	

					if(x >= 0 && y >= 0 && isValid){
						dfs = new DFSpaths(G, x);
						distance=0;
					
				        if (dfs.hasPathTo(y)) {
				        	distance = dfs.distTo(dfs.pathTo(y), G.E());
				        	if(latency >= distance){
				                //print path from x to y
				        		dfs.prtRoutes(dfs.pathTo(y), x, y, G.E(), distance);
				        	}
				        	else {
				        		System.out.println("Path not found.");
				        	}
				        }
				        else {
				        	//has no path from x to y for valid input
				        	prtNoPath(from, to);
				        }
					}
					else{
						//has no path from x to y for in-valid input (e.g. out of range)
						prtNoPath(from, to);
					}
	        	}
	        	line = scanner.nextLine();
	        }
	        catch (Exception e) { 
	        	line = null;
	        }
    	}
        
    	scanner.close();        
    }

}