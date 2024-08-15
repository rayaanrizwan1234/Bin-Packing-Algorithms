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
            int []replacer = { 0, 0, 0, 0, 0, 0 };
            // checks if item fits into any bin normally.
            for (j = 0; j < numberOfBins; j++){
                if (resCap[j] >= item[i]){
                    bins.get(j).add(item[i]);
                    resCap[j] -= item[i];
                    item[i] = 0;
                    break;
                }
                else {
                    int size = bins.get(j).size();
                    int smallestItem = (bins.get(j).get(size - 1));
                    if (size > 1) {
                        for (int k = i; k < n; k++){
                            for (int l = k; l < n; l++ ){
                                // checks the pair to see if it's iterative and stores in replacer if it's best feasible pair found yet.
                                int pair = item[k] + item[l];
                                if (resCap[j] + smallestItem - pair >= 0 && pair > smallestItem && pair > (cap/6) && k != l){
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
                    
                }
            }
            if (j == numberOfBins){
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
            //     System.out.print("Number of bins required in Best Two Fit: " + bestTwoFitDecreasing(itemList[i], 1000, 150) + "\n");
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
                    bestTwoFitDecreasing(item, n, cap);
                    long endTime = System.nanoTime();
                    System.out.println("Time taken: " + (endTime-startTime)/1000 + " microseconds.");
                }
            }
        }
        catch (FileNotFoundException e) {
            System.out.print("An error occured.\n");
            e.printStackTrace();
        }
    }
}