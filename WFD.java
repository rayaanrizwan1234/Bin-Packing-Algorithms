import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.File;

public class WFD {
    static int worstFitDecreasing(Integer[] items, int n, int cap) {
        Arrays.sort(items, Collections.reverseOrder());
        int[] resCap = new int[n];
        int numOfBins = 0;
        for (int i = 0; i < n; i++) {
            int j;
            // set default values, neither would change if no bin is found
            int max = -1;
            int maxBin = -1;
            for (j = 0; j < numOfBins; j++) {
                if (resCap[j] >= items[i] && (resCap[j] - items[i] > max)) {
                    maxBin = j;
                    max = resCap[j] - items[i];
                }
            }
            // No bin could accomodate it
            if (max == -1) {
                resCap[numOfBins] = cap - items[i];
                numOfBins++;
            } else {
                resCap[maxBin] -= items[i];
            }
        }

        return numOfBins;
    }

    public static void main(String[] args) {
        try {
            File binText = new File("Testing-Data/binpack4.txt");
            Scanner textReader = new Scanner(binText);
            int problems = Integer.parseInt(textReader.nextLine());
            Integer[][] itemList = new Integer[problems][1000];
            for (int i = 0; i < problems; i++){
                System.out.print("Problem:" + textReader.nextLine() + "\n");
                String data = textReader.nextLine().trim();
                int cap = Integer.parseInt(data.substring(0, 3));
                int n = Integer.parseInt(data.substring(4,8));
                Integer []item = new Integer[n];
                for (int j = 0; j < n; j++){
                    data = textReader.nextLine();
                    itemList[i][j] = Integer.parseInt(data);
                }
            }
        
        long startTime = System.nanoTime();
        for (int i = 0; i < problems; i++) { 
            System.out.print("Number of bins required in First Fit Decreasing: " + worstFitDecreasing(itemList[i], 1000, 150) + "\n");
        }
        long endTime = System.nanoTime();
        System.out.print("Time taken: " + (endTime - startTime) / 1000000  + "ms\n");
        // File folder = new File("Testing-Data/Complexity Test");
        //     File[] listOfFiles = folder.listFiles();
        //     Arrays.sort(listOfFiles);
        //     for (File file : listOfFiles) {
        //         System.out.println(file.getName());   
        //         try (Scanner textReader = new Scanner(file)) {
        //             int n = Integer.parseInt(textReader.nextLine());
        //             int cap = Integer.parseInt(textReader.nextLine());
        //             Integer[] item = new Integer[n];
        //             for (int j = 0; j < n; j++) {
        //                 String data = textReader.nextLine();
        //                 item[j] = Integer.parseInt(data);
        //             }
        //             long startTime = System.nanoTime();
        //             worstFitDecreasing(item, n, cap);
        //             long endTime = System.nanoTime();
        //             System.out.println("Time taken: " + (endTime-startTime)/1000 + " microseconds.");
        //         }
        //     }
    }
        catch (FileNotFoundException e) {
            System.out.print("An error occured.\n");
            e.printStackTrace();
        }
    }
}

