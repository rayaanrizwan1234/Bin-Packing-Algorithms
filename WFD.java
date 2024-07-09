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
            File binText = new File("binpack1.txt");
            Scanner textReader = new Scanner(binText);
            int problems = Integer.parseInt(textReader.nextLine());
            for (int i = 0; i < problems; i++){
                System.out.print("Problem:" + textReader.nextLine() + "\n");
                String data = textReader.nextLine().trim();
                int cap = Integer.parseInt(data.substring(0, 3));
                int n = Integer.parseInt(data.substring(4,7));
                Integer []item = new Integer[n];
                for (int j = 0; j < n; j++){
                    data = textReader.nextLine();
                    item[j] = Integer.parseInt(data);
                }
                System.out.print("Number of bins required in Best Fit Decreasing: " + worstFitDecreasing(item, n, cap) + "\n");
            }
        }
        catch (FileNotFoundException e) {
            System.out.print("An error occured.\n");
            e.printStackTrace();
        }
    }
}

