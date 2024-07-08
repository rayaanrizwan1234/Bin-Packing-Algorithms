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
                // have concerns about this iteration swapping elements in multiple different bins but don't want to use a break as it needs to check for the largest combined size
                for (j = 0; j < numberOfBins; j++){
                    int size = bins.get(j).size();
                    int smallestItem = bins.get(j).get(size - 1);
                    for (int k = 0; k < n - i ; k++){
                        for (int l = 0; l < n - i ; l++ ){
                            int pair = item[i+k] + item[i+l];
                            if (resCap[j] + smallestItem - pair >= 0 && pair > smallestItem){
                                bins.get(j).set(size - 1, item[i+k]);
                                bins.get(j).add(item[i+l]);
                                n -= 1;
                                item[i+k] = smallestItem;
                                break;
                            }
                        }
                    }
                }
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
        // B2F works for these values, haven't tried any others yet
        Integer items[] = {6, 3, 2, 2};
        int cap = 10;
        int n = items.length;
        System.out.print("Number of bins required in First Fit Decreasing: " + firstFitDecreasing(items, n, cap) + "\n");
    }
}