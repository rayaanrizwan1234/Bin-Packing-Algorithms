import java.util.Arrays;
import java.util.Collections;

public class WFD {
    static int worstFitDecreasing(Double[] items, double binCapacity) {
        Arrays.sort(items, Collections.reverseOrder());
        int n = items.length;
        double[] resCap = new double[n];
        int numOfBins = 0;
        for (int i = 0; i < n; i++) {
            int j;
            // set default values, neither would checnge if no bin is found
            double max = -1;
            int maxBin = -1;
            for (j = 0; j < numOfBins; j++) {
                if ((Double.compare(resCap[j], items[i]) >= 0) && (resCap[j] - items[i] > max)) {
                    maxBin = j;
                    max = resCap[j] - items[i];
                }
            }
            // No bin could accomodate it
            if (max == -1) {
                resCap[numOfBins] = binCapacity - items[i];
                numOfBins++;
            } else {
                resCap[maxBin] -= items[i];
            }
        }

        return numOfBins;
    }

    public static void main(String[] args) {
        Double[] items = {0.4, 0.4, 0.2};
        double binCapacity = 1.0;
        System.out.println(worstFitDecreasing(items, binCapacity));
    }
}

