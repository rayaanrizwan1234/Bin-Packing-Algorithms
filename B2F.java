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
            int j;
            for (j = 0; j < numberOfBins; j++){
                if (resCap[j] >= item[i]){
                    bins.get(j).add(item[i]);
                    resCap[j] -= item[i];
                    break;
                }
            }
            if (j == numberOfBins){
                bins.add(new ArrayList<Integer>());
                resCap[numberOfBins] = cap - item[i];
                bins.get(numberOfBins).add(item[i]);
                numberOfBins++;
            }
        }
        return numberOfBins;
    }
    
    static int firstFitDecreasing(Integer item[], int n, int cap) {
        Arrays.sort(item, Collections.reverseOrder());
        return firstFit(item, n, cap);
    }

    public static void main( String[] args ) {
        Integer items[] = {4, 4, 4, 2};
        int cap = 10;
        int n = items.length;
        System.out.print("Number of bins required in First Fit Decreasing: " + firstFitDecreasing(items, n, cap) + "\n");
    }
}