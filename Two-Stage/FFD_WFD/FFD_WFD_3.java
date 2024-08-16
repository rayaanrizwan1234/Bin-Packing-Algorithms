import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.io.FileNotFoundException; // Import this class to handle errors
import java.io.File;
import java.util.stream.Stream;

public class FFD_WFD_3 {
    ArrayList<Integer> resCap = new ArrayList<>();
    int numOfBins = 0;
    int capacity;

    Integer[] firstFit(Integer items[], boolean binRatio) {
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
            if (j == numOfBins && !allocated && !binRatio && (capacity >= item)) {
                resCap.add(capacity - item);
                numOfBins++;
            } else if (j == numOfBins && !allocated) {
                notAllocatedItems.add(item);
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
    boolean ffdToWfd(Integer items[], int capacity, double capacityRatio, double binRatio, double itemRatio) {
        Arrays.sort(items, Collections.reverseOrder());

        int newCap = (int) Math.ceil(capacity * capacityRatio);
        this.capacity = newCap;

        // Splits bins based on given trigger value
        if (binRatio != 0) {
            int sumOfItems = 0;
            for (Integer i : items) {
                sumOfItems += i;
            }
            double maximumBins = (double) sumOfItems / capacity;
            maximumBins = Math.ceil(maximumBins) * binRatio;
            numOfBins = (int) Math.ceil(maximumBins);
            resCap.addAll(Collections.nCopies(numOfBins, newCap));
        }

        Integer[] items1 = null;
        // splits items based on given trigger value
        if (itemRatio != 0) {
            int numItems1 = (int) Math.ceil(items.length * itemRatio);

            // items for first stage
            items1 = Arrays.copyOfRange(items, 0, numItems1);
            // items for second stage
            items = Arrays.copyOfRange(items, numItems1, items.length);
        }

        // Checks which triggers are set and runs accordingly
        if (binRatio != 0 && itemRatio != 0) {
            // gets the unallocated items from the first stage and passes it onto the second
            // stage
            final var unAllocated = firstFit(items1, true);
            Integer[] resultArray = Stream.concat(Arrays.stream(unAllocated), Arrays.stream(items))
                    .toArray(Integer[]::new);
            items = resultArray;
        } else if (binRatio == 0 && itemRatio != 0) {
            final var unAllocated = firstFit(items1, false);
            Integer[] resultArray = Stream.concat(Arrays.stream(unAllocated), Arrays.stream(items))
                    .toArray(Integer[]::new);
            items = resultArray;
        } else {
            items = firstFit(items, true);
        }

        for (int i = 0; i < resCap.size(); i++) {
            int cap = resCap.get(i) + capacity - newCap;
            resCap.set(i, cap);
        }

        this.capacity = capacity;

        worstFitDecreasing(items);

        return true;
    }

    // this gives the same result for any ratio you put in because nothing is being
    // changed
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
                double cr = -1;
                double br = -1;
                for (double c = 0.1; c <= 0.3; c += 0.01) {
                    for (double b = 0.4; b <= 0.6; b += 0.01) {
                        for (double ir = 0.85; ir <= 0.95; ir += 0.01) {
                            int sum = 0;
                            for (int i = 0; i < problems; i++) {
                                FFD_WFD_3 hybrid = new FFD_WFD_3();
                                hybrid.ffdToWfd(itemList[i], 150, 0.51, 0.95, 0.7);
                                System.out.println("Number of bins = " + hybrid.numOfBins + "\n");
                                sum += hybrid.numOfBins;
                            }
                            System.out.println("cr " + c + " br " + b + " ir " + ir);
                            if (sum < best) {
                                best = sum;
                                ira = ir;
                                br = b;
                                cr = c;
                            }
                        }
                    }
                }
                System.out.println(cr + " " + br + " " + ira);

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
