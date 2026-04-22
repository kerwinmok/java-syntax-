package datastructures.search;

import java.util.*;

/**
 * SEARCH ALGORITHMS - BFS and DFS
 *
 * Both BFS and DFS are graph/tree traversal algorithms.
 *
 * BFS (Breadth-First Search):
 *   - Explores all neighbors at current depth before going deeper
 *   - Uses a QUEUE
 *   - Finds SHORTEST PATH in unweighted graphs
 *   - Time: O(V + E), Space: O(V)
 *
 * DFS (Depth-First Search):
 *   - Explores as far as possible along each branch before backtracking
 *   - Uses a STACK (or recursion)
 *   - Good for: cycle detection, topological sort, connected components, maze solving
 *   - Time: O(V + E), Space: O(V)
 *
 * Where V = vertices, E = edges
 */
public class SearchAlgorithms {

    // =========================================================================
    // GRAPH REPRESENTATION: Adjacency List
    // =========================================================================
    static class Graph {
        private final int vertices;
        private final List<List<Integer>> adjList;
        private final boolean isDirected;

        public Graph(int vertices, boolean isDirected) {
            this.vertices = vertices;
            this.isDirected = isDirected;
            this.adjList = new ArrayList<>();
            for (int i = 0; i < vertices; i++) adjList.add(new ArrayList<>());
        }

        public void addEdge(int u, int v) {
            adjList.get(u).add(v);
            if (!isDirected) adjList.get(v).add(u);
        }

        public List<Integer> getNeighbors(int v) { return adjList.get(v); }
        public int getVertices() { return vertices; }
    }

    // =========================================================================
    // BFS IMPLEMENTATIONS
    // =========================================================================
    static class BFS {

        // --- BFS 1: Basic traversal - returns visit order ---
        public static List<Integer> traverse(Graph g, int start) {
            List<Integer> visited = new ArrayList<>();
            boolean[] seen = new boolean[g.getVertices()];
            Queue<Integer> queue = new LinkedList<>();

            seen[start] = true;
            queue.offer(start);

            while (!queue.isEmpty()) {
                int node = queue.poll();
                visited.add(node);
                for (int neighbor : g.getNeighbors(node)) {
                    if (!seen[neighbor]) {
                        seen[neighbor] = true;
                        queue.offer(neighbor);
                    }
                }
            }
            return visited;
        }

        // --- BFS 2: Shortest path (unweighted graph) ---
        // Returns distance array: distance[i] = shortest steps from start to i
        public static int[] shortestPath(Graph g, int start) {
            int n = g.getVertices();
            int[] dist = new int[n];
            Arrays.fill(dist, -1); // -1 = unreachable
            int[] prev = new int[n]; // to reconstruct path
            Arrays.fill(prev, -1);

            Queue<Integer> queue = new LinkedList<>();
            dist[start] = 0;
            queue.offer(start);

            while (!queue.isEmpty()) {
                int node = queue.poll();
                for (int neighbor : g.getNeighbors(node)) {
                    if (dist[neighbor] == -1) {
                        dist[neighbor] = dist[node] + 1;
                        prev[neighbor] = node;
                        queue.offer(neighbor);
                    }
                }
            }
            return dist;
        }

        // Reconstruct path from shortestPath result
        public static List<Integer> getPath(int[] prev, int end) {
            List<Integer> path = new ArrayList<>();
            for (int v = end; v != -1; v = prev[v]) path.add(0, v);
            return path;
        }

        // --- BFS 3: Level-by-level traversal ---
        public static List<List<Integer>> levelOrder(Graph g, int start) {
            List<List<Integer>> levels = new ArrayList<>();
            boolean[] seen = new boolean[g.getVertices()];
            Queue<Integer> queue = new LinkedList<>();

            seen[start] = true;
            queue.offer(start);

            while (!queue.isEmpty()) {
                int levelSize = queue.size();
                List<Integer> level = new ArrayList<>();
                for (int i = 0; i < levelSize; i++) {
                    int node = queue.poll();
                    level.add(node);
                    for (int neighbor : g.getNeighbors(node)) {
                        if (!seen[neighbor]) {
                            seen[neighbor] = true;
                            queue.offer(neighbor);
                        }
                    }
                }
                levels.add(level);
            }
            return levels;
        }

