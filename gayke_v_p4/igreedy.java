import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class igreedy {

    // Inner class to represent an Item with its index, profit, weight, and ratio
    static class Item {
        int index;
        int profit;
        int weight;
        double ratio;

        Item(int index, int profit, int weight) {
            this.index = index;
            this.profit = profit;
            this.weight = weight;
            this.ratio = (double) profit / weight; // Calculate the profit to weight ratio
        }
    }

    // Method to sort the selected items based on their index
    private static void sortItemsByIndex(List<Item> items) {
        items.sort(Comparator.comparingInt(item -> item.index));
    }

    // Method to solve the Knapsack problem using an approximation algorithm
    public static void approxKnapsack(String inputFileName, String outputFileName) {
        try {
            // Read the input file containing Knapsack problem details
            List<String> lines = Files.readAllLines(Paths.get(inputFileName));
            String[] firstLine = lines.get(0).split(" ");
            int n = Integer.parseInt(firstLine[0]); // Number of items
            int capacity = Integer.parseInt(firstLine[1]); // Capacity of the knapsack

            List<Item> items = new ArrayList<>(); // List to store Item objects
            int maxBenefit = Integer.MIN_VALUE; // Maximum benefit among all items

            // Extract item profits, weights, and calculate maximum benefit
            for (int i = 0; i < n; i++) {
                String[] parts = lines.get(i + 1).split(" ");
                int profit = Integer.parseInt(parts[1]);
                int weight = Integer.parseInt(parts[2]);
                maxBenefit = Math.max(maxBenefit, profit); // Update maximum benefit
                items.add(new Item(i + 1, profit, weight)); // Create Item object and add to the list
            }

            // Sort items based on their profit-to-weight ratio in descending order
            items.sort(Comparator.comparingDouble(o -> -o.ratio));

            int totalWeight = 0;
            int totalProfit = 0;
            List<Item> itemsSelected = new ArrayList<>(); // List to store selected items

            int greedyProfit = 0;
            // Greedy approach to select items that fit within the capacity
            for (Item item : items) {
                if (totalWeight + item.weight <= capacity) {
                    totalWeight += item.weight;
                    totalProfit += item.profit;
                    itemsSelected.add(item);
                    greedyProfit = totalProfit;
                }
            }

            // Determine the approximation solution (greedy profit or maximum benefit)
            int approximation = Math.max(greedyProfit, maxBenefit);

            // Write the solution to an output file
            try (FileWriter writer = new FileWriter(outputFileName)) {
                int finalProfit;
                int finalWeight;
                List<Item> finalItems;

                // To choose the final items and their details to write to the output file
                if (greedyProfit >= maxBenefit) {
                    finalProfit = totalProfit;
                    finalWeight = totalWeight;
                    finalItems = itemsSelected;
                } else {
                    finalProfit = maxBenefit;
                    finalWeight = 0;
                    finalItems = new ArrayList<>();
                    for (Item item : items) {
                        if (item.profit == maxBenefit) {
                            finalItems.add(item);
                            finalWeight += item.weight;
                            break;
                        }
                    }
                }

                // Write the final items and their details to the output file
                writer.write(finalItems.size() + " " + finalProfit + " " + finalWeight + "\n");
                sortItemsByIndex(finalItems); // Sort the final selected items by their index
                for (Item selectedItem : finalItems) {
                    writer.write("Item" + selectedItem.index + " " + selectedItem.profit + " " + selectedItem.weight + "\n");
                }
                System.out.println("File '" + outputFileName + "' has been created with the solution.");
            } catch (IOException e) {
                System.err.println("Error writing output to file: " + e.getMessage());
            }

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    // Main method to execute the approxKnapsack method
    public static void main(String[] args) {
        // Check if the correct number of commandline arguments is provided
        if (args.length != 1) {
            System.out.println("Usage: java igreedy <input_filename>");
        } else {
            String inputFilename = args[0];
            approxKnapsack(inputFilename, "output3.txt"); // Solve Knapsack problem using the approximation algorithm
        }
    }
}
