namespace LeetCode.csharp;

public class Void
{
    /// <summary>
    /// 二分查找函数
    /// </summary>
    /// <param name="leftIndex">左指针</param>
    /// <param name="rightIndex">右指针</param>
    /// <param name="getValue">根据指针拿值</param>
    /// <param name="target">目标值</param>
    /// <returns>如果存在则返回指针，不存在则返回null</returns>
    public int? BinarySearch(int leftIndex, int rightIndex, Func<int, int> getValue, int target)
    {
        while (leftIndex <= rightIndex)
        {
            var mid = (leftIndex + rightIndex) / 2;
            var temp = getValue(mid);
            if (temp > target)
            {
                rightIndex = mid - 1;
            } else if (temp < target)
            {
                leftIndex = mid + 1;
            }
            else return mid;
        }
        return null;
    }
}