        // --- BFS 4: Detect bipartite graph (2-coloring) ---
        public static boolean isBipartite(Graph g) {
            int[] color = new int[g.getVertices()];
            Arrays.fill(color, -1);

            for (int start = 0; start < g.getVertices(); start++) {
                if (color[start] != -1) continue; // already colored
                Queue<Integer> queue = new LinkedList<>();
                color[start] = 0;
                queue.offer(start);

                while (!queue.isEmpty()) {
                    int node = queue.poll();
                    for (int neighbor : g.getNeighbors(node)) {
                        if (color[neighbor] == -1) {
                            color[neighbor] = 1 - color[node]; // alternate color
                            queue.offer(neighbor);
                        } else if (color[neighbor] == color[node]) {
                            return false; // same color = not bipartite
                        }
                    }
                }
            }
            return true;
        }

        // --- BFS 5: Find connected components ---
        public static int countComponents(Graph g) {
            boolean[] seen = new boolean[g.getVertices()];
            int components = 0;

            for (int i = 0; i < g.getVertices(); i++) {
                if (!seen[i]) {
                    components++;
                    Queue<Integer> queue = new LinkedList<>();
                    seen[i] = true;
                    queue.offer(i);
                    while (!queue.isEmpty()) {
                        int node = queue.poll();
                        for (int neighbor : g.getNeighbors(node)) {
                            if (!seen[neighbor]) {
                                seen[neighbor] = true;
                                queue.offer(neighbor);
                            }
                        }
                    }
                }
            }
            return components;
        }

        // --- BFS 6: Matrix BFS (e.g., shortest path in grid) ---
        // Find shortest path from top-left to bottom-right in binary matrix
        // 0 = open, 1 = blocked
        public static int shortestPathInMatrix(int[][] grid) {
            int rows = grid.length;
            int cols = grid[0].length;
            if (grid[0][0] == 1 || grid[rows-1][cols-1] == 1) return -1;

            int[][] dirs = {{0,1},{1,0},{0,-1},{-1,0},{1,1},{1,-1},{-1,1},{-1,-1}};
            boolean[][] visited = new boolean[rows][cols];
            Queue<int[]> queue = new LinkedList<>();
            queue.offer(new int[]{0, 0, 1}); // {row, col, distance}
            visited[0][0] = true;

            while (!queue.isEmpty()) {
                int[] curr = queue.poll();
                int r = curr[0], c = curr[1], dist = curr[2];
                if (r == rows-1 && c == cols-1) return dist;
                for (int[] dir : dirs) {
                    int nr = r + dir[0];
                    int nc = c + dir[1];
                    if (nr >= 0 && nr < rows && nc >= 0 && nc < cols
                        && !visited[nr][nc] && grid[nr][nc] == 0) {
                        visited[nr][nc] = true;
                        queue.offer(new int[]{nr, nc, dist + 1});
                    }
                }
            }
            return -1; // no path
        }
    }

    // =========================================================================
    // DFS IMPLEMENTATIONS
    // =========================================================================
    static class DFS {

        // --- DFS 1: Recursive DFS traversal ---
        public static List<Integer> traverseRecursive(Graph g, int start) {
            List<Integer> visited = new ArrayList<>();
            boolean[] seen = new boolean[g.getVertices()];
            dfsRec(g, start, seen, visited);
            return visited;
        }

        private static void dfsRec(Graph g, int node, boolean[] seen, List<Integer> visited) {
            seen[node] = true;
            visited.add(node);
            for (int neighbor : g.getNeighbors(node)) {
                if (!seen[neighbor]) dfsRec(g, neighbor, seen, visited);
            }
        }

        // --- DFS 2: Iterative DFS (using explicit stack) ---
        public static List<Integer> traverseIterative(Graph g, int start) {
            List<Integer> visited = new ArrayList<>();
            boolean[] seen = new boolean[g.getVertices()];
            Deque<Integer> stack = new ArrayDeque<>();

            stack.push(start);
            while (!stack.isEmpty()) {
                int node = stack.pop();
                if (seen[node]) continue;
                seen[node] = true;
                visited.add(node);
                // Push in reverse order to maintain same order as recursive
                List<Integer> neighbors = g.getNeighbors(node);
                for (int i = neighbors.size() - 1; i >= 0; i--) {
                    if (!seen[neighbors.get(i)]) stack.push(neighbors.get(i));
                }
            }
            return visited;
        }

