using LeetCode.csharp;
using static LeetCode.csharp.Log;

public class Solution {
    public Solution()
    {
    }

    public static void Main(string[] args)
    {
        var solution = new Solution();
        var timings = new TimingsUtil();
        solution.Start();
        timings.Print();
    }

    public void Start()
    {
        Info(SearchMatrix(new int[][] {
            [1,3,5,7],
            [10,11,16,20],
            [23,30,34,60],
        }, 3));
        Info(SearchMatrix(new int[][] {
            [1,3,5,7],
            [10,11,16,20],
            [23,30,34,60],
        }, 13));
        Info(SearchMatrix(new int[][] {
            [1,3]
        }, 3));
        Info(SearchMatrix(new int[][] {
            [1,3]
        }, 6));
    }

    public bool SearchMatrix(int[][] matrix, int target) {
        var m = matrix.Length;
        var n = matrix[0].Length;
        if (m == 1 && n == 1) return matrix[0][0] == target;
        int l = 0, r = m * n - 1;
        while (l <= r)
        {
            var mid = (l + r) / 2;
            var temp = matrix[mid / n][mid % n];
            if (temp > target)
            {
                r = mid - 1;
            } else if (temp < target)
            {
                l = mid + 1;
            }
            else return true;
        }
        return false;
    }
}