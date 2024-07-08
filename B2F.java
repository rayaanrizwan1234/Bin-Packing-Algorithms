import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;

class B2F{
    static int firstFit(Integer item[], int n, int cap) {
        int numberOfBins = 0;
        int []resCap = new int[n];
        ArrayList<ArrayList<Integer>> bins = new ArrayList<ArrayList<Integer> >();
        resCap[0] = cap;
        for (int i = 0; i < n; i++){
            // new issue is that need to figure out how to skip past element if it has already been swapped in.
            System.out.print(item[i] + "\n");
            int j;
            for (j = 0; j < numberOfBins; j++){
                if (resCap[j] >= item[i]){
                    bins.get(j).add(item[i]);
                    resCap[j] -= item[i];
                    break;
                }
            }
            if (j == numberOfBins){
                // this holds : bin where swap occured, smallest item in that bin, k value, l value, largest feasible pair found, the size of that bin
                int []replacer = { 0, 0, 0, 0, 0, 0 };
                for (j = 0; j < numberOfBins; j++){
                    int size = bins.get(j).size();
                    int smallestItem = (bins.get(j).get(size - 1));
                    for (int k = i; k < n; k++){
                        for (int l = i; l < n; l++ ){
                            int pair = item[k] + item[l];
                            if (resCap[j] + smallestItem - pair >= 0 && pair > smallestItem){
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
                    }
                }
                if (replacer[4] != 0){
                    bins.get(replacer[0]).set(replacer[5] - 1, item[replacer[2]]);
                    bins.get(replacer[0]).add(item[replacer[3]]);
                    item[i+1] = replacer[1];
                }
                else{
                    bins.add(new ArrayList<Integer>());
                    resCap[numberOfBins] = cap - item[i];
                    bins.get(numberOfBins).add(item[i]);
                    numberOfBins++;
                }
            }
        }
        System.out.print(bins.get(1).get(0));
        return numberOfBins;
    }
    
    static int firstFitDecreasing(Integer item[], int n, int cap) {
        Arrays.sort(item, Collections.reverseOrder());
        return firstFit(item, n, cap);
    }

    public static void main( String[] args ) {
        // B2F works for these values, haven't tried any others yet
        Integer items[] = {5, 4, 3, 2};
        int cap = 10;
        int n = items.length;
        System.out.print("Number of bins required in First Fit Decreasing: " + firstFitDecreasing(items, n, cap) + "\n");
    }
}