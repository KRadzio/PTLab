// See https://aka.ms/new-console-template for more information
//Console.WriteLine("Hello, World!");

using System.Collections;
using System.IO;
using System.Runtime.CompilerServices;
using System.Xml.Linq;
using static System.Net.WebRequestMethods;

public static class ExtensionMethods
{
    static public DateTime GetOldestElementDate(this DirectoryInfo di)
    {
        DateTime oldestDate = DateTime.Now;

        foreach(var file in di.GetFiles())
        {
            if(file.CreationTime < oldestDate) 
                oldestDate = file.CreationTime;
        }
        foreach(var dir in di.GetDirectories())
        {
            DateTime subDirectoryOldestDate = GetOldestElementDate(dir);
            if (subDirectoryOldestDate < oldestDate)
                oldestDate = subDirectoryOldestDate;
        }
        return oldestDate;
    }

    public static string GetDOSAttributes(this FileSystemInfo fileSystemInfo)
    {
        string attributes = "";
        attributes += fileSystemInfo.Attributes.HasFlag(FileAttributes.ReadOnly) ? "r" : "-";
        attributes += fileSystemInfo.Attributes.HasFlag(FileAttributes.Archive) ? "a" : "-";
        attributes += fileSystemInfo.Attributes.HasFlag(FileAttributes.Hidden) ? "h" : "-";
        attributes += fileSystemInfo.Attributes.HasFlag(FileAttributes.System) ? "s" : "-";
        return attributes;
    }
}

public static class app
{
    static private void printRecursive(string dirPath,int tabAmount)
    {
        String[] files = System.IO.Directory.GetFiles(dirPath);
        String[] dirs = System.IO.Directory.GetDirectories(dirPath);
        int totalElements = files.Length + dirs.Length;
        String currDir = "";
        for (int i = 0; i < tabAmount; i++)
            currDir += "\t";
        currDir += dirPath;
        currDir += " (";
        currDir += totalElements;
        currDir += ") ";
        FileInfo dirInfo = new FileInfo(dirPath);
        string attributes = dirInfo.GetDOSAttributes();
        currDir += attributes;
        Console.WriteLine(currDir);
        foreach (var file in files)
        {
            String name = "";
            for (int i = 0; i < tabAmount+1; i++)
                name += "\t";
            name += file;
            name += " ";
            FileInfo fileInfo = new FileInfo(file);
            name += fileInfo.Length;
            name += " bajtow ";
            string fileAttributes = fileInfo.GetDOSAttributes();
            name += fileAttributes;
            Console.WriteLine(name);
        }
        foreach (var dir in dirs)
        {
            printRecursive(dir, tabAmount + 1);
        }

    }
    public class ComparerClass : IComparer<string>
    {
        public int Compare(string x, string y)
        { 
            if(x.Length != y.Length)
                return x.Length > y.Length ? 1 : -1;
            else
                return String.Compare(x, y);
        }
    }

    static void Main(string[] args)
    {
        printRecursive(args[0], 0);
        System.IO.DirectoryInfo di = new DirectoryInfo(args[0]);
        DateTime oldestDate = di.GetOldestElementDate();
        Console.WriteLine("Najstarszy Plik: {0}", oldestDate);
        SortedDictionary<string, long> elements = new SortedDictionary<string, long>(new ComparerClass());
        String[] files = System.IO.Directory.GetFiles(args[0]);
        String[] dirs = System.IO.Directory.GetDirectories(args[0]);

        foreach (var file in files)
        {
            FileInfo fileInfo = new FileInfo(file);
            elements.Add(file, fileInfo.Length);
        }
        foreach (var dir in dirs)
        {
            String[] currfiles = System.IO.Directory.GetFiles(dir);
            String[] currdirs = System.IO.Directory.GetDirectories(dir);
            int totalElements = currfiles.Length + currdirs.Length;
            elements.Add(dir, totalElements);
        }
        foreach (KeyValuePair<string, long> e in elements)
        {
            Console.WriteLine("{0} -> {1}",
                e.Key, e.Value);
        }
    }
}