import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.io.File;

public class MBWFD {
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
        System.out.println("lower bound = "+lowerBound+"\n");
        numOfBins = lowerBound - 1;

        Boolean success = false;
        while (!success) {
            numOfBins += 1;
            resCap.clear();
            resCap.addAll(Collections.nCopies(numOfBins, capacity));
            success = mbWfd(items);
        }
    }

    public static void main(String[] args) {
        try {
                // Reading data from a file
                long startTime = System.nanoTime();
                for (int fileIndex = 2000; fileIndex <= 4000; fileIndex += 100) {
                    String file = "/uolstore/home/users/sc21rr/Desktop/Bin_Packing/algorithms/Bin-Packing-Algorithms/Testing-Data/Complexity Test/" + fileIndex;
                    File binText = new File(file);
                    
                    try (Scanner textReader = new Scanner(binText)) {
                            System.out.println("Problem:" + fileIndex);
                            int n = Integer.parseInt(textReader.nextLine());
                            int capacity = Integer.parseInt(textReader.nextLine());
                            Integer[] item = new Integer[n];
                            for (int j = 0; j < n; j++) {
                                String data = textReader.nextLine();
                                item[j] = Integer.parseInt(data);
                            }
                            // Testing objects
                            MBWFD res = new MBWFD();
                            res.mbWfdHelper(item, capacity);
                            // System.out.println("Number of bins "+res.numOfBins+ "\n");
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                long endTime = System.nanoTime();
                System.out.println("Time taken: " + (endTime - startTime) / 1000000  + "ms\n");
            }
         catch (FileNotFoundException e) {
            System.out.print("An error occured.\n");
            e.printStackTrace();
        }
        // Integer[] items = new Integer[] {6, 6, 5, 5, 4, 3, 3, 2, 1, 1};
        // int capacity = 10;
        // MBWFD res = new MBWFD();
        // res.mbWfdHelper(items, capacity);
    } 
}