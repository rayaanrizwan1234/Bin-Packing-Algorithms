import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.io.FileNotFoundException; // Import this class to handle errors
import java.io.File;

class BFD_MBWFD_2 {
    ArrayList<Integer> resCap = new ArrayList<>();
    int numOfBins = 0;

    boolean bestFit(Integer items[], int capacity) {
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
    boolean bfdToMbWfd(Integer items[], int capacity, double ItemRatio) {
        Arrays.sort(items, Collections.reverseOrder());

        int numItems1 = (int) Math.ceil(items.length * ItemRatio);
        final var items1 = Arrays.copyOfRange(items, 0, numItems1);
        var items2 = Arrays.copyOfRange(items, numItems1, items.length);

        bestFit(items1, capacity);

        int numOfBinsFirstStage = numOfBins;

        ArrayList<Integer> firstStageResCap = new ArrayList<>(resCap);

        int sumOfItems = 0;
        for (Integer i : items) {
            sumOfItems += i;
        }

        // run binary search over different m values
        int lowerBound = (int) Math.ceil((double) sumOfItems / capacity);
        int upperBound = items.length;

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
                success = mbWfd(items2);
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

    public static void main(String[] args) {
        try {
            int best = 1000000;
            double rat = -1;
            for (Double itemRatio = 0.0; itemRatio <= 1; itemRatio += 0.01) {
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
                        BFD_MBWFD_2 res = new BFD_MBWFD_2();
                        res.bfdToMbWfd(item, capacity, itemRatio);
                        sum += res.numOfBins;
                    }
                    System.out.println("rat = " + itemRatio + " sum is " + sum);
                    if (sum <= best) {
                        best = sum;
                        rat = itemRatio;
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
                    BFD_MBWFD_2 res = new BFD_MBWFD_2();
                    res.bfdToMbWfd(item, capacity, rat);
                    System.out.println("num of bins " + res.numOfBins);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.print("An error occured.\n");
            e.printStackTrace();
        }
        // Integer[] item = {6, 6, 5, 5, 4, 4, 3, 3, 1};
        // BFD_MBWFD_2 res = new BFD_MBWFD_2();
        // res.bfdToMbWfd(item, 10, 0.5);
    }
}