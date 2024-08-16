import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.io.FileNotFoundException; // Import this class to handle errors
import java.io.File;
import java.util.stream.Stream;

public class BFD_BFD_3 {
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

    boolean bestFitSecondStage(Integer items[]) {
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
    boolean bfdToBfd(Integer items[], int capacity, double capacityRatio, double binRatio, double itemRatio) {
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

        this.capacity = capacity;

        bestFitSecondStage(items);

        return true;
    }

    // this gives the same result for any ratio you put in because nothing is being
    // changed
    public static void main(String[] args) {
        Double[] values = { 0.5, 0.55, 0.6, 0.65, 0.7, 0.75, 0.8, 0.85, 0.9, 0.95, 1.0 };
        try {
            File folder = new File(
                    "/uolstore/home/users/sc21rr/Desktop/Bin_Packing/algorithms/Bin-Packing-Algorithms/Testing-Data/Waescher");
            File[] listOfFiles = folder.listFiles();

            // long startTime = System.nanoTime();
            // int best = 1000000000;
            // Double cr = -5.5;
            // Double br = -4.0;
            // Double ira = -3.0;
            // for (Double c : values) {
            // for (Double b : values) {
            // for (Double ir : values) {
            // int sum = 0;
            // for (File file : listOfFiles) {
            // try (Scanner textReader = new Scanner(file)) {
            // int n = Integer.parseInt(textReader.nextLine());
            // int cap = Integer.parseInt(textReader.nextLine());
            // Integer[] item = new Integer[n];
            // for (int j = 0; j < n; j++) {
            // String data = textReader.nextLine();
            // item[j] = Integer.parseInt(data);
            // }
            // BFD_BFD_3 res = new BFD_BFD_3();
            // res.bfdToBfd(item, cap, c, b, ir);
            // sum += res.numOfBins;
            // }
            // }
            // System.out.println("c = "+c +" b = "+b+" i = "+ir+" sum = "+sum);
            // if (sum < best) {
            // best = sum;
            // cr = c;
            // br = b;
            // ira = ir;
            // }
            // }
            // }
            // }
            // System.out.println("cr = "+cr +" br = "+br+" ir = "+ira+ " best = "+best);
            // long endTime = System.nanoTime();
            // System.out.println("Time taken: " + (endTime - startTime) / 1000000 +
            // "ms\n");

            for (File file : listOfFiles) {
                try (Scanner textReader = new Scanner(file)) {
                    int n = Integer.parseInt(textReader.nextLine());
                    int cap = Integer.parseInt(textReader.nextLine());
                    Integer[] item = new Integer[n];
                    for (int j = 0; j < n; j++) {
                        String data = textReader.nextLine();
                        item[j] = Integer.parseInt(data);
                    }
                    BFD_BFD_3 res = new BFD_BFD_3();
                    res.bfdToBfd(item, cap, 0.5, 0.5, 0.5);
                    System.out.println("num of bins " + res.numOfBins);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
