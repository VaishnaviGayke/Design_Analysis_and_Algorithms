import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class program1 {
    public static HashMap<String, Integer> marketPriceReader(String filename) throws FileNotFoundException {
        HashMap<String, Integer> cardToNames = new HashMap<>();
        try (Scanner sc = new Scanner(new File(filename))) {
            int numb = Integer.parseInt(sc.nextLine());

            for (int i = 0; i < numb; i++) {
                String[] line = sc.nextLine().split("\\s+");
                cardToNames.put(line[0], Integer.parseInt(line[1]));
            }
        }
        return cardToNames;
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        if (args.length != 2) {
            System.err.println("Usage: java Program1 market_list.txt price_list.txt");
            System.exit(1);
        }

        long startTime = System.nanoTime();
        
        HashMap<String, Integer> mpCard = marketPriceReader(args[0]);
        Scanner sc = new Scanner(new File(args[1]));

        // Create a PrintWriter specifically for error messages to write them to the output file
        PrintWriter errorPrintWriter = new PrintWriter(new FileWriter("output.txt", true));

        try (FileWriter fileWriter = new FileWriter("output.txt");
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

            while (sc.hasNextLine()) {
                String[] no = sc.nextLine().split("\\s+");
                int cardNumber = Integer.parseInt(no[0]);
                int budget = Integer.parseInt(no[1]);
                String[] cardNames = new String[cardNumber];
                int[][] budgetList = new int[cardNumber][2];
                ArrayList<String> cardNamesList = new ArrayList<>();

                for (int j = 0; j < cardNumber; j++) {
                    if (!sc.hasNextLine()) {
                        errorPrintWriter.println("Error: Insufficient data for card " + (j + 1));
                        break;
                    }
                    String[] lineByLine = sc.nextLine().split("\\s+");
                    if (lineByLine.length < 2) {
                        errorPrintWriter.println("Error: Missing data for card " + (j + 1));
                        continue;
                    }
                    String cardName = lineByLine[0];
                    if (!mpCard.containsKey(cardName)) {
                        // Card not found in market_price.txt, print error message
                        errorPrintWriter.println("Error: Card '" + cardName + "' not found in market_price.txt");
                        continue;
                    }
                    cardNamesList.add(cardName);
                    budgetList[j][0] = Integer.parseInt(lineByLine[1]);
                    budgetList[j][1] = mpCard.get(cardName);
                }

                if (cardNumber == cardNamesList.size()) { // Check if data is available for all cards
                    int maxProfit = 0;
                    ArrayList<String> maxProfitSubset = new ArrayList<>();

                    for (int i = 0; i < (1 << cardNumber); i++) {
                        int sumBuy = 0;
                        int sumSell = 0;
                        ArrayList<String> name = new ArrayList<>();
                        int profit = 0;

                        for (int j = 0; j < cardNumber; j++) {
                            if ((i & (1 << j)) != 0) {
                                sumBuy += budgetList[j][0];
                                sumSell += budgetList[j][1];
                                name.add(cardNamesList.get(j));
                            }
                        }

                        profit = sumSell - sumBuy;

                        if (sumBuy <= budget) {
                            if (profit > maxProfit) {
                                maxProfit = profit;
                                maxProfitSubset = new ArrayList<>(name); // Create a new copy
                            }
                        }
                    }

                    
                    long endTime = System.nanoTime();
                    long time = endTime - startTime;
                    double timeInSeconds = (double) time / 1e9;

                    // Write the results to the output file
                    printWriter.println(cardNumber + " " + maxProfit + " " + maxProfitSubset.size() + " " + timeInSeconds);
                    for (String s : maxProfitSubset) {
                        printWriter.println(s);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing to output.txt: " + e.getMessage());
        } finally {
            errorPrintWriter.close(); // Close the error PrintWriter
        }
    }
}