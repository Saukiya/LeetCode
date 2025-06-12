using System.Runtime.CompilerServices;

namespace LeetCode.csharp;

public class Log
{
    public static void Info(object message, [CallerFilePath] string path = null, [CallerLineNumber] int line = default)
    {
        Console.WriteLine("[" + path.Split('/','\\','.')[^2] + ":" + line + "] " + message);
    }
}