import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.io.FileNotFoundException; // Import this class to handle errors
import java.io.File;
import java.util.stream.Stream;


class BFD_FFDTwoStageItem {
    ArrayList<Integer> resCap = new ArrayList<>();
    int numOfBins = 0;

    Integer[] bestFit(Integer items[], int cap) {
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
                resCap.add(cap - items[i]);
                numOfBins++;
            }
            else{
                final var res = resCap.get(bi) - items[i];
                resCap.set(bi, res);
            }
        }
        return notAllocatedItems.toArray(new Integer[0]);
    }

    Integer[] firstFit(Integer items[], int cap) {
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
            if (j == numOfBins){
                resCap.add(cap - items[i]);
                numOfBins++;
            }
        }
        return notAllocatedItems.toArray(new Integer[0]);
    }

    // This method is used to set triggers for number of bins or the number of items
    boolean BFD_FFDItem(Integer items[], int capacity, double ItemRatio) {
        Arrays.sort(items, Collections.reverseOrder());
        int numItems1 = (int) Math.ceil(items.length * ItemRatio);

        final var items1 = Arrays.copyOfRange(items, 0, numItems1);
        final var items2 = Arrays.copyOfRange(items, numItems1, items.length);

        bestFit(items1, capacity);
        firstFit(items2, capacity);
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
                    BFD_FFDTwoStageItem bfdffdTwoStageItemTrigger = new BFD_FFDTwoStageItem();
                    bfdffdTwoStageItemTrigger.BFD_FFDItem(item, capacity, 0.65);
                    System.out.print("Item trigger " +
                    bfdffdTwoStageItemTrigger.numOfBins + "\n");
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