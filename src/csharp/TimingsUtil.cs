using System.Diagnostics;

namespace LeetCode.csharp;

public class TimingsUtil
{
    private LinkedList<(string, long)> timings = new LinkedList<(string, long)>();

    private Stopwatch stopwatch = new Stopwatch();

    public TimingsUtil() => stopwatch.Start();

    public void Dot() => Dot(null);

    public void Dot(string desc)
    {
        stopwatch.Stop();
        timings.AddLast((desc, stopwatch.ElapsedMilliseconds));
        stopwatch.Restart();
    }

    public void Print()
    {
        stopwatch.Stop();
        var total = stopwatch.ElapsedMilliseconds;
        foreach (var tuple in timings)
        {
            if (string.IsNullOrEmpty(tuple.Item1)) continue;
            Log.Info("-\t" + tuple.Item1 + ": " + tuple.Item2 + "ms");
            total += tuple.Item2;
        }
        Log.Info("timings: " + total + "ms");
    }
}