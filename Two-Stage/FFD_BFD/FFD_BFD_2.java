import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.io.File;

// FFD to BFD with item trigger
public class FFD_BFD_2 {
    ArrayList<Integer> resCap = new ArrayList<>();
    int numOfBins = 0;
    int capacity;

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
    boolean ffdToBfd(Integer items[], int capacity, double itemRatio) {
        Arrays.sort(items, Collections.reverseOrder());

        int numItems1 = (int) Math.ceil(items.length * itemRatio);

        final var items1 = Arrays.copyOfRange(items, 0, numItems1);
        final var items2 = Arrays.copyOfRange(items, numItems1, items.length);

        this.capacity = capacity;

        firstFit(items1);
        bestFitDecreasing(items2);

        return true;
    }

    public static void main(String[] args) {
        try {
            // Reading data from a file
            File binText = new File("Testing-Data/binpack3.txt");
            try (Scanner textReader = new Scanner(binText)) {
                int problems = Integer.parseInt(textReader.nextLine());
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
                    FFD_BFD_2 hybrid = new FFD_BFD_2();
                    hybrid.ffdToBfd(item, capacity, 0.75);
                    System.out.println("Item trigger " +hybrid.numOfBins + "\n");
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
