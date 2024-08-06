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
            // File binText = new File("Testing-Data/binpack3.txt");
            // try (Scanner textReader = new Scanner(binText)) {
                
            //     int problems = Integer.parseInt(textReader.nextLine());
            //     Integer[][] itemList = new Integer[problems][500];
            //     for (int i = 0; i < problems; i++) {
            //         textReader.nextLine();
            //         String data = textReader.nextLine().trim();
            //         int cap = Integer.parseInt(data.substring(0, 3));
            //         int n = Integer.parseInt(data.substring(4, 7));
            //         Integer[] item = new Integer[n];
            //         for (int j = 0; j < n; j++) {
            //             data = textReader.nextLine();
            //             itemList[i][j] = Integer.parseInt(data);
            //         }                    
            //     }
            //     long startTime = System.nanoTime();
            //     for (int i = 0; i < problems; i++){
            //         System.out.print("Number of bins required in First Fit Decreasing: " + firstFitDecreasing(itemList[i], 500, 150) + "\n");
            //     }
            //     long endTime = System.nanoTime();
            //     System.out.print("Time taken: " + (endTime - startTime) / 1000000  + "ms\n");
            // }
            File folder = new File("Testing-Data/Complexity Test");
            File[] listOfFiles = folder.listFiles();
            Arrays.sort(listOfFiles);
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
                    long startTime = System.nanoTime();
                    firstFitDecreasing(item, n, cap);
                    long endTime = System.nanoTime();
                    System.out.println("Time taken: " + (endTime-startTime)/1000 + " microseconds.");
                }
            }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
    }
}
