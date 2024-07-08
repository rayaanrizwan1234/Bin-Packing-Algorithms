import java.util.Arrays;
import java.util.Collections;
class BFD {
    static int bestFit(Integer item[], int n, int cap) {
        int numberOfBins = 0;
        int []resCap = new int[n];
        for (int i = 0; i < n; i++) {
            int j;
            int min = cap + 1;
            int bi = 0;
            for (j = 0; j < numberOfBins; j++) {
                if ((resCap[j] >= item[i]) && resCap[j] - item[i] < min){
                    bi = j;
                    min = resCap[j] - item[i];
                    System.out.print("Putting " + item[i] + " in bin " + (j+1)+ "\n");
                }
            }
            if (min == cap + 1) {
                resCap[numberOfBins] = cap - item[i];
                System.out.print("Putting " + item[i] + " in bin " + (numberOfBins+1)+ "\n");
                numberOfBins++;
            }
            else
                resCap[bi] -= item[i];
        }
        return numberOfBins;
    }

    static int bestFitDecreasing(Integer item[], int n, int cap) {
        Arrays.sort(item, Collections.reverseOrder());
        return bestFit(item, n, cap);
    }

    public static void main( String[] args ) {
        Integer items[] = {7, 5, 4, 1, 3};
        int cap = 10;
        int n = items.length;
        System.out.print("Number of bins required in Best Fit Decreasing: " + bestFitDecreasing(items, n, cap) + "\n");
    }
}