import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.stream.Stream;
import java.io.File;

public class MbWfdToBfd_3 {
    ArrayList<Integer> resCap = new ArrayList<>();
    int numOfBins = 0;
    int capacity;

    // Stage 1 MB(WFD) with bin limit
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

    void hybridMbWfdToBfd(Integer[] items, int capacity, double capacityRatio, double itemRatio, double binRatio) {
        Arrays.sort(items, Collections.reverseOrder());

        // set new reduced capacity
        int newCapacity = (int) Math.ceil(capacity * capacityRatio);
        this.capacity = newCapacity;
        System.out.println("cap = " + newCapacity);

        int numItems1 = (int) Math.ceil(items.length * itemRatio);

        final var items1 = Arrays.copyOfRange(items, 0, numItems1);
        Integer[] items2 = Arrays.copyOfRange(items, numItems1, items.length);

        int sumOfItems = 0;
        for (Integer i : items1) {
            sumOfItems += i;
        }

        double lowerBound = (double) sumOfItems / capacity;
        lowerBound = Math.ceil(lowerBound) * binRatio;
        numOfBins = (int) Math.ceil(lowerBound);
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

        bestFitDecreasing(items2);
    }

    public static void main(String[] args) {
        try {
            // Reading data from a file
            File binText = new File(
                    "/uolstore/home/users/sc21rr/Desktop/Bin_Packing/algorithms/Bin-Packing-Algorithms/Testing-Data/binpack4.txt");
            int capacity = 0;
            try (Scanner textReader = new Scanner(binText)) {
                int problems = Integer.parseInt(textReader.nextLine());
                // Record the start time
                Integer[][] itemList = new Integer[problems][1000];
                for (int i = 0; i < problems; i++) {
                    System.out.print("Problem:" + textReader.nextLine() + "\n");
                    String data = textReader.nextLine().trim();
                    capacity = Integer.parseInt(data.substring(0, 3));
                    int n = Integer.parseInt(data.substring(4, 8));
                    for (int j = 0; j < n; j++) {
                        data = textReader.nextLine();
                        itemList[i][j] = Integer.parseInt(data);
                    }
                    // Testing objects

                }
                long startTime = System.nanoTime();
                int best = 1000000000;
                double br = -8;
                double cr = -8;
                double ir = -1;
                // for (Double c = 0.85; c <= 1; c += 0.01) {
                // for (Double b = 0.85; b <= 1; b += 0.01) {
                // for (Double ira = 0.35; ira <= 1; ira += 0.01) {
                int sum = 0;
                // Calculate the elapsed time
                for (int i = 0; i < problems; i++) {
                    MbWfdToBfd_3 hybrid = new MbWfdToBfd_3();
                    hybrid.hybridMbWfdToBfd(itemList[i], capacity, 1, 0.45, 1);
                    System.out.println("num of bins " + hybrid.numOfBins);
                    sum += hybrid.numOfBins;
                }
                // System.out.println("cap = " +c+" bin = "+b+" item = "+ira);
                if (sum <= best) {
                    best = sum;
                    // br = b;
                    // cr = c;
                    // ir = ira;
                }
                // }
                // }
                // }
                System.out.println(br + " " + cr + " " + ir);
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
