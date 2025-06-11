using LeetCode.csharp;

public class Solution {

    public static void Main(string[] args)
    {
        var timings = new TimingsUtil();
        var solution = new Solution();
        Log.Info("2333");
        solution.Start();
        timings.Print();
    }

    public void Start()
    {
        SearchMatrix(new int[][] {
            [1,3,5,7],
            [10,11,16,20],
            [23,30,34,60],
        }, 3);
    }
    
    public bool SearchMatrix(int[][] matrix, int target)
    {
        var m = matrix.Length;
        var n = matrix[0].Length;
        // i = [0] - [m * n - 1]
        // matrix[i / n][i % n]
        if (m == 1 && n == 1) return matrix[0][0] == target;
        
        return false;
    }
}