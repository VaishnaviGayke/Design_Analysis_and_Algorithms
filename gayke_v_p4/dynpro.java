import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

class dynpro {

    // Static variables for the uninitialized state of the dparray
    static int label = -3;

    // Lists to store weights and profits of items
    static List<Integer> weights;
    static List<Integer> profits;

    // array for dynamic programming
    static int[][] dparr;

    // Function to solve the Knapsack problem using Dynamic Programming
    public static void dynamicProgramKnapsack(String inputFileName, String outputFileName, String entriesFileName) {
        try (
            FileReader inputFile = new FileReader(inputFileName);
            Scanner sc = new Scanner(inputFile)) {

            // Initialize lists and dparr
            weights = new ArrayList<>();
            profits = new ArrayList<>();
            int countOfItem = 0;
            int capacity = 0;

            // Read the first line to extract countOfItem and capacity
            while(sc.hasNextInt()){
               String inputParam = sc.nextLine();
               String[] data = inputParam.split(" ");
                countOfItem = Integer.parseInt(data[0]);
                capacity = Integer.parseInt(data[1]);
            }

            // Initialize the dparr 
            dparr = new int[countOfItem + 1][capacity + 1];
            for (int i = 0; i < countOfItem + 1; i++) {
                for (int j = 0; j < capacity + 1; j++) {
                    dparr[i][j] = label; // Initialize dparr with label
                }
            }

            // Read input data for weights and profits
            while(sc.hasNext()){
               String inputParam = sc.nextLine();
               String[] data = inputParam.split(" ");
               int profit = Integer.parseInt(data[1]);
               int weight = Integer.parseInt(data[2]);
               weights.add(weight);
               profits.add(profit);
            }

            // If the count of items is more than 1000, print a message
            if (countOfItem > 1000) {
                System.out.println("Count of items is greater than 1000");
            }

            // If the capacity is more than 1000, print a message
            if (capacity > 1000) {
                System.out.println("Capacity is greater than 1000");
            }

            // Solve the Knapsack problem using dynamic programming
            knapsackProb(countOfItem, capacity);

            // Write the calculated entries to the entries file
            try (FileWriter entriesFile = new FileWriter(entriesFileName)) {
                for (int i = 1; i < countOfItem + 1; i++) {
                    StringBuilder s = new StringBuilder("row" + i);
                    for (int j = 1; j < capacity + 1; j++) {
                        if (dparr[i][j] != label) {
                            s.append(" ").append(dparr[i][j]);
                        }
                    }
                    s.append("\n");
                    entriesFile.write(s.toString());
                }
                System.out.println("File '" + entriesFileName + "' has been created with the calculated entries.");
            } catch (IOException e) {
                System.err.println("Error writing entries to file: " + e.getMessage());
            }

            // Write the solution to the output file
            try (FileWriter outputFile = new FileWriter(outputFileName)) {
                int i = countOfItem;
                int remainingCapacity = capacity;

                // trace back to find selected items for the knapsack
                while (i > 0 && remainingCapacity > 0) {
                    if (dparr[i][remainingCapacity] != dparr[i - 1][remainingCapacity]) {
                        i--;
                        remainingCapacity -= weights.get(i);
                    } else {
                        i--;
                    }
                }

                // Initialize list of selected items
                List<Integer> selectedItems = new ArrayList<>();
                int totalProfit = 0;
                int totalWeight = 0;

                // Collect selected items and calculate their total profit and weight
                for (int j = countOfItem; j > i; j--) {
                    if (dparr[j][capacity] != dparr[j - 1][capacity]) {
                        selectedItems.add(j - 1);
                        totalProfit += profits.get(j - 1);
                        totalWeight += weights.get(j - 1);
                        capacity -= weights.get(j - 1);
                    }
                }

                // Reverse the list of selected items to maintain the original order
                List<Integer> reversedSelectedItems = new ArrayList<>();
                for (int j = selectedItems.size() - 1; j >= 0; j--) {
                    reversedSelectedItems.add(selectedItems.get(j));
                }

                // Write the knapsack solution to the output file
                outputFile.write(reversedSelectedItems.size() + " " + totalProfit + " " + totalWeight + "\n");

                for (int j = 0; j < reversedSelectedItems.size(); j++) {
                    outputFile.write("Item" + (reversedSelectedItems.get(j) + 1) + " " + profits.get(reversedSelectedItems.get(j)) + " " + weights.get(reversedSelectedItems.get(j)) + "\n");
                }

                System.out.println("File '" + outputFileName + "' has been created with the solution.");
            } catch (IOException e) {
                System.err.println("Error writing output to file: " + e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    // Function to solve the Knapsack problem using Dynamic Programming
    public static int knapsackProb(int index, int excessWeight) {
        if (dparr[index][excessWeight] != label) {
            return dparr[index][excessWeight];
        }
        if (index == 0 || excessWeight == 0) {
            dparr[index][excessWeight] = 0;
            return 0;
        }

        int newWeight = excessWeight - weights.get(index - 1);

        if (dparr[index - 1][excessWeight] == label) {
            dparr[index - 1][excessWeight] = knapsackProb(index - 1, excessWeight);
        }
        if (newWeight >= 0 && dparr[index - 1][newWeight] == label) {
            dparr[index - 1][newWeight] = knapsackProb(index - 1, newWeight);
        }
        dparr[index][excessWeight] = dparr[index - 1][excessWeight];

        if (newWeight >= 0 && dparr[index - 1][newWeight] + profits.get(index - 1) > dparr[index][excessWeight]) {
            dparr[index][excessWeight] = dparr[index - 1][newWeight] + profits.get(index - 1);
        }

        return dparr[index][excessWeight];
    }

    // Main method to execute the dynamic programming approach
    public static void main(String args[]) {
        // Check if the correct number of command line arguments is provided
        if (args.length != 1) {
            System.out.println("Usage: java dynpro <input_filename>");
        } else {
            String inputFilename = args[0];
            dynamicProgramKnapsack(inputFilename, "output2.txt", "entries2.txt"); // Solve Knapsack problem using Dynamic Programming
        }
    }
}
