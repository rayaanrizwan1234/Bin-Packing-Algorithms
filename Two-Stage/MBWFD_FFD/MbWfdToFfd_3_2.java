import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.stream.Stream;
import java.io.File;

// MB(WFD) to FFD with capacity trigger
public class MbWfdToFfd_3_2 {
    ArrayList<Integer> resCap = new ArrayList<>();
    int numOfBins = 0;
    int capacity;

    // Stage 1 MB(WFD) wiht bin limit
    Integer[] MbWfd(Integer[] items) {
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

        return notAllocatedItems.toArray(new Integer[0]);
    }

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

    void hybridMbWfdToFfd(Integer[] items, int capacity, double capacityRatio, double itemRatio) {
        Arrays.sort(items, Collections.reverseOrder());

        // set new reduced capacity
        int newCapacity = (int) Math.ceil(capacity * capacityRatio);
        this.capacity = newCapacity;

        int numItems1 = (int) Math.ceil(items.length * itemRatio);

        final var items1 = Arrays.copyOfRange(items, 0, numItems1);
        Integer[] items2 = Arrays.copyOfRange(items, numItems1, items.length);

        int sumOfItems = 0;
        for (Integer i : items1) {
            sumOfItems += i;
        }

        int lowerBound = (int) Math.ceil((double) sumOfItems / capacity);
        numOfBins = lowerBound;
        resCap.addAll(Collections.nCopies(numOfBins, newCapacity));

        final var unAllocated = MbWfd(items1);
        Integer[] resultArray = Stream.concat(Arrays.stream(unAllocated), Arrays.stream(items2))
        .toArray(Integer[]::new);
        Arrays.sort(resultArray, Collections.reverseOrder());
        items2 = resultArray;

        // set capacity back to the original capacity
        this.capacity = capacity;

        // increase the bins capacity by the remaining amount
        for (int i = 0; i < resCap.size(); i++) {
            int cap = resCap.get(i) + capacity - this.capacity;
            resCap.set(i, cap);
        }

        firstFit(items2);
    }

    public static void main(String[] args) {
            try {
            int min = 100000;
            double bestCap = 0;
            double bestBin = 0;
            for (double cap = 0; cap <= 1; cap+=0.01) {
                for (double bin = 0; bin <= 1; bin += 0.01) {
                    int sum  = 0;
                    File binText = new File("Testing-Data/binpack4.txt");
                    try (Scanner textReader = new Scanner(binText)) {
                        int problems = Integer.parseInt(textReader.nextLine());
                        // Record the start time
                        long startTime = System.nanoTime();
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
                            MbWfdToFfd_3_2 hybrid = new MbWfdToFfd_3_2();
                            hybrid.hybridMbWfdToFfd(item, capacity, cap, bin);
                            sum += hybrid.numOfBins;
                        }
                        long endTime = System.nanoTime();
                        // Calculate the elapsed time
                        long elapsedTime = endTime - startTime;
        
                        // System.out.println("Execution time in ms: " + elapsedTime / 1000000);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    System.out.println(cap + " " + sum + " " + bin);
                    if (sum <= min) {
                        min = sum;
                        bestCap = cap;
                        bestBin = bin;
                    }
                }
            }
            System.out.println(bestCap + " " + bestBin);
        } catch (FileNotFoundException e) {
            System.out.print("An error occured.\n");
            e.printStackTrace();
        }
        // try {
        //     // Reading data from a file
        //     File binText = new File("Testing-Data/binpack4.txt");
        //     try (Scanner textReader = new Scanner(binText)) {
        //         int problems = Integer.parseInt(textReader.nextLine());
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
        //             MbWfdToFfd_3_2 res = new MbWfdToFfd_3_2();
        //             res.hybridMbWfdToFfd(item, capacity, 0.5, 0.75);
        //             System.out.println("Number of bins used " + res.numOfBins);
        //         }
        //     } catch (NumberFormatException e) {
        //         e.printStackTrace();
        //     }
        // } catch (FileNotFoundException e) {
        //     System.out.print("An error occured.\n");
        //     e.printStackTrace();
        // }
        // Integer[] items = new Integer[] {6, 6, 5, 5, 4, 3, 3, 2, 1, 1};
        // int capacity = 10;
        // MbWfdToFfd_3 res = new MbWfdToFfd_3();
        // res.hybridMbWfdToFfd(items, capacity, 0.5, 0.75);
        // System.out.println("Number of bins used "+ res.numOfBins);
    }
}
