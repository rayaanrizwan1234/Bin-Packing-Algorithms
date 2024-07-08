import java.util.Arrays;
import java.util.Collections;

public class WFD {
    static int worstFitDecreasing(Integer[] items, int binCapacity) {
        Arrays.sort(items, Collections.reverseOrder());
        int n = items.length;
        int[] resCap = new int[n];
        int numOfBins = 0;
        for (int i = 0; i < n; i++) {
            int j;
            // set default values, neither would change if no bin is found
            int max = -1;
            int maxBin = -1;
            for (j = 0; j < numOfBins; j++) {
                if (resCap[j] >= items[i] && (resCap[j] - items[i] > max)) {
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
        Integer[] items = {4, 4, 2};
        int binCapacity = 10;
        System.out.println(worstFitDecreasing(items, binCapacity));
    }
}

