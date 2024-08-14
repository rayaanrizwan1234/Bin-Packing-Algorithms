import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.io.FileNotFoundException; // Import this class to handle errors
import java.io.File;

public class BFD_MBWFD_1 {
    ArrayList<Integer> resCap = new ArrayList<>();
    int numOfBins = 0;
    int capacity;

    Integer[] bestFitFirstStage(Integer items[]) {
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
    boolean bfdToMbWfd(Integer items[], int capacity, double binRatio) {
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

        items = bestFitFirstStage(items);
        int numOfBinsFirstStage = numOfBins;

        sumOfItems = 0;
        for (Integer i : items) {
            sumOfItems += i;
        }

        // run binary search over different m values
        int lowerBound = (int) Math.ceil((double) sumOfItems / capacity);
        int upperBound = items.length;
        numOfBins = (int) lowerBound;

        ArrayList<Integer> tempResCap = new ArrayList<>();
        int min = upperBound;
        while (lowerBound <= upperBound) {
            int middle = lowerBound + ((upperBound - lowerBound) / 2);
            this.numOfBins = middle;
            resCap.clear();
            resCap.addAll(Collections.nCopies(numOfBins, capacity));
            final var success = mbWfd(items);

            if (!success) {
                lowerBound = middle + 1;
            } else {
                tempResCap = new ArrayList<>(resCap);
                min = middle;
                upperBound = middle - 1;
            }
        }
        resCap = tempResCap;
        numOfBins = min + numOfBinsFirstStage;

        return true;
    }

    // this gives the same result for any ratio you put in because nothing is being
    // changed
    public static void main(String[] args) {
        try {
            int best = 1000000;
            double rat = -1;
            for (Double binRatio = 0.0; binRatio <= 1; binRatio += 0.01) {
                int sum = 0;
                // Reading data from a file
                File binText = new File(
                        "/uolstore/home/users/sc21rr/Desktop/Bin_Packing/algorithms/Bin-Packing-Algorithms/Testing-Data/Faulkner/binpack4.txt");
                try (Scanner textReader = new Scanner(binText)) {
                    int problems = Integer.parseInt(textReader.nextLine());
                    for (int i = 0; i < problems; i++) {
                        textReader.nextLine();
                        // System.out.print("Problem:" + textReader.nextLine() + "\n");
                        String data = textReader.nextLine().trim();
                        int capacity = Integer.parseInt(data.substring(0, 3));
                        int n = Integer.parseInt(data.substring(4, 8));
                        Integer[] item = new Integer[n];
                        for (int j = 0; j < n; j++) {
                            data = textReader.nextLine();
                            item[j] = Integer.parseInt(data);
                        }
                        // Testing objects
                        BFD_MBWFD_1 res = new BFD_MBWFD_1();
                        res.bfdToMbWfd(item, capacity, binRatio);
                        sum += res.numOfBins;
                    }
                    System.out.println("rat = " + binRatio + " sum is " + sum);
                    if (sum <= best) {
                        best = sum;
                        rat = binRatio;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Best ratio is " + rat);

            File binText = new File(
                    "/uolstore/home/users/sc21rr/Desktop/Bin_Packing/algorithms/Bin-Packing-Algorithms/Testing-Data/Faulkner/binpack4.txt");
            try (Scanner textReader = new Scanner(binText)) {
                int problems = Integer.parseInt(textReader.nextLine());
                for (int i = 0; i < problems; i++) {
                    textReader.nextLine();
                    // System.out.print("Problem:" + textReader.nextLine() + "\n");
                    String data = textReader.nextLine().trim();
                    int capacity = Integer.parseInt(data.substring(0, 3));
                    int n = Integer.parseInt(data.substring(4, 8));
                    Integer[] item = new Integer[n];
                    for (int j = 0; j < n; j++) {
                        data = textReader.nextLine();
                        item[j] = Integer.parseInt(data);
                    }
                    // Testing objects
                    BFD_MBWFD_1 res = new BFD_MBWFD_1();
                    res.bfdToMbWfd(item, capacity, rat);
                    System.out.println("num of bins " + res.numOfBins);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.print("An error occured.\n");
            e.printStackTrace();
        }
    }
}
