import java.util.Arrays;
// import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.io.FileNotFoundException; // Import this class to handle errors
import java.io.File;

class FFDTwoStage {
    ArrayList<Integer> resCap = new ArrayList<>();
    int numOfBins = 0;

    Integer[] firstFit(Integer items[], int cap, int stage) {
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
            if (stage == 1 && !allocated) {
                return Arrays.copyOfRange(items, i, items.length);
            }
            if (stage == 2 && j == numOfBins && !allocated) {
                resCap.add(cap - items[i]);
                numOfBins++;
            }
        }
        return new Integer[0];
    }

    boolean firstFitDecreasingBinOrItem(Integer items[], int capacity, double binOrItemRatio, boolean binTrigger) {
        Arrays.sort(items, Collections.reverseOrder());

        double maximumBins = 0;
        if (binTrigger) {
            int sumOfItems = 0;
            for (Integer i : items) {
                sumOfItems += i;
            }
            maximumBins = (double) sumOfItems / capacity;
            maximumBins = Math.ceil(maximumBins);
            maximumBins *= binOrItemRatio;
            maximumBins = Math.ceil(maximumBins);
            numOfBins = (int) maximumBins;

            resCap.addAll(Collections.nCopies(numOfBins, capacity));

            items = firstFit(items, capacity, 1);
            firstFit(items, capacity, 2);
        } else {
            final var numItemsDouble1 = items.length * binOrItemRatio;
            int numItems1 = (int) numItemsDouble1;

            final var items1 = Arrays.copyOfRange(items, 0, numItems1);
            final var items2 = Arrays.copyOfRange(items, numItems1, items.length);

            firstFit(items1, capacity, 2);
            firstFit(items2, capacity, 2);
        }

        return true;
    }

    // static int firstFitDecreasingCapacity(Integer item[], int n, int capacity,
    // double m, double numOfItems, double binCapacity) {
    // Arrays.sort(item, Collections.reverseOrder());
    // return firstFit(item, n, capacity);
    // }

    public static void main(String[] args) {
        // try {
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
        //             FFDTwoStage ffdTwoStage = new FFDTwoStage();
        //             ffdTwoStage.firstFitDecreasingBinOrItem(item, capacity, 0.5, false);
        //             System.out.print("Number of bins required in First Fit Decreasing: " + ffdTwoStage.numOfBins+ "\n");
        //         }
        //     } catch (NumberFormatException e) {
        //         e.printStackTrace();
        //     }
        // } catch (FileNotFoundException e) {
        //     System.out.print("An error occured.\n");
        //     e.printStackTrace();
        // }
        Integer[] items = new Integer[] { 1, 2, 3, 4, 5, 6, 2, 10 };
        int cap = 10;
        FFDTwoStage ffdTwoStage = new FFDTwoStage();
        ffdTwoStage.firstFitDecreasingBinOrItem(items, cap, 0.5, false);
        System.out.println("Number of bins required in First Fit Decreasing: " + ffdTwoStage.numOfBins + "\n");
    }
}