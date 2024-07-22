import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.io.File;

// FFD to BFD with bin trigger
public class FFD_BFD_1 {
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
    boolean ffdToBfd(Integer items[], int capacity, double binRatio) {
        Arrays.sort(items, Collections.reverseOrder());
        int sumOfItems = 0;
        for (Integer i : items) {
            sumOfItems += i;
        }
            
        double maximumBins = (double) sumOfItems / capacity;
        maximumBins = Math.ceil(maximumBins) * binRatio;
        numOfBins = (int) Math.ceil(maximumBins);
        resCap.addAll(Collections.nCopies(numOfBins, capacity));

        this.capacity = capacity;

        items = firstFit(items);
        bestFitDecreasing(items);

        return true;
    }

    public static void main(String[] args) {
        try {
            // Reading data from a file
            File binText = new File("Testing-Data/binpack4.txt");
            try (Scanner textReader = new Scanner(binText)) {
                int problems = Integer.parseInt(textReader.nextLine());
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
                    FFD_BFD_1 hybrid = new FFD_BFD_1();
                    hybrid.ffdToBfd(item, capacity, 0.5);
                    System.out.println("Bin trigger " +hybrid.numOfBins + "\n");
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            System.out.print("An error occured.\n");
            e.printStackTrace();
        }
    }
}
