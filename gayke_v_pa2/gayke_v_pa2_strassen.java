// Project 2 Question 1
//Implemention of Strassen’s matrix multiplication algorithm

import java.util.Random;

public class gayke_v_pa2_strassen {

    // Function for Strassen's matrix multiplication
    public static int[][] funStrassenMatrixMultiply(int[][] A, int[][] B) {
        int n = A.length;

        if (n >= 1) {
            // Use standard matrix multiplication, if the matrix size is small
            return funStandardMatrixMultiply(A, B);
        }

        int[][] C = new int[n][n];
        int m = n / 2;

        int[][] A11 = new int[m][m];
        int[][] A12 = new int[m][m];
        int[][] A21 = new int[m][m];
        int[][] A22 = new int[m][m];

        int[][] B11 = new int[m][m];
        int[][] B12 = new int[m][m];
        int[][] B21 = new int[m][m];
        int[][] B22 = new int[m][m];

        // Divide matrices A and B into four submatrices
        divideMatrix(A, A11, A12, A21, A22);
        divideMatrix(B, B11, B12, B21, B22);

        // Perform the Strassen's matrix multiplication algorithm
        // Calculate seven products using recursion
        /** Use these below formulas :
              M1 = (A11 + A22)(B11 + B22)
              M2 = (A21 + A22) B11
              M3 = A11 (B12 - B22)
              M4 = A22 (B21 - B11)
              M5 = (A11 + A12) B22
              M6 = (A21 - A11) (B11 + B12)
              M7 = (A12 - A22) (B21 + B22)
        **/
        int[][] P1 = funStrassenMatrixMultiply(addMatrix(A11, A22), addMatrix(B11, B22));
        int[][] P2 = funStrassenMatrixMultiply(addMatrix(A21, A22), B11);
        int[][] P3 = funStrassenMatrixMultiply(A11, subtractMatrix(B12, B22));
        int[][] P4 = funStrassenMatrixMultiply(A22, subtractMatrix(B21, B11));
        int[][] P5 = funStrassenMatrixMultiply(addMatrix(A11, A12), B22);
        int[][] P6 = funStrassenMatrixMultiply(subtractMatrix(A21, A11), addMatrix(B11, B12));
        int[][] P7 = funStrassenMatrixMultiply(subtractMatrix(A12, A22), addMatrix(B21, B22));

        // Calculate the four submatrices of the result
        /** By using below formulas:
              C11 = M1 + M4 - M5 + M7
              C12 = M3 + M5
              C21 = M2 + M4
              C22 = M1 - M2 + M3 + M6
        **/
        int[][] C11 = subtractMatrix(addMatrix(addMatrix(P1, P4), P7), P5);
        int[][] C12 = addMatrix(P3, P5);
        int[][] C21 = addMatrix(P2, P4);
        int[][] C22 = subtractMatrix(subtractMatrix(addMatrix(P1, P3), P2), P6);

        // Combine the four submatrices into the result matrix C
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                C[i][j] = C11[i][j];
                C[i][j + m] = C12[i][j];
                C[i + m][j] = C21[i][j];
                C[i + m][j + m] = C22[i][j];
            }
        }

        return C;
    }

    // Function for standard matrix multiplication
    public static int[][] funStandardMatrixMultiply(int[][] A, int[][] B) {
        int n = A.length;
        int[][] C = new int[n][n];

        // Perform standard matrix multiplication
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = 0;
                for (int k = 0; k < n; k++) {
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }

        return C;
    }

    // Define matrix utility functions
    // Function for addition of two matrices  
    public static int[][] addMatrix(int[][] A, int[][] B) {
        int n = A.length;
        int[][] D = new int[n][n];

        // Add two matrices element-wise
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                D[i][j] = A[i][j] + B[i][j];
            }
        }

        return D;
    }

    // Function for substraction of two matrices  
    public static int[][] subtractMatrix(int[][] A, int[][] B) {
        int n = A.length;
        int[][] D = new int[n][n];

        // Subtract one matrix from another element-wise
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                D[i][j] = A[i][j] - B[i][j];
            }
        }

        return D;
    }

    // Function for splitting the matrix into smaller matrices 
    public static void divideMatrix(int[][] A, int[][] A11, int[][] A12, int[][] A21, int[][] A22) {
        int n = A.length;
        int m = n / 2;

        // divide a matrix into four submatrices
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                A11[i][j] = A[i][j];
                A12[i][j] = A[i][j + m];
                A21[i][j] = A[i + m][j];
                A22[i][j] = A[i + m][j + m];
            }
        }
    }

    // Check the condition if the given number is power of two
    public static boolean powerOfTwo(int n) {
        if (n <= 0) {
            return false;
        }

        // Check if a number is a power of two
        while (n > 1) {
            if (n % 2 != 0) {
                return false;
            }
            n = n / 2;
        }

        return true;
    }

    // Generate random integer matrix of n*n 
    private static int[][] generateRandomIntegerMatrix(int n, int max) {
        int[][] arr = new int[n][n];
        Random ran = new Random();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                arr[i][j] = ran.nextInt(max); // Maximum random integer value
            }
        }
        return arr;
    }

    // Print the matrix
    private static void printMatrix(int[][] mat) {
        int n = mat.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(mat[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static boolean equalMatrices(int[][] A, int[][] B) {
        int n = A.length;

        if (n != B.length || A[0].length != B[0].length) {
            return false;
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (A[i][j] != B[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    public static void main(String[] args) {
        
        if (args.length != 1) {
            System.out.println("Please ensure that input follows the format: java <class_name> <matrix_size>");
            return;
        }

        // for maximum integer value
        int maxInt = Integer.MAX_VALUE;
        int n = Integer.parseInt(args[0]);

        int maximumnum = (int) Math.floor(Math.sqrt(maxInt / n));
        
        // Check input variable is power of two or not
        if (!powerOfTwo(n) || n < 1 || n > 1024) {
            System.out.println("Matrix size (n) must be a power of 2 between 1 and 1024.");
            return;
        }

        // generate random integer matrix for matrix A and B 
        int A[][] = generateRandomIntegerMatrix(n, maximumnum);
        int B[][] = generateRandomIntegerMatrix(n, maximumnum);

        int[][] standardMatrix = funStandardMatrixMultiply(A, B);
        int[][] strassenMatrix = funStrassenMatrixMultiply(A, B);

        System.out.println("A=");
        printMatrix(A);

        System.out.println("\nB=");
        printMatrix(B);

        System.out.println("\nThe standard matrix multiplication A*B =\n");
        printMatrix(standardMatrix);
    
        System.out.println("\nThe Strassens multiplication A*B =\n");
        printMatrix(strassenMatrix);

        // Check if the resultant matrices are same
        if (equalMatrices(standardMatrix, strassenMatrix)) {
            System.out.println("\nBoth the results are same!");
        } else {
            System.out.println("\nResults are not same!");
        }
    }
}
