import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.io.FileNotFoundException; // Import this class to handle errors
import java.io.File;

class FFD {
    static int firstFit(Integer item[], int n, int cap) {
        int numberOfBins = 0;
        int[] resCap = new int[n];
        resCap[0] = cap;
        for (int i = 0; i < n; i++) {
            int j;
            for (j = 0; j < numberOfBins; j++) {
                if (resCap[j] >= item[i]) {
                    resCap[j] -= item[i];
                    break;
                }
            }
            if (j == numberOfBins) {
                resCap[numberOfBins] = cap - item[i];
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
            // File binText = new File("Testing-Data/binpack1.txt");
            // try (Scanner textReader = new Scanner(binText)) {
            //     long startTime = System.nanoTime();
            //     int problems = Integer.parseInt(textReader.nextLine());
            //     for (int i = 0; i < problems; i++) {
            //         System.out.print("Problem:" + textReader.nextLine() + "\n");
            //         String data = textReader.nextLine().trim();
            //         int cap = Integer.parseInt(data.substring(0, 3));
            //         int n = Integer.parseInt(data.substring(4, 7));
            //         Integer[] item = new Integer[n];
            //         int lowerBound = 0;
            //         for (int j = 0; j < n; j++) {
            //             data = textReader.nextLine();
            //             item[j] = Integer.parseInt(data);
            //             lowerBound += item[j];
            //         }
            //         System.out.println("Lower Bound of Bins is: " + lowerBound / cap );
            //         System.out.print("Number of bins required in First Fit Decreasing: " + firstFitDecreasing(item, n, cap) + "\n");
            //     }
            File folder = new File("Testing-Data/Waescher");
            File[] listOfFiles = folder.listFiles();
            long startTime = System.nanoTime();
            for (File file : listOfFiles) {
                System.out.println(file.getName());   
                try (Scanner textReader = new Scanner(file)) {
                    int n = Integer.parseInt(textReader.nextLine());
                    int cap = Integer.parseInt(textReader.nextLine());
                    Integer[] item = new Integer[n];
                    int lowerBound = 0;
                    for (int j = 0; j < n; j++) {
                        String data = textReader.nextLine();
                        item[j] = Integer.parseInt(data);
                        lowerBound += item[j];
                    }
                    System.out.println("Lower Bound of Bins is " + lowerBound / cap );
                    System.out.print("Number of bins required om First Fit Decreasing: " + firstFitDecreasing(item, n, cap) + "\n");
                    }
            }
            long endTime = System.nanoTime();
            System.out.println("Time taken: " + (endTime - startTime) / 1000000  + "ms\n");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
    }
}