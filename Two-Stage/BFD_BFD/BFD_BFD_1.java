import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.io.FileNotFoundException; // Import this class to handle errors
import java.io.File;

public class BFD_BFD_1 {
    ArrayList<Integer> resCap = new ArrayList<>();
    int numOfBins = 0;
    int capacity;

    Integer[] bestFitFirstStage(Integer items[]) {
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
            if (min == capacity + 1) {
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
    boolean bfdToBfd(Integer items[], int capacity, double binRatio) {
        Arrays.sort(items, Collections.reverseOrder());

        int sumOfItems = 0;
        for (Integer i : items) {
            sumOfItems += i;
        }

        this.capacity = capacity;

        double maximumBins = (double) sumOfItems / capacity;
        maximumBins = Math.ceil(maximumBins) * binRatio;
        numOfBins = (int) Math.ceil(maximumBins);

        resCap.addAll(Collections.nCopies(numOfBins, capacity));

        items = bestFitFirstStage(items);
        bestFitSecondStage(items);

        return true;
    }

    // this gives the same result for any ratio you put in because nothing is being
    // changed
    public static void main(String[] args) {
        try {
            int best = 1000000;
            double rat = -1;
            for (Double binRatio = 0.0; binRatio <= 1; binRatio += 0.01) {
                int sum = 0;
                // Reading data from a file
                File binText = new File("Testing-Data/binpack4.txt");
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
                        BFD_BFD_1 res = new BFD_BFD_1();
                        res.bfdToBfd(item, capacity, binRatio);
                        // System.out.println("Bin trigger " +
                        // res.numOfBins + "\n");
                        sum += res.numOfBins;
                    }
                    System.out.println("rat = " + binRatio + " sum is " + sum);
                    if (sum <= best) {
                        best = sum;
                        rat = binRatio;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Best ratio is " + rat);
        } catch (FileNotFoundException e) {
            System.out.print("An error occured.\n");
            e.printStackTrace();
        }
    }
}