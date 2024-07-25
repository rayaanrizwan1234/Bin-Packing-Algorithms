import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.io.File;

// MB(WFD) to BFD with item trigger
public class MbWfdToBfd_2 {
    ArrayList<Integer> resCap = new ArrayList<>();
    int numOfBins = 0;
    int capacity;

    // Stage 1 MB(WFD) wiht bin limit
    Boolean MbWfd(Integer[] items) {
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

    void hybridMbWfdToBfd(Integer[] items, int capacity, double itemRatio) {
        Arrays.sort(items, Collections.reverseOrder());

        this.capacity = capacity;

        int numItems1 = (int) Math.ceil(items.length * itemRatio);

        final var items1 = Arrays.copyOfRange(items, 0, numItems1);
        final var items2 = Arrays.copyOfRange(items, numItems1, items.length);

        int sumOfItems = 0;
        for (Integer i : items1) {
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
            final var success = MbWfd(items1);

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

        bestFitDecreasing(items2);
    }

    public static void main(String[] args) {
        try {
            // Reading data from a file
            File binText = new File("Testing-Data/binpack4.txt");
            try (Scanner textReader = new Scanner(binText)) {
                int problems = Integer.parseInt(textReader.nextLine());
                long startTime = System.nanoTime();
                for (int i = 0; i < problems; i++) {
                    System.out.print("Problem:" + textReader.nextLine() + "\n");
                    String data = textReader.nextLine().trim();
                    int capacity = Integer.parseInt(data.substring(0, 3));
                    int n = Integer.parseInt(data.substring(4, 8));
                    Integer[] item = new Integer[n];
                    for (int j = 0; j < n; j++) {
                        data = textReader.nextLine();
                        item[j] = Integer.parseInt(data);
                    }
                    // Testing objects
                    MbWfdToBfd_2 res = new MbWfdToBfd_2();
                    res.hybridMbWfdToBfd(item, capacity, 0.4);
                    System.out.println("Number of bins used " + res.numOfBins);
                }
                long endTime = System.nanoTime();
                // Calculate the elapsed time in nanoseconds
                long elapsedTime = endTime - startTime;

                // Convert elapsed time to milliseconds (optional)
                double elapsedTimeInMillis = elapsedTime / 1_000_000.0;

                // Print the elapsed time
                System.out.println("Elapsed time in milliseconds: " + elapsedTimeInMillis);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            System.out.print("An error occured.\n");
            e.printStackTrace();
        }
        // Integer[] items = new Integer[] {6, 6, 5, 5, 4, 3, 3, 2, 1, 1};
        // int capacity = 10;
        // MbWfdToFfd_2 res = new MbWfdToFfd_2();
        // res.hybridMbWfdToFfd(items, capacity, 0.67);
    }
}
