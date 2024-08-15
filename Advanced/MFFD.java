import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.File;

class MFFD{
    static int modifiedFirstFit(Integer item[], int n, int cap) {
        int numberOfBins = 0;
        
        int []resCap = new int[n];
        ArrayList<ArrayList<Integer>> bins = new ArrayList<ArrayList<Integer> >();
        resCap[0] = cap;
        ArrayList<Integer> AItems = new ArrayList<Integer>();
        ArrayList<Integer> BItems = new ArrayList<Integer>();
        ArrayList<Integer> SItems = new ArrayList<Integer>();
        ArrayList<Integer> remains = new ArrayList<Integer>();
        // Seperating the items into their different partitions
        for (int i = 0; i < n; i++){
            if (item[i] > (cap/2))AItems.add(item[i]);
            else if (((cap/3) <= item[i]) && ( item[i] < (cap/2))) BItems.add(item[i]);
            else if (((cap/6) <= item[i]) && ( item[i] < (cap/3))) SItems.add(item[i]);
            else remains.add(item[i]);
        }
        int i;
        // Executing the first and second phase of MFFD, creating A-bins and then depositing largest B-item that fits in each bin.
        for (i = 0; i < AItems.size(); i++){
            bins.add(new ArrayList<Integer>());
            resCap[numberOfBins] = cap - item[i];
            bins.get(numberOfBins).add(item[i]);
            item[i] = 0;
            for (int j = AItems.size(); j < AItems.size() + BItems.size() ; j++){
                if (resCap[numberOfBins] >= item[j] && item[j] != 0){
                    bins.get(numberOfBins).add(item[j]);
                    resCap[numberOfBins] -= item[j];
                    item[j] = 0;
                    break;
                } 
            }
            numberOfBins++;
        }
        // Executing the third phase.
        for (i = AItems.size() - 1; i >= 0; i--){
            if (bins.get(i).size() > 1) continue;
            else{
                //  pair value, j value, k value
                int []replacer = {0, 0, 0};
                int initialStep = AItems.size() + BItems.size();
                for (int j = initialStep; j < initialStep + SItems.size(); j++){
                    for (int k = n - remains.size(); k > j; k--){
                        int pair = item[j] + item[k];
                        if (resCap[i] - pair >= 0 && ((item[j] != 0) && (item[k]) != 0) && j != k){
                            if (pair > replacer[0]){
                                replacer[0] = pair;
                                replacer[1] = j;
                                replacer[2] = k;
                            }
                        }
                    }
                }
                if (replacer[0] != 0){
                    bins.get(i).add(item[replacer[1]]);
                    bins.get(i).add(item[replacer[2]]);
                    resCap[i] -= replacer[0];
                    item[replacer[1]] = 0;
                    item[replacer[2]] = 0;
                }
            }
        }
        // Executing fourth phase: Last pass through considering any item
        for (i = 0; i < AItems.size(); i++){
            for (int j = AItems.size() + BItems.size(); j < n; j++){
                if (resCap[i] >= item[j] && item[j] != 0){
                    resCap[i] -= item[j];
                    bins.get(i).add(item[j]);
                    item[j] = 0;
                }
            }
        }
        // Executing fifth phase: Performing normal FFD with remaining items.
        for (i = 0; i < n; i++){
            int j;
            for (j = 0; j < numberOfBins; j++){
                if (resCap[j] >= item[i] && item[i] != 0){
                    resCap[j] -= item[i];
                    break;
                }
            }
            if (j == numberOfBins && item[i] != 0){
                resCap[numberOfBins] = cap - item[i];
                numberOfBins++;
            }
        }
        return numberOfBins;
    }
    
    static int modifiedFirstFitDecreasing(Integer item[], int n, int cap) {
        Arrays.sort(item, Collections.reverseOrder());
        return modifiedFirstFit(item, n, cap);
    }

    public static void main( String[] args ) {
        try {
            File binText = new File("Testing-Data/binpack2.txt");
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
                System.out.print("Number of bins required in Modified First Fit Decreasing: " + modifiedFirstFitDecreasing(item, n, cap) + "\n");
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