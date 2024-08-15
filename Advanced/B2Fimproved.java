import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.File;

class B2Fimproved{
    static int bestTwoFit(Integer item[], int n, int cap) {
        int numberOfBins = 0;
        int []resCap = new int[n];
        ArrayList<ArrayList<Integer>> bins = new ArrayList<ArrayList<Integer> >();
        resCap[0] = cap;
        int largestItem = item[0];
        item[0] = 0;
        while ((largestItem > cap/6) && numberOfBins < n){
            bins.add(new ArrayList<Integer>());
            // finds the largest item used and sets it to 0.
            int i;
            resCap[numberOfBins] = cap - largestItem;
            bins.get(numberOfBins).add(largestItem);       
            for (i = 0; i < n; i++) {
                if (item[i] == 0) continue;
                if (resCap[numberOfBins] >= item[i]) {
                    bins.get(numberOfBins).add(item[i]);
                    resCap[numberOfBins] -= item[i];
                    item[i] = 0;
                }
            }
            int size = bins.get(numberOfBins).size();
            int smallestItem = (bins.get(numberOfBins).get(size - 1));
            int []replacer = { 0, 0, 0, 0, 0, 0 };
            if (size > 1) {
                for (int k = 0; k < n; k++){
                    if (item[k] < cap/6) continue;
                    for (int l = k; l < n; l++ ){
                    // checks the pair to see if it's iterative and stores in replacer if it's best feasible pair found yet.
                        if (item[l] < cap/6) continue;
                        int pair = item[k] + item[l];
                        if (resCap[numberOfBins] + smallestItem - pair >= 0 && pair > smallestItem && k != l){
                            if (pair > replacer[4]){
                                replacer[0] = numberOfBins;
                                replacer[1] = smallestItem;
                                replacer[2] = k;
                                replacer[3] = l; 
                                replacer[4] = pair;
                                replacer[5] = size;
                            }
                        }
                    }
                }
            }
            if (replacer[4] != 0){
                // System.out.print("Replacing " + replacer[1] + " in " + replacer[0] + " with " + item[replacer[2]] + " and " + item[replacer[3]] + "\n");
                resCap[replacer[0]] = resCap[replacer[0]] + replacer[1] - replacer[4];
                bins.get(replacer[0]).set(replacer[5] - 1, item[replacer[2]]);
                bins.get(replacer[0]).add(item[replacer[3]]);
                item[replacer[2]] = replacer[1];
                item[replacer[3]] = 0;
            }
            numberOfBins++;   
            for ( i = 0; i < n; i++){
                if (item[i] != 0){
                    largestItem = item[i];
                    item[i] = 0;
                    break;
                }
            }
            if (i == n) break;
                   
        }
        for (int unit : item) {
            if (unit == 0) continue;
            int j;
            for (j = 0; j < numberOfBins; j++) {
                if (resCap[j] >= unit) {
                    resCap[j] -= unit;
                    break;
                }
            }
            if (j == numberOfBins) {
                bins.add(new ArrayList<Integer>());
                resCap[numberOfBins] = cap - unit;
                bins.get(numberOfBins).add(unit);
                numberOfBins++;
            }
        }
        

        return numberOfBins;
    }
    
    static int bestTwoFitDecreasing(Integer item[], int n, int cap) {
        Arrays.sort(item, Collections.reverseOrder());
        return bestTwoFit(item, n, cap);
    }

    public static void main( String[] args ) {
        try {
            File binText = new File("Testing-Data/binpack2.txt");
            Scanner textReader = new Scanner(binText);
            int problems = Integer.parseInt(textReader.nextLine());
            Integer[][] itemList = new Integer[problems][250];
            for (int i = 0; i < problems; i++){
                System.out.print("Problem:" + textReader.nextLine() + "\n");
                String data = textReader.nextLine().trim();
                int cap = Integer.parseInt(data.substring(0, 3));
                int n = Integer.parseInt(data.substring(4,7));
                Integer []item = new Integer[n];
                for (int j = 0; j < n; j++){
                    data = textReader.nextLine();
                    itemList[i][j] = Integer.parseInt(data);
                }
                
            }
            long startTime = System.nanoTime();
            for (int i = 0; i < problems; i++){
                System.out.print("Number of bins required in Best Two Fit: " + bestTwoFitDecreasing(itemList[i], 250, 150) + "\n");
            }
            long endTime = System.nanoTime();
            System.out.print("Time taken: " + (endTime - startTime) / 1000000  + "ms\n");
        }
        catch (FileNotFoundException e) {
            System.out.print("An error occured.\n");
            e.printStackTrace();
        }
    }
}

    // File folder = new File("Testing-Data/Triplets/120");
    // File[] listOfFiles = folder.listFiles();
    // Arrays.sort(listOfFiles);
    // int problems = 20;
    // Integer[][] itemList = new Integer[problems][120];
    // int a  = 0;
    // for (File file : listOfFiles) {
    //     System.out.println(file.getName());   
    //     try (Scanner textReader = new Scanner(file)) {
    //         int n = Integer.parseInt(textReader.nextLine());
    //         int cap = Integer.parseInt(textReader.nextLine());
    //         for (int j = 0; j < n; j++) {
    //             String data = textReader.nextLine();
    //             itemList[a][j] = Integer.parseInt(data);
    //         }
    //         a++;
    //     }
    // }
    // long startTime = System.nanoTime();
    // for (int i = 0; i < problems; i++){
    //     System.out.print("Number of bins required in Best Two Fit: " + bestTwoFitDecreasing(itemList[i], 120, 1000) + "\n");
    // }
    // long endTime = System.nanoTime();
    // System.out.print("Time taken: " + (endTime - startTime) / 1000000  + "ms\n");
//     } catch (FileNotFoundException e) {
//         e.printStackTrace();
//     }
// }
// }