import java.io.*;
import java.util.*;

// Class representing a Knapsack item with name, profit, weight, and profit-to-weight ratio
class KsItem {
    String name;
    int profit;
    int weight;
    double ratio;

    // Constructor for initializing a KsItem
    public KsItem(String name, int profit, int weight) {
        this.name = name;
        this.profit = profit;
        this.weight = weight;
        this.ratio = (double) profit / weight;
    }
}

public class backtrack {
    // Static variables for values, weights, capacity, number of items, best solution set, etc.
    static int[] values;
    static int[] weights;
    static int capacity;
    static int numItems;
    static int[] bestSet;
    static int maxProfit;
    static int visitCounter;
    static List<String> entries;
    static List<KsItem> bestItems;

    // Main method to execute the Knapsack problem using backtracking
    public static void main(String[] args) {
        // Check for correct commandline argument usage
        if (args.length != 1) {
            System.out.println("Usage: java backtrack <input_file>");
            return;
        }

        // Read input from the specified file
        String inputFile = args[0];
        readInput(inputFile);

        // Sort items based on profit-to-weight ratio
        KsItem[] items = new KsItem[numItems];
        for (int i = 0; i < numItems; i++) {
            items[i] = new KsItem("Item" + (i + 1), values[i], weights[i]);
        }
        Arrays.sort(items, Comparator.comparingDouble(item -> -item.ratio));

        // Create arrays for sorted profits and weights
        int[] sortedProfits = new int[numItems];
        int[] sortedWeights = new int[numItems];
        for (int i = 0; i < numItems; i++) {
            sortedProfits[i] = items[i].profit;
            sortedWeights[i] = items[i].weight;
        }

        // Initialize variables for backtracking
        bestSet = new int[numItems];
        maxProfit = 0;
        visitCounter = 0;
        entries = new ArrayList<>();
        bestItems = new ArrayList<>();

        // Perform backtracking to find the optimal solution
        backtrack(0, 0, 0, items, new ArrayList<>());

        // output results to files
        outputResults("entries3.txt");
        outputFinalSolution("output3.txt");
    }

    // Calculate the upper bound of the current state
    private static double calculateUpperBound(int level, int currentWeight, int currentProfit, KsItem[] items) {
        int weight = currentWeight;
        double profit = currentProfit;
        double bound = profit;

        for (int i = level; i < items.length; i++) {
            if (weight + items[i].weight <= capacity) {
                weight += items[i].weight;
                bound += items[i].profit;
            } else {
                double remaining = capacity - weight;
                bound += (items[i].profit / (double) items[i].weight) * remaining;
                break;
            }
        }

        return bound;
    }

    // Backtracking method to explore possible solutions
    private static void backtrack(int level, int currentWeight, int currentProfit, KsItem[] items, List<KsItem> currentItems) {
        visitCounter++;

        // Check if the current state is over capacity before calculating the bound
        if (currentWeight > capacity) {
            entries.add(String.format("%d %d %d %d", visitCounter, currentProfit, currentWeight, 0));
            return; // Prune the branch if over capacity
        }

        // Calculate the upper bound for the current state
        double bound = calculateUpperBound(level, currentWeight, currentProfit, items);
        
        // Conditional formatting depending on whether the bound is zero or non-zero
        String entry = bound > 0 ?
                String.format("%d %d %d %.6f", visitCounter, currentProfit, currentWeight, bound) :
                String.format("%d %d %d %d", visitCounter, currentProfit, currentWeight, 0);
        entries.add(entry);

        // Update the best solution if the current state is valid and has higher profit
        if (currentWeight <= capacity && currentProfit > maxProfit) {
            maxProfit = currentProfit;
            bestItems = new ArrayList<>(currentItems);
        }

        // prune the branch if not promising or reached the end of items
        if (level == items.length || bound <= (double)maxProfit) {
            return;
        }

        // Include the current item and recursively explore the branch
        currentItems.add(items[level]);
        backtrack(level + 1, currentWeight + items[level].weight, currentProfit + items[level].profit, items, currentItems);

        // Exclude the current item and recursively explore the branch
        currentItems.remove(currentItems.size() - 1);
        backtrack(level + 1, currentWeight, currentProfit, items, currentItems);
    }

    // Read input from the specified file
    private static void readInput(String inputFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line = reader.readLine();
            String[] firstLineValues = line.split(" ");
            numItems = Integer.parseInt(firstLineValues[0]);
            capacity = Integer.parseInt(firstLineValues[1]);

            values = new int[numItems];
            weights = new int[numItems];

            for (int i = 0; i < numItems; i++) {
                line = reader.readLine();
                values[i] = Integer.parseInt(line.split(" ")[1]);
                weights[i] = Integer.parseInt(line.split(" ")[2]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Output the intermediate results to a file
    private static void outputResults(String outputFile) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
            
            for (String entry : entries) {
                writer.println(entry);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Output the final solution to a file
    private static void outputFinalSolution(String outputFile) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
            int totalWeight = 0;
            int totalProfit = 0;
            int itemCount = 0;

            // Calculate total weight, total profit, and count of items in the best solution
            for (KsItem item : bestItems) {
                itemCount++;
                totalWeight += item.weight;
                totalProfit += item.profit;
            }

            // Output the total values and details of each item in the best solution
            writer.println(itemCount + " " + totalProfit + " " + totalWeight);
            for (KsItem item : bestItems) {
                writer.println(item.name + " " + item.profit + " " + item.weight);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}