        // --- DFS 3: Cycle detection in directed graph ---
        public static boolean hasCycleDirected(Graph g) {
            boolean[] visited = new boolean[g.getVertices()];
            boolean[] inStack = new boolean[g.getVertices()]; // currently in recursion stack

            for (int i = 0; i < g.getVertices(); i++) {
                if (!visited[i] && dfsCycleDirected(g, i, visited, inStack)) return true;
            }
            return false;
        }

        private static boolean dfsCycleDirected(Graph g, int node, boolean[] visited, boolean[] inStack) {
            visited[node] = true;
            inStack[node] = true;

            for (int neighbor : g.getNeighbors(node)) {
                if (!visited[neighbor] && dfsCycleDirected(g, neighbor, visited, inStack)) return true;
                if (inStack[neighbor]) return true; // back edge = cycle
            }

            inStack[node] = false; // remove from recursion stack
            return false;
        }

        // --- DFS 4: Topological Sort (only for DAGs) ---
        public static List<Integer> topologicalSort(Graph g) {
            boolean[] visited = new boolean[g.getVertices()];
            Deque<Integer> stack = new ArrayDeque<>();

            for (int i = 0; i < g.getVertices(); i++) {
                if (!visited[i]) topoSortDFS(g, i, visited, stack);
            }

            List<Integer> order = new ArrayList<>(stack);
            return order;
        }

        private static void topoSortDFS(Graph g, int node, boolean[] visited, Deque<Integer> stack) {
            visited[node] = true;
            for (int neighbor : g.getNeighbors(node)) {
                if (!visited[neighbor]) topoSortDFS(g, neighbor, visited, stack);
            }
            stack.push(node); // push after all descendants processed
        }

        // --- DFS 5: Find all paths from source to destination ---
        public static List<List<Integer>> findAllPaths(Graph g, int src, int dest) {
            List<List<Integer>> allPaths = new ArrayList<>();
            boolean[] visited = new boolean[g.getVertices()];
            List<Integer> currentPath = new ArrayList<>();
            currentPath.add(src);
            findAllPathsDFS(g, src, dest, visited, currentPath, allPaths);
            return allPaths;
        }

        private static void findAllPathsDFS(Graph g, int curr, int dest,
                boolean[] visited, List<Integer> path, List<List<Integer>> allPaths) {
            if (curr == dest) {
                allPaths.add(new ArrayList<>(path));
                return;
            }
            visited[curr] = true;
            for (int neighbor : g.getNeighbors(curr)) {
                if (!visited[neighbor]) {
                    path.add(neighbor);
                    findAllPathsDFS(g, neighbor, dest, visited, path, allPaths);
                    path.remove(path.size() - 1); // backtrack
                }
            }
            visited[curr] = false; // allow revisiting via different paths
        }

        // --- DFS 6: Strongly Connected Components (Kosaraju's Algorithm) ---
        public static List<List<Integer>> stronglyConnectedComponents(int n, List<List<Integer>> adj) {
            boolean[] visited = new boolean[n];
            Deque<Integer> finishOrder = new ArrayDeque<>();

            // Step 1: DFS on original graph, push to stack by finish time
            for (int i = 0; i < n; i++) {
                if (!visited[i]) dfsFinishOrder(adj, i, visited, finishOrder);
            }

            // Step 2: Build transposed graph
            List<List<Integer>> transposed = new ArrayList<>();
            for (int i = 0; i < n; i++) transposed.add(new ArrayList<>());
            for (int u = 0; u < n; u++) {
                for (int v : adj.get(u)) transposed.get(v).add(u);
            }

            // Step 3: DFS on transposed graph in reverse finish order
            Arrays.fill(visited, false);
            List<List<Integer>> sccs = new ArrayList<>();
            while (!finishOrder.isEmpty()) {
                int node = finishOrder.pop();
                if (!visited[node]) {
                    List<Integer> scc = new ArrayList<>();
                    dfsCollect(transposed, node, visited, scc);
                    sccs.add(scc);
                }
            }
            return sccs;
        }

