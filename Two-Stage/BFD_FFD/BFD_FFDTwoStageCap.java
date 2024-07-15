import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.io.FileNotFoundException; // Import this class to handle errors
import java.io.File;
import java.util.stream.Stream;


class BFD_FFDTwoStageCap{
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

    Integer[] bestFitItemCapacityTrigger(Integer items[], int cap) {
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
                if (!allocated && (cap >= items[i])) {
                    resCap.add(cap - items[i]);
                    numOfBins++;
                }
                else if (!allocated && (cap < items[i])){
                    notAllocatedItems.add(items[i]);
                }
            }
            else{
                final var res = resCap.get(bi) - items[i];
                resCap.set(bi, res);
            }
        }
        return notAllocatedItems.toArray(new Integer[0]);
    }

    // Method used for setting restrained bin capacity and n.o bins and/or n.o items
    boolean BFD_FFDCapacity(Integer items[], int capacity, double binRatio, double itemRatio,
            double capacityRatio) {
        Arrays.sort(items, Collections.reverseOrder());

        int newCap = (int) Math.ceil(capacity * capacityRatio);

        Integer[] items1 = null;
        // Splits bins based on given trigger value
        if (binRatio != 0) {
            int sumOfItems = 0;
            for (Integer i : items) {
                sumOfItems += i;
            }
            double maximumBins = (double) sumOfItems / capacity;
            maximumBins = Math.ceil(maximumBins) * binRatio;
            numOfBins = (int) Math.ceil(maximumBins);
            resCap.addAll(Collections.nCopies(numOfBins, newCap));
        }

        // splits items based on given trigger value
        if (itemRatio != 0) {
            int numItems1 = (int) Math.ceil(items.length * itemRatio);

            items1 = Arrays.copyOfRange(items, 0, numItems1);
            items = Arrays.copyOfRange(items, numItems1, items.length);
        }

        // Checks which triggers are set and runs accordingly
        if (binRatio != 0 && itemRatio != 0) {
            // gets the unallocated items from the first stage and passes it onto the second
            // stage
            final var unAllocated = bestFit(items1, newCap, 1);
            Integer[] resultArray = Stream.concat(Arrays.stream(unAllocated), Arrays.stream(items))
                    .toArray(Integer[]::new);
            items = resultArray;
        } else if (binRatio == 0 && itemRatio != 0) {
            final var unAllocated = bestFitItemCapacityTrigger(items1, newCap);
            Integer[] resultArray = Stream.concat(Arrays.stream(unAllocated), Arrays.stream(items))
                    .toArray(Integer[]::new);
            items = resultArray;
        } else {
            items = bestFit(items, newCap, 1);
        }

        for (int i = 0; i < resCap.size(); i++) {
            int cap = resCap.get(i) + capacity - newCap;
            resCap.set(i, cap);
        }
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
                    BFD_FFDTwoStageCap ffdTwoStageBinItemCapacityTrigger = new BFD_FFDTwoStageCap();
                    ffdTwoStageBinItemCapacityTrigger.BFD_FFDCapacity(item, capacity, 0.4, 0.5, 0.6);
                    System.out.print("Bin Item Capacity trigger " +
                    ffdTwoStageBinItemCapacityTrigger.numOfBins + "\n");
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