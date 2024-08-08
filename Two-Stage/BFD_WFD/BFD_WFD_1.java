import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.io.FileNotFoundException; // Import this class to handle errors
import java.io.File;

public class BFD_WFD_1 {
    ArrayList<Integer> resCap = new ArrayList<>();
    int numOfBins = 0;
    int capacity;

    Integer[] bestFitDecreasing(Integer items[]) {
        ArrayList<Integer> notAllocatedItems = new ArrayList<>();
        for (int item : items) {
            int min = capacity + 1;
            int minBin = 0;
            for (int j = 0; j < numOfBins; j++) {
                int remaining = resCap.get(j) - item;
                if ((resCap.get(j) >= item) && remaining < min) {
                    minBin = j;
                    min = remaining;
                }
            }
            if (min == capacity + 1) {
                notAllocatedItems.add(item);
            } else {
                resCap.set(minBin, min);
            }
        }
        return notAllocatedItems.toArray(new Integer[0]);
    }

    boolean worstFitDecreasing(Integer items[]) {
        for (int item : items) {
            int max = -1;
            int maxBin = -1;
            for (int j = 0; j < resCap.size(); j++) {
                int remaining = resCap.get(j) - item;
                if (resCap.get(j) >= item && (remaining > max)) {
                    maxBin = j;
                    max = remaining;
                }
            }
            if (max == -1) {
                resCap.add(capacity - item);
                numOfBins++;
            } else {
                resCap.set(maxBin, max);
            }
        }
        return true;
    }

    // This method is used to set triggers for number of bins or the number of items
    boolean bfdToWfd(Integer items[], int capacity, double binRatio) {
        Arrays.sort(items, Collections.reverseOrder());

        int sumOfItems = 0;
        for (Integer i : items) {
            sumOfItems += i;
        }

        this.capacity = capacity;

        double maximumBins = (double) sumOfItems / capacity;
        maximumBins = Math.ceil(maximumBins) * binRatio;
        numOfBins = (int) Math.ceil(maximumBins);

        resCap.addAll(Collections.nCopies(numOfBins, capacity));

        items = bestFitDecreasing(items);
        worstFitDecreasing(items);

        return true;
    }

    //
    public static void main(String[] args) {
        try {
            // Reading data from a file
            File binText = new File(
                    "/uolstore/home/users/sc21rr/Desktop/Bin_Packing/algorithms/Bin-Packing-Algorithms/Testing-Data/binpack4.txt");
            try (Scanner textReader = new Scanner(binText)) {
                int problems = Integer.parseInt(textReader.nextLine());
                // Record the start time
                Integer[][] itemList = new Integer[problems][1000];
                for (int i = 0; i < problems; i++) {
                    System.out.print("Problem:" + textReader.nextLine() + "\n");
                    String data = textReader.nextLine().trim();
                    int n = Integer.parseInt(data.substring(4, 8));
                    for (int j = 0; j < n; j++) {
                        data = textReader.nextLine();
                        itemList[i][j] = Integer.parseInt(data);
                    }
                    // Testing objects

                }
                long startTime = System.nanoTime();
                // Calculate the elapsed time
                for (int i = 0; i < problems; i++) {
                    BFD_WFD_1 hybrid = new BFD_WFD_1();
                    hybrid.bfdToWfd(itemList[i], 150, 0.5);
                    System.out.println("Number of bins = " + hybrid.numOfBins + "\n");
                }
                long elapsedTime = System.nanoTime() - startTime;

                System.out.println("Execution time in ms: " + elapsedTime / 1000000);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            System.out.print("An error occured.\n");
            e.printStackTrace();
        }
    }
}
