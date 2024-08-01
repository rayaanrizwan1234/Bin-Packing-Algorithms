import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.File;

class BFD {
    static int bestFit(Integer item[], int n, int cap) {
        int numberOfBins = 0;
        int []resCap = new int[n];
        for (int i = 0; i < n; i++) {
            int j;
            int min = cap + 1;
            int bi = 0;
            for (j = 0; j < numberOfBins; j++) {
                if ((resCap[j] >= item[i]) && resCap[j] - item[i] < min){
                    bi = j;
                    min = resCap[j] - item[i];
                }
            }
            if (min == cap + 1) {
                resCap[numberOfBins] = cap - item[i];
                numberOfBins++;
            }
            else
                resCap[bi] -= item[i];
        }
        return numberOfBins;
    }

    static int bestFitDecreasing(Integer item[], int n, int cap) {
        Arrays.sort(item, Collections.reverseOrder());
        return bestFit(item, n, cap);
    }

    public static void main( String[] args ) {
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
                    Integer[] item = new Integer[n];
                    for (int j = 0; j < n; j++) {
                        String data = textReader.nextLine();
                        item[j] = Integer.parseInt(data);
                    }
                    System.out.println("n.o bins " + bestFitDecreasing(item, n, cap) + "\n");
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