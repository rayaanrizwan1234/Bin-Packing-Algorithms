import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.io.File;

public class MbWfdBinary {
    ArrayList<Integer> resCap = new ArrayList<>();
    int numOfBins = 0;
    int capacity;

    // Stage 1 MB(WFD) wiht bin limit
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

    void mbWfdHelper(Integer[] items, int capacity) {
        Arrays.sort(items, Collections.reverseOrder());

        this.capacity = capacity;

        int sumOfItems = 0;
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
        numOfBins = min;
    }

    public static void main(String[] args) {
        try {
            File folder = new File("/uolstore/home/users/sc21rr/Desktop/Bin_Packing/algorithms/Bin-Packing-Algorithms/Testing-Data/hard28");
            File[] listOfFiles = folder.listFiles();
            Arrays.sort(listOfFiles);
            long startTime = System.nanoTime();
            for (File file : listOfFiles) {
                System.out.println(file.getName());   
                try (Scanner textReader = new Scanner(file)) {
                    int n = Integer.parseInt(textReader.nextLine());
                    int cap = Integer.parseInt(textReader.nextLine());
                    Integer[] item = new Integer[n];
                    for (int j = 0; j < n; j++) {
                        String data = textReader.nextLine();
                        item[j] = Integer.parseInt(data);
                    }
                    MbWfdBinary res = new MbWfdBinary();
                    res.mbWfdHelper(item, cap);
                    System.out.println("Number of bins "+res.numOfBins+ "\n");
                }
            }

            long endTime = System.nanoTime();
            System.out.println("Time taken: " + (endTime-startTime)/1000000 + " millisecond.");
        } catch (FileNotFoundException e) {
            System.out.print("An error occured.\n");
            e.printStackTrace();
        }
    }
}