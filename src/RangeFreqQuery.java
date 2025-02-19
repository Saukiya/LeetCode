import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RangeFreqQuery {

    public static void main(String[] args) {
        int[] ints = new int[]{12, 33, 4, 56, 22, 2, 34, 33, 22, 12, 34, 56};
        TimingsUtil timingsUtil = new TimingsUtil();
        RangeFreqQuery rfq = new RangeFreqQuery(ints);
        System.out.println(rfq.query(1, 2, 4));
        System.out.println(rfq.query(0, 11, 33));
        timingsUtil.dot();
        timingsUtil.print();
    }

    HashMap<Integer, List<Integer>> arrays = new HashMap<>();

    public RangeFreqQuery(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arrays.computeIfAbsent(arr[i], k -> new ArrayList<>()).add(i);
        }
    }

    public int query(int left, int right, int value) {
        List<Integer> array = arrays.get(value);
        // array - 2
        if (array == null || array.get(0) > right || array.get(array.size() - 1) < left) return 0;

        int l = 0, r = array.size() - 1;

        while (l < r) {
            int mid = (l + r) / 2;
            int temp = array.get(mid);
            if (temp >= left) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }

        int v1 = l;
        l = 0;
        r = array.size() - 1;
        while (l < r) {
            int mid = (l + r + 1) / 2;
            int temp = array.get(mid);
            if (temp <= right) {
                l = mid;
            } else {
                r = mid - 1;
            }
        }
        return l - v1 + 1;
    }
}