        private static void dfsFinishOrder(List<List<Integer>> adj, int node,
                boolean[] visited, Deque<Integer> stack) {
            visited[node] = true;
            for (int neighbor : adj.get(node)) {
                if (!visited[neighbor]) dfsFinishOrder(adj, neighbor, visited, stack);
            }
            stack.push(node);
        }

        private static void dfsCollect(List<List<Integer>> adj, int node,
                boolean[] visited, List<Integer> component) {
            visited[node] = true;
            component.add(node);
            for (int neighbor : adj.get(node)) {
                if (!visited[neighbor]) dfsCollect(adj, neighbor, visited, component);
            }
        }
    }

    // =========================================================================
    // DEMO / MAIN
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== SEARCH ALGORITHMS (BFS & DFS) ===\n");

        // Build undirected graph:
        //  0 --- 1 --- 2
        //  |     |
        //  3 --- 4 --- 5
        Graph g = new Graph(6, false);
        g.addEdge(0, 1); g.addEdge(0, 3);
        g.addEdge(1, 2); g.addEdge(1, 4);
        g.addEdge(3, 4); g.addEdge(4, 5);

        // ---- BFS ----
        System.out.println("=== BFS ===");
        System.out.println("BFS traversal from 0: " + BFS.traverse(g, 0));

        int[] dists = BFS.shortestPath(g, 0);
        System.out.println("Shortest distances from 0: " + Arrays.toString(dists));

        List<List<Integer>> levels = BFS.levelOrder(g, 0);
        System.out.println("Level-order from 0: " + levels);

        System.out.println("Is bipartite? " + BFS.isBipartite(g));

        // Non-bipartite (odd cycle: triangle)
        Graph triangle = new Graph(3, false);
        triangle.addEdge(0, 1); triangle.addEdge(1, 2); triangle.addEdge(0, 2);
        System.out.println("Triangle is bipartite? " + BFS.isBipartite(triangle));

        // Disconnected graph (2 components)
        Graph disconnected = new Graph(5, false);
        disconnected.addEdge(0, 1); disconnected.addEdge(1, 2);
        disconnected.addEdge(3, 4);
        System.out.println("Connected components: " + BFS.countComponents(disconnected));

        System.out.println("\n[Matrix BFS - shortest path in grid]");
        int[][] grid = {
            {0, 0, 0},
            {1, 1, 0},
            {0, 0, 0}
        };
        System.out.println("Shortest path top-left to bottom-right: " + BFS.shortestPathInMatrix(grid));

        // ---- DFS ----
        System.out.println("\n=== DFS ===");
        System.out.println("DFS recursive from 0:  " + DFS.traverseRecursive(g, 0));
        System.out.println("DFS iterative from 0:  " + DFS.traverseIterative(g, 0));

        // Cycle detection in directed graph
        Graph dag = new Graph(5, true);
        dag.addEdge(0, 1); dag.addEdge(0, 2);
        dag.addEdge(1, 3); dag.addEdge(2, 3);
        dag.addEdge(3, 4);
        System.out.println("\nDAG has cycle? " + DFS.hasCycleDirected(dag));  // false

        Graph cyclic = new Graph(4, true);
        cyclic.addEdge(0, 1); cyclic.addEdge(1, 2);
        cyclic.addEdge(2, 3); cyclic.addEdge(3, 1); // back edge
        System.out.println("Cyclic graph has cycle? " + DFS.hasCycleDirected(cyclic)); // true

        // Topological sort
        System.out.println("\nTopological sort of DAG: " + DFS.topologicalSort(dag));

        // All paths
        System.out.println("\nAll paths from 0 to 4 in DAG: " + DFS.findAllPaths(dag, 0, 4));

        // Strongly Connected Components
        System.out.println("\n[Strongly Connected Components - Kosaraju's]");
        int n = 5;
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        adj.get(1).add(0);
        adj.get(0).add(2);
        adj.get(2).add(1);
        adj.get(0).add(3);
        adj.get(3).add(4);
        List<List<Integer>> sccs = DFS.stronglyConnectedComponents(n, adj);
        System.out.println("SCCs: " + sccs);
    }
}
