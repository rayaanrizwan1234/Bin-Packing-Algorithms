import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.io.FileNotFoundException; // Import this class to handle errors
import java.io.File;
import java.util.stream.Stream;

// FFD -> BFD cap & bin trigger
public class FFD_BFD_3 {
    ArrayList<Integer> resCap = new ArrayList<>();
    int numOfBins = 0;
    int capacity;

    Integer[] firstFit(Integer items[]) {
        ArrayList<Integer> notAllocatedItems = new ArrayList<>();
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
                notAllocatedItems.add(item);
            }
        }
        return notAllocatedItems.toArray(new Integer[0]);
    }

    boolean bestFitDecreasing(Integer items[]) {
        for (int item : items) {
            int min = capacity + 1;
            int minBin = 0;
            for (int j = 0; j < resCap.size(); j++) {
                int remaining = resCap.get(j) - item;
                if (resCap.get(j) >= item && (remaining < min)) {
                    minBin = j;
                    min = remaining;
                }
            }
            if (min == capacity + 1) {
                resCap.add(capacity - item);
                numOfBins++;
            } else {
                resCap.set(minBin, min);
            }
        }
        return true;
    }

    // This method is used to set triggers for number of bins or the number of items
    boolean ffdToBfd(Integer items[], int capacity, double capRatio, double binRatio, double itemRatio) {
        Arrays.sort(items, Collections.reverseOrder());

        int newCap = (int) Math.ceil(capacity * capRatio);

        int sumOfItems = 0;
        for (Integer i : items) {
            sumOfItems += i;
        }

        double maximumBins = (double) sumOfItems / capacity;
        maximumBins = Math.ceil(maximumBins) * binRatio;
        numOfBins = (int) Math.ceil(maximumBins);
        resCap.addAll(Collections.nCopies(numOfBins, newCap));

        Integer[] items1 = null;
        // splits items based on given trigger value
        if (itemRatio != 0) {
            int numItems1 = (int) Math.ceil(items.length * itemRatio);

            // items for first stage
            items1 = Arrays.copyOfRange(items, 0, numItems1);
            // items for second stage
            items = Arrays.copyOfRange(items, numItems1, items.length);
        }

        this.capacity = capacity;

        if (itemRatio != 0) {
            final var unAllocated = firstFit(items1);
            Integer[] resultArray = Stream.concat(Arrays.stream(unAllocated), Arrays.stream(items))
                    .toArray(Integer[]::new);
            items = resultArray;
        } else {
            items = firstFit(items);
        }

        for (int i = 0; i < resCap.size(); i++) {
            int cap = resCap.get(i) + capacity - newCap;
            resCap.set(i, cap);
        }

        bestFitDecreasing(items);

        return true;
    }

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
                    FFD_BFD_3 hybrid = new FFD_BFD_3();
                    hybrid.ffdToBfd(itemList[i], 150, 0.51, 0.95, 0.71);
                    System.out.println("Item trigger " + hybrid.numOfBins + "\n");
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