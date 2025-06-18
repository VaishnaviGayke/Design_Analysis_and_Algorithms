import java.io.*;

public class floyd {

    // constant to represent infinity for initialization of distance matrix
    static final int INF = Integer.MAX_VALUE;

    // Floyd's Algorithm to find the shortest paths between all pairs of vertices
    public static void funFloyd(int[][] graph, int n, PrintWriter outputFile) {
        // Create matrices to store the distance and predecessor information
        int[][] dist = new int[n][n];
        int[][] P = new int[n][n];

        // Copy the input graph to the distance matrix and initialize predecessor matrix
        for (int i = 0; i < n; i++) {
            System.arraycopy(graph[i], 0, dist[i], 0, n);
            for (int j = 0; j < n; j++) {
                P[i][j] = 0;
            }
        }

        // Floyd's Algorithm
        for (int k = 0; k < n; k++) {
            int[][] updatedDist = new int[n][n];

            // Computing D’ from D
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][j] >= dist[i][k] + dist[k][j]) {
                        updatedDist[i][j] = dist[i][k] + dist[k][j];
                        if(i != k && j !=k){
                        P[i][j] = k + 1; // Set predecessor information
                        }
                    } else {
                        updatedDist[i][j] = dist[i][j];
                    }
                }
            }

            // Move D’ to D
            for (int i = 0; i < n; i++) {
                System.arraycopy(updatedDist[i], 0, dist[i], 0, n);
            }
        }

        // Print P matrix
        outputFile.println("P matrix:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                outputFile.print(P[i][j] + "\t");
            }
            outputFile.println();
        }outputFile.println();

        // Print shortest paths and lengths
        for (int i = 0; i < n; i++) {
            outputFile.println("V" + (i + 1) + "-Vj: shortest path and length");
            for (int j = 0; j < n; j++) {
                if (P[i][j] != 0) {
                    outputFile.print("V" + (i + 1));
                    path(i, j, P, outputFile);    
                    outputFile.print("\tV" + (j + 1) + ": " + dist[i][j]);
                    outputFile.println();
                } else {
                    outputFile.println("V" + (i + 1) + "\tV" + (j + 1) + ": " + dist[i][j]);
                }
            }    
            outputFile.println();
        }
        outputFile.println();
    }

    // Recursive method to print the path between two vertices
    public static void path(int q, int r, int[][] P, PrintWriter outputFile) {
        if (P[q][r] != 0) {
            path(q, P[q][r] - 1, P, outputFile);
            outputFile.print("\tV" + P[q][r]);
            // path(q, P[q][r - 1], P, outputFile);
            path(P[q][r] - 1 , r, P, outputFile);
        }
    }

    // Main method to read input, run Floyd's algorithm, and write results to output.txt
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Floyd <graph-file>");
            System.exit(1);
        }

        try (BufferedReader inputFile = new BufferedReader(new FileReader(args[0]));
            PrintWriter outputFile = new PrintWriter("output.txt")) {

            int problemNum = 1;
            String line;
            while ((line = inputFile.readLine()) != null) {
                if (line.matches("Problem \\d+: n = \\d+")) {
                    String[] tokens = line.split("\\s+");
                    if (tokens.length >= 5) {
                        int n = Integer.parseInt(tokens[tokens.length - 1]);
                        int[][] graph = new int[n][n];

                        // Read the graph matrix
                        for (int i = 0; i < n; i++) {
                            line = inputFile.readLine();
                            if (line != null) {
                                String[] values = line.trim().split("\\s+");
                                if (values.length == n) {
                                    for (int j = 0; j < n; j++) {
                                        graph[i][j] = Integer.parseInt(values[j]);
                                    }
                                } else {
                                    System.err.println("Error: Incorrect number of values in graph matrix for problem " + problemNum);
                                    System.exit(1);
                                }
                            } else {
                                System.err.println("Error: Incomplete graph matrix for problem " + problemNum);
                                System.exit(1);
                            }
                        }

                        outputFile.println("Problem " + problemNum + ": n = " + n);
                        funFloyd(graph, n, outputFile);

                        problemNum++;
                    } else {
                        System.err.println("Error: Invalid format for problem information");
                        System.exit(1);
                    }
                }
            }

            System.out.println("Please review the results in the output.txt file.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
