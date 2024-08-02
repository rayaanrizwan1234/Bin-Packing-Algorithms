import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.io.FileNotFoundException; // Import this class to handle errors
import java.io.File;

class FFD {
    static int firstFit(Integer items[], int n, int cap) {
        int numberOfBins = 0;
        int[] resCap = new int[n];
        resCap[0] = cap;
        for (int item : items) {
            int j;
            for (j = 0; j < numberOfBins; j++) {
                if (resCap[j] >= item) {
                    resCap[j] -= item;
                    break;
                }
            }
            if (j == numberOfBins) {
                resCap[numberOfBins] = cap - item;
                numberOfBins++;
            }
        }
        return numberOfBins;
    }

    static int firstFitDecreasing(Integer item[], int n, int cap) {
        Arrays.sort(item, Collections.reverseOrder());
        return firstFit(item, n, cap);
    }

    public static void main(String[] args) {
        try {
            File folder = new File("/uolstore/home/users/sc21rr/Desktop/Bin_Packing/algorithms/Bin-Packing-Algorithms/Testing-Data/hard28");
            File[] listOfFiles = folder.listFiles();
            Arrays.sort(listOfFiles);
            long startTime = System.nanoTime();
            for (File file : listOfFiles) {
                System.out.println(file.getName());   
                try (Scanner textReader = new Scanner(file)) {
                    int n = Integer.parseInt(textReader.nextLine());
                    int cap = Integer.parseInt(textReader.nextLine());
                    Integer[] items = new Integer[n];
                    for (int j = 0; j < n; j++) {
                        String data = textReader.nextLine();
                        items[j] = Integer.parseInt(data);
                    }
                    System.out.println("n.o bins " + firstFitDecreasing(items, n, cap) + "\n");
                }
            }

            long endTime = System.nanoTime();
            System.out.println("Time taken: " + (endTime-startTime)/1000000 + " millisecond.");
        } catch (FileNotFoundException e) {
            System.out.print("An error occured.\n");
            e.printStackTrace();
        }
}
}
