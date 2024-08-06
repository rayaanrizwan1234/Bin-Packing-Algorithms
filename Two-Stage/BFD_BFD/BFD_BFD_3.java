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
            }
            else if (min == capacity + 1) {
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

    // this gives the same result for any ratio you put in because nothing is being changed
    public static void main(String[] args) {
        try {
            int best = 1000000;
            double binRat = -1;
            double capRat = -1;
            double itemRat = -1;
            // for (Double itemRatio = 0.0; itemRatio <= 1; itemRatio += 0.01) {
                // for (Double capRatio = 0.0; capRatio <= 1; capRatio += 0.01) {
                //     for (Double binRatio = 0.0; binRatio <= 1; binRatio += 0.01) {
                        int sum = 0;
                        // Reading data from a file
                        File binText = new File("../../Testing-Data/binpack4.txt");
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
                                BFD_BFD_3 res = new BFD_BFD_3();
                                res.bfdToBfd(item, capacity, 0.5, 0.85, 0.92);
                                System.out.println("Bin trigger " +
                                        res.numOfBins + "\n");
                                sum += res.numOfBins;
                            }
                            // System.out.println("capRatio = "+capRatio+ " binratio = " + binRatio + " item ratio " + 0.0 + " sum is "+sum);
                            // if (sum <= best) {
                            //     best = sum;
                            //     binRat = binRatio;
                            //     capRat = capRatio;
                            //     itemRat = 0.0;
                            // }
                        } 
                        catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
            // }
            System.out.println(capRat + " " + binRat + " " + itemRat);
        } catch (FileNotFoundException e) {
            System.out.print("An error occured.\n");
            e.printStackTrace();
        }
        // try {
        //     // Reading data from a file
        //     File binText = new File("/uolstore/home/users/sc21rr/Desktop/Bin_Packing/algorithms/Bin-Packing-Algorithms/Testing-Data/binpack4.txt");
        //     try (Scanner textReader = new Scanner(binText)) {
        //         int problems = Integer.parseInt(textReader.nextLine());
        //         long startTime = System.nanoTime();
        //         for (int i = 0; i < problems; i++) {
        //             System.out.print("Problem:" + textReader.nextLine() + "\n");
        //             String data = textReader.nextLine().trim();
        //             int capacity = Integer.parseInt(data.substring(0, 3));
        //             int n = Integer.parseInt(data.substring(4, 8));
        //             Integer[] item = new Integer[n];
        //             for (int j = 0; j < n; j++) {
        //                 data = textReader.nextLine();
        //                 item[j] = Integer.parseInt(data);
        //             }
        //             // Testing objects
        //             BFD_BFD_3 res = new BFD_BFD_3();
        //             res.bfdToBfd(item, capacity, 0.59, 0.61, 0.62);
        //             System.out.println("Number of bins used " + res.numOfBins);
        //         }
        //         long endTime = System.nanoTime();
        //         // Calculate the elapsed time in nanoseconds
        //         long elapsedTime = endTime - startTime;

        //         // Convert elapsed time to milliseconds (optional)
        //         double elapsedTimeInMillis = elapsedTime / 1_000_000.0;

        //         // Print the elapsed time
        //         System.out.println("Elapsed time in milliseconds: " + elapsedTimeInMillis);
        //     } catch (NumberFormatException e) {
        //         e.printStackTrace();
        //     }
        // } catch (FileNotFoundException e) {
        //     System.out.print("An error occured.\n");
        //     e.printStackTrace();
        // }

        //         Integer[] item = new Integer[] {6, 6, 5, 5, 4, 3, 3, 2, 1, 1};
        // int capacity = 10;
        // BFD_BFD_3 res = new BFD_BFD_3();
        // res.bfdToBfd(item, capacity, 0.6, 0.4, 0.5);
        // System.out.println("Number of bins used " + res.numOfBins);
    }
}
