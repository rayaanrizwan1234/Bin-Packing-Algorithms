import java.util.Arrays;
import java.util.Collections;

// designing this algorithm using the FFD implementation due to similarities.
class B2F{
    // where resCap is mentioned this will be converted into a tree
    static int firstFit(Double item[], int n, double cap) {
        int numberOfBins = 0;
        double []resCap = new double[n];
        resCap[0] = cap;
        for (int i = 0; i < n; i++){
            int j;
            for (j = 0; j < numberOfBins; j++){
                if (resCap[j] >= item[i] || Math.abs(resCap[j] - item[i]) < 0.00000001){
                    resCap[j] -= item[i];
                    break;
                }
            }
            if (j == numberOfBins){
                resCap[numberOfBins] = cap - item[i];
                numberOfBins++;
            }
        }
        return numberOfBins;
    }
    
    static int firstFitDecreasing(Double item[], int n, double cap) {
        Arrays.sort(item, Collections.reverseOrder());
        return firstFit(item, n, cap);
    }

    public static void main( String[] args ) {
        Double items[] = {0.4, 0.4, 0.2};
        double cap = 1.0;
        int n = items.length;
        System.out.print("Number of bins required in First Fit Decreasing: " + firstFitDecreasing(items, n, cap) + "\n");
    }
}