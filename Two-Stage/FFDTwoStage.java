import java.util.Arrays;
// import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;
// import java.util.List;
// import java.util.Scanner;
// import java.io.FileNotFoundException; // Import this class to handle errors
// import java.io.File;

class FFDTwoStage {
    static ArrayList<Integer> resCap;
    static Integer[] firstFit(Integer items[], int cap, int numOfBins, int stage) {
        resCap = new ArrayList<>(Collections.nCopies(numOfBins, cap));
        final var n  = items.length;
        for (int i = 0; i < n; i++) {
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
                System.out.println("item is "+items[i]);
                return Arrays.copyOfRange(items, i, items.length);
            }
            // if (stage == 2 && j == numberOfBins) {
            //     resCap[numberOfBins] = cap - item[i];
            //     numberOfBins++;
            // }
        }
        return new Integer[0];
    }

    // static int firstFitDecreasing(Integer item[], int n, int capacity) {
    //     Arrays.sort(item, Collections.reverseOrder());
    //     return firstFit(item, n, capacity);
    // }

    static boolean firstFitDecreasingBinOrItem(Integer items[], int capacity, double binRatio, boolean binTrigger) {
        double maximumBins = 0;
        if (binTrigger) {
            int sumOfItems = 0;
            for (Integer i : items) {
                sumOfItems += i;
            }
            maximumBins = (double) sumOfItems / capacity;
            maximumBins = Math.ceil(maximumBins);
            maximumBins *= binRatio;
            maximumBins = Math.ceil(maximumBins);
        }

        Arrays.sort(items, Collections.reverseOrder());
        int stage  = 1;
        System.out.println("max "+maximumBins);
        items = firstFit(items, capacity, (int) maximumBins, stage);
        // implement second stage here
        return true;
    }

    // static int firstFitDecreasingCapacity(Integer item[], int n, int capacity, double m, double numOfItems, double binCapacity) {
    //     Arrays.sort(item, Collections.reverseOrder());
    //     return firstFit(item, n, capacity);
    // }

    public static void main(String[] args) {
        // try {
        //     File binText = new File("../Testing-Data/binpack1.txt");
        //     try (Scanner textReader = new Scanner(binText)) {
        //         int problems = Integer.parseInt(textReader.nextLine());
        //         for (int i = 0; i < problems; i++) {
        //             System.out.print("Problem:" + textReader.nextLine() + "\n");
        //             String data = textReader.nextLine().trim();
        //             int capacity = Integer.parseInt(data.substring(0, 3));
        //             int n = Integer.parseInt(data.substring(4, 7));
        //             Integer[] item = new Integer[n];
        //             for (int j = 0; j < n; j++) {
        //                 data = textReader.nextLine();
        //                 item[j] = Integer.parseInt(data);
        //             }
        //             final var res = firstFitDecreasingBinOrItem(item, capacity, 0.5, true);
        //             System.out.print("Number of bins required in First Fit Decreasing: " + res + "\n");
        //         }
        //     } catch (NumberFormatException e) {
        //         e.printStackTrace();
        //     }
        // } catch (FileNotFoundException e) {
        //     System.out.print("An error occured.\n");
        //     e.printStackTrace();
        // }
        Integer[] items = new Integer[]{1, 2, 3, 4, 5, 6};
        int cap = 10;
        final var res = firstFitDecreasingBinOrItem(items, cap, 0.5, true);
        System.out.println("Number of bins required in First Fit Decreasing: " + res + "\n");

    }
}