import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.io.FileNotFoundException; // Import this class to handle errors
import java.io.File;

class MF_k {
    static int mostKFit(Integer item[], int n, int cap) {
        int numberOfBins = 0;
        int[] resCap = new int[n];
        resCap[0] = cap;
        n -= 1;
        for (int i = 0; i < n; i++) {
            int j;
            for (j = 0; j < numberOfBins; j++) {
                int initialBacktrack = 0; 
                if (item[n] == 0){
                    while (item[n - initialBacktrack] == 0) {
                        initialBacktrack++;
                        if (initialBacktrack == n) break;
                    }
                }
                
                if (resCap[j] >= item[n - initialBacktrack]){
                    // this holds: highest feasible value found, k value, l value, number of elements (if single element or pair)
                    int []replacer = {0, 0, 0, 0};
                    for (int k = n - initialBacktrack; k > 0; k--){
                        int pair = item[k]; 
                        if (pair > replacer[0] && resCap[j] >= pair) {
                            replacer[0] = pair;
                            replacer[1] = k;
                            replacer[3] = 1;
                        }
                        for (int l = n - initialBacktrack; l > 0; l--)  {
                            pair = item[k] + item[l];
                            if (pair > replacer[0] && resCap[j] >= pair && (item[k] != 0 && item[l] != 0) && k != l){
                                replacer[0] = pair;
                                replacer[1] = k;
                                replacer[2] = l;
                                replacer[3] = 2;
                            }
                        }
                    }
                    if (replacer[0] != 0){
                        if (replacer[3] == 1){
                            resCap[j] -= replacer[0];
                            item[replacer[1]] = 0;
                        }
                        else{
                            resCap[j] -= replacer[0];
                            item[replacer[1]] = 0;
                            item[replacer[2]] = 0;
                        }
                    }
                }
            }
            if (j == numberOfBins && item[i] != 0) {
                resCap[numberOfBins] = cap - item[i];
                item[i] = 0;
                numberOfBins++;

            }
        }
        return numberOfBins;
    }

    static int mostKFitDecreasing(Integer item[], int n, int cap) {
        Arrays.sort(item, Collections.reverseOrder());
        return mostKFit(item, n, cap);
    }

    public static void main(String[] args) {
        try {
            File binText = new File("Testing-Data/binpack4.txt");
            try (Scanner textReader = new Scanner(binText)) {
                int problems = Integer.parseInt(textReader.nextLine());
                for (int i = 0; i < problems; i++) {
                    System.out.print("Problem:" + textReader.nextLine() + "\n");
                    String data = textReader.nextLine().trim();
                    int cap = Integer.parseInt(data.substring(0, 3));
                    int n = Integer.parseInt(data.substring(4, 8));
                    Integer[] item = new Integer[n];
                    for (int j = 0; j < n; j++) {
                        data = textReader.nextLine();
                        item[j] = Integer.parseInt(data);
                    }
                    System.out.print("Number of bins required in Most K Fit Decreasing: " + mostKFitDecreasing(item, n, cap) + "\n");
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