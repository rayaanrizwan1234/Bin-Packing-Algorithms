import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.File;

class B2F{
    static int bestTwoFit(Integer item[], int n, int cap) {
        int numberOfBins = 0;
        int []resCap = new int[n];
        ArrayList<ArrayList<Integer>> bins = new ArrayList<ArrayList<Integer> >();
        resCap[0] = cap;
        for (int i = 0; i < n; i++){
            // System.out.print(item[i] + "\n");
            // checks if item has already been inserted into a bin.
            if (item[i] == 0) continue;
            int j;
            // checks if item fits into any bin normally.
            for (j = 0; j < numberOfBins; j++){
                if (resCap[j] >= item[i]){
                    bins.get(j).add(item[i]);
                    resCap[j] -= item[i];
                    item[i] = 0;
                    break;
                }
            }
            if (j == numberOfBins){
                // this holds : bin where swap occured, smallest item in that bin, k value, l value, largest feasible pair found, the size of that bin
                int []replacer = { 0, 0, 0, 0, 0, 0 };
                // checks whether any feasible swaps are possible with the smallest item in each bin.
                for (j = 0; j < numberOfBins; j++){
                    int size = bins.get(j).size();
                    int smallestItem = (bins.get(j).get(size - 1));
                    for (int k = i; k < n; k++){
                        for (int l = k; l < n; l++ ){
                            // checks the pair to see if it's iterative and stores in replacer if it's best feasible pair found yet.
                            int pair = item[k] + item[l];
                            if (resCap[j] + smallestItem - pair >= 0 && pair > smallestItem && pair > (cap/6) && k != l && size > 1){
                                if (pair > replacer[4]){
                                    replacer[0] = j;
                                    replacer[1] = smallestItem;
                                    replacer[2] = k;
                                    replacer[3] = l; 
                                    replacer[4] = pair;
                                    replacer[5] = size;
                                }
                            }
                        }
                        // checks and breaks the iterative loop if feasible solution found is optimal (0 residual capacity)
                        if (resCap[j] - replacer[4] == 0) break;
                    }
                }
                // updates item and bins with largest feasible pair found if there has been one found
                if (replacer[4] != 0){
                    // System.out.print("Replacing " + replacer[1] + " in " + replacer[0] + " with " + item[replacer[2]] + " and " + item[replacer[3]] + "\n");
                    resCap[replacer[0]] = resCap[replacer[0]] + replacer[1] - replacer[4];
                    bins.get(replacer[0]).set(replacer[5] - 1, item[replacer[2]]);
                    bins.get(replacer[0]).add(item[replacer[3]]);
                    item[replacer[2]] = replacer[1];
                    item[replacer[3]] = item[i];
                }
                else{
                    bins.add(new ArrayList<Integer>());
                    resCap[numberOfBins] = cap - item[i];
                    bins.get(numberOfBins).add(item[i]);
                    numberOfBins++;
                }
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
            File binText = new File("Testing-Data/binpack3.txt");
            Scanner textReader = new Scanner(binText);
            long startTime = System.nanoTime();
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
                System.out.print("Number of bins required in Best Two Fit: " + bestTwoFitDecreasing(item, n, cap) + "\n");
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