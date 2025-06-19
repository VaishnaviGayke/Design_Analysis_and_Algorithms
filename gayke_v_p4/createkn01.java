import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class createkn01 {

    // Method to generate a Knapsack problem and write it to a file
    public static void createKnapsack01(String fileName) {
        Random random = new Random();

        // Generate a random number of items between 5 and 10
        int n = random.nextInt(6) + 5;
        System.out.println("Number of items (n): " + n);

        // Create a 2D array to store item profits and weights
        int[][] items = new int[n][2];
        int totalWeight = 0;

        // Generate random profits and weights for each item
        for (int i = 0; i < n; i++) {
            int profit = random.nextInt(21) + 10; // Random profit between 10 and 30
            int weight = random.nextInt(16) + 5; // Random weight between 5 and 20
            items[i][0] = profit; // Store profit in the first column of the 2D array
            items[i][1] = weight; // Store weight in the second column of the 2D array
            totalWeight += weight; // Calculate total weight of all items
        }

        // Determine the capacity of the knapsack 
        int capacity = (int) (0.6 * totalWeight);
        System.out.println("Capacity of the knapsack: " + capacity);

        try (FileWriter writer = new FileWriter(fileName)) {
            // Write the number of items and knapsack capacity to the file
            writer.write(n + " " + capacity + "\n");

            // Write each item's details (profit and weight) to the file
            for (int i = 0; i < n; i++) {
                writer.write("Item" + (i + 1) + " " + items[i][0] + " " + items[i][1] + "\n");
            }

            // Confirmation message after successful file creation
            System.out.println("File '" + fileName + "' has been created successfully with the Knapsack problem.");
        } catch (IOException e) {
            // Display error message if there's an issue writing to the file
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    // Main method to execute the program
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java create01 <input_filename>");
        } else {
            String inputFilename = args[0];
            createKnapsack01(inputFilename);
        }    
    }
}
