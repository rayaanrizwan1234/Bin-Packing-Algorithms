import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.io.FileNotFoundException; // Import this class to handle errors
import java.io.File;
import java.util.stream.Stream;

public class BFD_MBWFD_3 {
    ArrayList<Integer> resCap = new ArrayList<>();
    int numOfBins = 0;
    int capacity;

    Integer[] bestFitFirstStage(Integer items[], boolean binRatio) {
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
            if ((min == capacity + 1) && !binRatio && (capacity >= item)) {
                resCap.add(capacity - item);
                numOfBins++;
            } else if (min == capacity + 1) {
                notAllocatedItems.add(item);
            } else {
                resCap.set(minBin, min);
            }
        }
        return notAllocatedItems.toArray(new Integer[0]);
    }

    Boolean mbWfd(Integer[] items) {
        ArrayList<Integer> notAllocatedItems = new ArrayList<>();
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
                notAllocatedItems.add(item);
            } else {
                resCap.set(maxBin, max);
            }
        }

        return notAllocatedItems.size() > 0 ? false : true;
    }

    // This method is used to set triggers for number of bins or the number of items
    boolean bfdToMbwfd(Integer items[], int capacity, double capacityRatio, double binRatio, double itemRatio) {
        Arrays.sort(items, Collections.reverseOrder());

        int upperBound = items.length;

        int newCap = (int) Math.ceil(capacity * capacityRatio);
        this.capacity = newCap;

        // Splits bins based on given trigger value
        int sumOfItems = 0;
        for (Integer i : items) {
            sumOfItems += i;
        }
        double maximumBins = (double) sumOfItems / capacity;
        maximumBins = Math.ceil(maximumBins) * binRatio;
        numOfBins = (int) Math.ceil(maximumBins);
        resCap.addAll(Collections.nCopies(numOfBins, newCap));

        // splits items based on given trigger value
        Integer[] items1 = null;
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
            final var unAllocated = bestFitFirstStage(items1, true);
            Integer[] resultArray = Stream.concat(Arrays.stream(unAllocated), Arrays.stream(items))
                    .toArray(Integer[]::new);
            items = resultArray;
        } else if (binRatio == 0 && itemRatio != 0) {
            final var unAllocated = bestFitFirstStage(items1, false);
            Integer[] resultArray = Stream.concat(Arrays.stream(unAllocated), Arrays.stream(items))
                    .toArray(Integer[]::new);
            items = resultArray;
        } else {
            items = bestFitFirstStage(items, true);
        }

        for (int i = 0; i < resCap.size(); i++) {
            int cap = resCap.get(i) + capacity - newCap;
            resCap.set(i, cap);
        }

        int numOfBinsFirstStage = numOfBins;

        ArrayList<Integer> firstStageResCap = new ArrayList<>(resCap);
        int lowerBound = (int) maximumBins;
        this.capacity = capacity;

        ArrayList<Integer> tempResCap = new ArrayList<>();
        int min = upperBound;
        while (lowerBound <= upperBound) {
            int middle = lowerBound + ((upperBound - lowerBound) / 2);
            this.numOfBins = middle;
            resCap.clear();
            resCap.addAll(firstStageResCap);
            Boolean success = false;
            if (middle >= numOfBinsFirstStage) {
                resCap.addAll(Collections.nCopies(numOfBins - numOfBinsFirstStage, capacity));
                success = mbWfd(items);
            }

            if (!success) {
                lowerBound = middle + 1;
            } else {
                tempResCap = new ArrayList<>(resCap);
                min = middle;
                upperBound = middle - 1;
            }
        }

        resCap = tempResCap;
        numOfBins = min;

        return true;
    }

    // this gives the same result for any ratio you put in because nothing is being
    // changed
    public static void main(String[] args) {
        Double[] values = { 0.5, 0.55, 0.6, 0.65, 0.7, 0.75, 0.8, 0.85, 0.9, 0.95, 1.0 };
        try {
            // Reading data from a file
            File binText = new File(
                    "/uolstore/home/users/sc21rr/Desktop/Bin_Packing/algorithms/Bin-Packing-Algorithms/Testing-Data/Faulkner/binpack4.txt");
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
                double cr = -4.0;
                double br = -4.0;
                double ira = -4.0;
                int best = 100000000;

                for (double c : values) {
                    for (double b : values) {
                        for (double ir : values) {
                            int sum = 0;
                            for (int i = 0; i < problems; i++) {
                                BFD_MBWFD_3 hybrid = new BFD_MBWFD_3();
                                hybrid.bfdToMbwfd(itemList[i], 150, c, b, ir);
                                sum += hybrid.numOfBins;
                            }

                            if (sum < best) {
                                best = sum;
                                br = b;
                                cr = c;
                                ira = ir;
                            }
                        }
                    }
                }

                System.out.println(br + " " + cr + " " + ira);
                long elapsedTime = System.nanoTime() - startTime;

                for (int i = 0; i < problems; i++) {
                    BFD_MBWFD_3 hybrid = new BFD_MBWFD_3();
                    hybrid.bfdToMbwfd(itemList[i], 150, cr, br, ira);
                    System.out.println("num of bins " + hybrid.numOfBins);
                }

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
