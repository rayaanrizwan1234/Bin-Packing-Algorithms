import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.io.File;

// MB(WFD) to WFD with Bin trigger
public class MbWfdToWfd_1 {
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

    boolean worstFitDecreasing(Integer items[]) {
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
                resCap.add(capacity - item);
                numOfBins++;
            } else {
                resCap.set(maxBin, max);
            }
        }
        return true;
    }

    void hybridMbWfdToWfd(Integer[] items, int capacity, double binRatio) {
        Arrays.sort(items, Collections.reverseOrder());

        int sumOfItems = 0;
        for (int i : items) {
            sumOfItems += i;
        }

        double maximumBins = (double) sumOfItems / capacity;
        maximumBins = Math.ceil(maximumBins) * binRatio;
        numOfBins = (int) Math.ceil(maximumBins);

        resCap.addAll(Collections.nCopies(numOfBins, capacity));

        this.capacity = capacity;

        items = MbWfd(items);
        worstFitDecreasing(items);
    }

    public static void main(String[] args) {
        try {
            // Reading data from a file
            File binText = new File("../../Testing-Data/binpack4.txt");
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
                    MbWfdToWfd_1 res = new MbWfdToWfd_1();
                    res.hybridMbWfdToWfd(item, capacity, 0.67);
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
        // MbWfdToBfd_1 res = new MbWfdToBfd_1();
        // res.hybridMbWfdToFfd(items, capacity, 0.5);
    }
}
