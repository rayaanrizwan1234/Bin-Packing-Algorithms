import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.io.FileNotFoundException; // Import this class to handle errors
import java.io.File;
import java.util.stream.Stream;


class BFD_FFDTwoStageBin {
    ArrayList<Integer> resCap = new ArrayList<>();
    int numOfBins = 0;

    Integer[] bestFit(Integer items[], int cap, int stage) {
        ArrayList<Integer> notAllocatedItems = new ArrayList<>();
        for (int i = 0; i < items.length; i++) {
            int j;
            int min = cap + 1;
            int bi = 0;
            boolean allocated = false;
            for (j = 0; j < numOfBins; j++) {
                if ((resCap.get(j) >= items[i]) && resCap.get(j) - items[i] < min){
                    bi = j;
                    min = resCap.get(j) - items[i];
                    allocated = true;
                }
            }
            if (min == cap + 1) {
                if (stage == 1 && !allocated) {
                    notAllocatedItems.add(items[i]);
                }
                if (stage == 2 && !allocated){
                    resCap.add(cap - items[i]);
                    numOfBins++;
                }
            }
            else{
                final var res = resCap.get(bi) - items[i];
                resCap.set(bi, res);
            }
        }
        return notAllocatedItems.toArray(new Integer[0]);
    }

    Integer[] firstFit(Integer items[], int cap, int stage) {
        ArrayList<Integer> notAllocatedItems = new ArrayList<>();
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
                notAllocatedItems.add(items[i]);
            }
            if (stage == 2 && j == numOfBins && !allocated) {
                resCap.add(cap - items[i]);
                numOfBins++;
            }
        }
        return notAllocatedItems.toArray(new Integer[0]);
    }

    // This method is used to set triggers for number of bins or the number of items
    boolean firstFitDecreasingBinOrItem(Integer items[], int capacity, double binRatio) {
        Arrays.sort(items, Collections.reverseOrder());
        int sumOfItems = 0;
        for (Integer i : items) {
            sumOfItems += i;
        }
            
        double maximumBins = (double) sumOfItems / capacity;
        maximumBins = Math.ceil(maximumBins) * binRatio;
        numOfBins = (int) Math.ceil(maximumBins);
        resCap.addAll(Collections.nCopies(numOfBins, capacity));
        items = bestFit(items, capacity, 1);
        firstFit(items, capacity, 2);

        return true;
    }

    public static void main(String[] args) {
        try {
            // Reading data from a file
            File binText = new File("../../Testing-Data/binpack4.txt");
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
                    BFD_FFDTwoStageBin ffdTwoStageBinTrigger = new BFD_FFDTwoStageBin();
                    ffdTwoStageBinTrigger.firstFitDecreasingBinOrItem(item, capacity, 0.75);
                    System.out.print("Bin trigger " +
                    ffdTwoStageBinTrigger.numOfBins + "\n");
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