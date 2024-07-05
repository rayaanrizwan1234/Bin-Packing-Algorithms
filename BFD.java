import java.util.Arrays;
import java.util.Collections;
class BFD {
    static int bestFit(Double item[], int n, double cap) {
        int numberOfBins = 0;
        double []resCap = new double[n];
        for (int i = 0; i < n; i++) {
            int j;
            double min = cap + 1.0;
            int bi = 0;
            // with a tree you just get the in order traversal and select the smallest element in it.
            for (j = 0; j < numberOfBins; j++) {
                if ((resCap[j] >= item[i] || Math.abs(resCap[j] - item[i]) < 0.00000001) && resCap[j] - item[i] < min){
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

    static int bestFitDecreasing(Double item[], int n, double cap) {
        Arrays.sort(item, Collections.reverseOrder());
        return bestFit(item, n, cap);
    }

    public static void main( String[] args ) {
        Double items[] = {0.7, 0.5, 0.4, 0.1, 0.3};
        double cap = 1.0;
        int n = items.length;
        System.out.print("Number of bins required in Best Fit Decreasing: " + bestFitDecreasing(items, n, cap) + "\n");
    }
}