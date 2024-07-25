import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.io.File;

// FFD -> BFD cap & bin trigger
public class FFD_BFD_3 {
    ArrayList<Integer> resCap = new ArrayList<>();
    int numOfBins = 0;
    int capacity;

    Integer[] firstFit(Integer items[]) {
        ArrayList<Integer> notAllocatedItems = new ArrayList<>();
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
                notAllocatedItems.add(item);
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

    // This method is used to set triggers for number of bins or the number of items
    boolean ffdToBfd(Integer items[], int capacity, double capRatio, double binRatio) {
        Arrays.sort(items, Collections.reverseOrder());

        int newCap = (int) Math.ceil(capacity * capRatio);

        int sumOfItems = 0;
        for (Integer i : items) {
            sumOfItems += i;
        }

        double maximumBins = (double) sumOfItems / capacity;
        maximumBins = Math.ceil(maximumBins) * binRatio;
        numOfBins = (int) Math.ceil(maximumBins);
        resCap.addAll(Collections.nCopies(numOfBins, newCap));

        this.capacity = capacity;

        items = firstFit(items);

        for (int i = 0; i < resCap.size(); i++) {
            int cap = resCap.get(i) + capacity - newCap;
            resCap.set(i, cap);
        }

        bestFitDecreasing(items);

        return true;
    }

    public static void main(String[] args) {
        try {
            // Reading data from a file
            File binText = new File("Testing-Data/binpack2.txt");
            try (Scanner textReader = new Scanner(binText)) {
                int problems = Integer.parseInt(textReader.nextLine());
                // Record the start time
                long startTime = System.nanoTime();
                for (int i = 0; i < problems; i++) {
                    System.out.print("Problem:" + textReader.nextLine() + "\n");
                    String data = textReader.nextLine().trim();
                    int capacity = Integer.parseInt(data.substring(0, 3));
                    int n = Integer.parseInt(data.substring(4, 7));
                    Integer[] item = new Integer[n];
                    for (int j = 0; j < n; j++) {
                        data = textReader.nextLine();
                        item[j] = Integer.parseInt(data);
                    }
                    // Testing objects
                    FFD_BFD_3 hybrid = new FFD_BFD_3();
                    hybrid.ffdToBfd(item, capacity, 0.59, 0.6);
                    System.out.println("Item trigger " + hybrid.numOfBins + "\n");
                }
                long endTime = System.nanoTime();
                // Calculate the elapsed time
                long elapsedTime = endTime - startTime;

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
