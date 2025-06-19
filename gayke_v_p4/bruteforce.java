import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class bruteforce {

    // Method to solve the Knapsack problem using a brute-force approach
    public static void bruteForceKnapsack(String inputFileName, String outputFileName) {
        try {
            // Read the input file containing Knapsack problem details
            List<String> lines = Files.readAllLines(Paths.get(inputFileName));
            String[] firstLine = lines.get(0).split(" ");
            int n = Integer.parseInt(firstLine[0]); // Number of items
            int capacity = Integer.parseInt(firstLine[1]); // Capacity of the knapsack

            // Create a 2D array to store item profits, weights, and item numbers
            int[][] items = new int[n][3];

            // Extract item profits, weights, and assign item numbers
            for (int i = 0; i < n; i++) {
                String[] parts = lines.get(i + 1).split(" ");
                items[i][0] = Integer.parseInt(parts[1]); // Profit
                items[i][1] = Integer.parseInt(parts[2]); // Weight
                items[i][2] = i + 1; // Item number
            }

            // variables to store the best combination's details
            List<Integer> set = new ArrayList<>();
            int maxProfit = -1;
            int maxWeight = -1;

            // Brute force approach to find the best combination of items
            for (int i = 0; i < (1 << n); i++) {
                List<Integer> currSet = new ArrayList<>();
                int currProfit = 0;
                int currWeight = 0;

                // To check different combinations of items
                for (int j = 0; j < n; j++) {
                    if ((i & (1 << j)) > 0) {
                        currSet.add(items[j][2]);
                        currProfit += items[j][0];
                        currWeight += items[j][1];
                    }
                }

                // Update the maximum profit and weight if the current combination is valid and better
                if (currWeight <= capacity && currProfit > maxProfit) {
                    maxProfit = currProfit;
                    maxWeight = currWeight;
                    set = new ArrayList<>(currSet);
                }
            }

            // Write the solution to an output file
            try (FileWriter writer = new FileWriter(outputFileName)) {
                writer.write(set.size() + " " + maxProfit + " " + maxWeight + "\n");
                for (int itemNumber : set) {
                    writer.write(lines.get(itemNumber) + "\n");
                }
                System.out.println("File '" + outputFileName + "' has been created with the solution.");
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    // Main method to execute the bruteForceKnapsack method
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java bruteforce <input_filename>");
        } else {
            String inputFilename = args[0];
            bruteForceKnapsack(inputFilename, "output1.txt");
        }    
    }
}
