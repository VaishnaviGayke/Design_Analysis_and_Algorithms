public class lcs {
    
    // Function to construct the LCS using the direction matrix
    static String constructLCS(String s1, int i, int j) {
        if (i == 0 || j == 0) {
            return "";
        }
        if (directionMatrix[i][j] == 'D') {
            return constructLCS(s1, i - 1, j - 1) + s1.charAt(i - 1);
        } else if (directionMatrix[i][j] == 'U') {
            return constructLCS(s1, i - 1, j);
        } else {
            return constructLCS(s1, i, j - 1);
        }
    }

    static int[][] lengthMatrix;  // Matrix to store the length of the LCS
    static char[][] directionMatrix;  // Matrix to store the direction of the LCS

    // Function to find the length of the LCS and fill the matrices
    static void findlcsLength(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();

        lengthMatrix = new int[m + 1][n + 1];  // Initialize the matrix for lengths
        directionMatrix = new char[m + 1][n + 1];  // Initialize the matrix for directions

        // build the matrices using dynamic programming
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    lengthMatrix[i][j] = lengthMatrix[i - 1][j - 1] + 1;  // Diagonal + 1
                    directionMatrix[i][j] = 'D';  // Diagonal direction
                } else if (lengthMatrix[i - 1][j] >= lengthMatrix[i][j - 1]) {
                    lengthMatrix[i][j] = lengthMatrix[i - 1][j];  // Move up
                    directionMatrix[i][j] = 'U';  // Up diretion
                } else {
                    lengthMatrix[i][j] = lengthMatrix[i][j - 1];  // Move left
                    directionMatrix[i][j] = 'L';  // Left direction
                }
            }
        }
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java LCS <input-string1> <input-string2>");
            System.exit(1);
        }

        // Get input strings from command line arguments
        String s1 = args[0];
        String s2 = args[1];

        // Find the length and direction matrix using LCS algorithm
        findlcsLength(s1, s2);

        // Print the length of LCS
        System.out.println("Length of LCS: " + lengthMatrix[s1.length()][s2.length()]);

        // Check the strings are empty and print the LCS
        if (s1.isEmpty() && s2.isEmpty()) {
            System.out.println("Both input strings are empty.");
        } else if(s1.isEmpty() || s2.isEmpty()){
            System.out.println("One of the input string is empty.");
        }else {
            System.out.println("LCS: " + constructLCS(s1, s1.length(), s2.length()));
        }
    }
}
