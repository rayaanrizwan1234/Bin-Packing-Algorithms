import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.io.File;

// MB(WFD) to FFD with Bin trigger
public class MbWfdToFfd_1 {
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
        for (int i = 0; i < items.length; i++) {
            int j;
            boolean allocated = false;
            for (j = 0; j < numOfBins; j++) {
                if (resCap.get(j) >= items[i]) {
                    final var res = resCap.get(j) - items[i];
                    resCap.set(j, res);
                    allocated = true;
                    break;
                }
            }
            if (j == numOfBins && !allocated) {
                resCap.add(capacity - items[i]);
                numOfBins++;
            }
        }
        return true;
    }

    void hybridMbWfdToFfd(Integer[] items, int capacity, double binRatio) {
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
        firstFit(items);
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
                    MbWfdToFfd_1 res = new MbWfdToFfd_1();
                    res.hybridMbWfdToFfd(item, capacity, 0.80);
                    System.out.println("Number of bins used " + res.numOfBins + "\n");
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
