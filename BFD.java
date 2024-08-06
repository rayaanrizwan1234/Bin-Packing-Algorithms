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
            // File binText = new File("Testing-Data/binpack4.txt");
            // Scanner textReader = new Scanner(binText);
            // int problems = Integer.parseInt(textReader.nextLine());
            // Integer[][] itemList = new Integer[problems][1000];
            // for (int i = 0; i < problems; i++){
            //     System.out.print("Problem:" + textReader.nextLine() + "\n");
            //     String data = textReader.nextLine().trim();
            //     int cap = Integer.parseInt(data.substring(0, 3));
            //     int n = Integer.parseInt(data.substring(4,8));
            //     Integer []item = new Integer[n];
            //     for (int j = 0; j < n; j++){
            //         data = textReader.nextLine();
            //         itemList[i][j] = Integer.parseInt(data);
            //     }
                
            // }
            // long startTime = System.nanoTime();
            // for (int i = 0; i < problems; i++){
            //     System.out.print("Number of bins required in Best Fit Decreasing: " + bestFitDecreasing(itemList[i], 1000, 150) + "\n");
            // }
            // long endTime = System.nanoTime();
            // System.out.print("Time taken: " + (endTime - startTime) / 1000000  + "ms\n");
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
                    bestFitDecreasing(item, n, cap);
                    long endTime = System.nanoTime();
                    System.out.println("Time taken: " + (endTime-startTime)/1000 + " microseconds.");
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