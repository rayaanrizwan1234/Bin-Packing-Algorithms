import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.io.FileNotFoundException; // Import this class to handle errors
import java.io.File;

public class FFD_WFD_2 {
    ArrayList<Integer> resCap = new ArrayList<>();
    int numOfBins = 0;
    int capacity;

    boolean firstFit(Integer items[]) {
        for (int item : items) {
            int j;
            boolean allocated = false;
            for (j = 0; j < numOfBins; j++) {
                if (resCap.get(j) >= item) {
                    final var res = resCap.get(j) - item;
                    resCap.set(j, res);
                    allocated = true;
                    break;
                }
            }
            if (j == numOfBins && !allocated) {
                resCap.add(capacity - item);
                numOfBins++;
            }
        }
        return true;
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
    boolean ffdToWfd(Integer items[], int capacity, double itemRatio) {
        Arrays.sort(items, Collections.reverseOrder());

        int numItems1 = (int) Math.ceil(items.length * itemRatio);

        final var items1 = Arrays.copyOfRange(items, 0, numItems1);
        final var items2 = Arrays.copyOfRange(items, numItems1, items.length);

        this.capacity = capacity;

        firstFit(items1);
        worstFitDecreasing(items2);

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
                int best = 10000000;
                double ira = -1;
                // for (double ir = 0.0; ir <= 1; ir += 0.01) {
                int sum = 0;
                for (int i = 0; i < problems; i++) {
                    FFD_WFD_2 hybrid = new FFD_WFD_2();
                    hybrid.ffdToWfd(itemList[i], 150, 0.65);
                    System.out.println("Number of bins = " + hybrid.numOfBins + "\n");
                    sum += hybrid.numOfBins;
                }
                // System.out.println("ir ra = " + ir);
                if (sum < best) {
                    best = sum;
                    // ira = ir;
                }
                // }
                System.out.println(ira);

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
