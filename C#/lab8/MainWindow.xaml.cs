using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.Windows.Forms;
using System.IO;
using System.Xml.Linq;

namespace lab8
{
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();
        }

        // tworzenie drzewa
        private TreeViewItem createNewTreeItem(string path)
        {
            FileInfo file = new FileInfo(path);
            var newItem = new TreeViewItem
            {
                Header = file.Name,
                Tag = path
            };
            string uniqueContextMenuOption;
            newItem.ContextMenu = new System.Windows.Controls.ContextMenu();
            if (Directory.Exists(path)) // pliki i katalogi mają mieć różne opcje
                uniqueContextMenuOption = "Create";
            else
                uniqueContextMenuOption = "Open";
            var option1 = new System.Windows.Controls.MenuItem { Header = uniqueContextMenuOption };
            var option2 = new System.Windows.Controls.MenuItem { Header = "Delete" };
            newItem.ContextMenu.Items.Add(option1);
            newItem.ContextMenu.Items.Add(option2);
            option2.Click += new RoutedEventHandler(deleteOptionClick);
            if (Directory.Exists(path))
                option1.Click += new RoutedEventHandler(createOptionClick);
            else
                option1.Click += new RoutedEventHandler(openOptionClick);
            newItem.Selected += new RoutedEventHandler(setItemAttributes);
            return newItem;
        }

        private void buildViewTreeRec(string currPath, TreeViewItem node)
        {
            foreach(var dir in Directory.GetDirectories(currPath))
            {
                var newDir = createNewTreeItem(dir);
                node.Items.Add(newDir);
                buildViewTreeRec(System.IO.Path.GetFullPath(dir), newDir);
            }
            foreach(var file in Directory.GetFiles(currPath))
            {
                var newFile = createNewTreeItem(file);
                node.Items.Add(newFile);
            }
        }

        // obsługa kontrolek
        private void exitClick(object sender, RoutedEventArgs e)
        {
            System.Windows.Application.Current.Shutdown();
        }

        private void openClick(object sender, RoutedEventArgs e)
        {
            var dlg = new FolderBrowserDialog() { Description = "Select directory to open" };
            dlg.ShowDialog();
            string rootPath = dlg.SelectedPath;
            var root = createNewTreeItem(rootPath);
            treeView.Items.Add(root);
            buildViewTreeRec(rootPath, root);
        }

        private void deleteDirectoryRec(string currPath)
        {
            foreach (var file in Directory.GetFiles(currPath))
                File.Delete(file);
            foreach (var dir in Directory.GetDirectories(currPath))
                deleteDirectoryRec(dir);
            Directory.Delete(currPath);
        }

        private void deleteOptionClick(object sender, System.EventArgs e)
        {
            TreeViewItem selectedItem = (TreeViewItem)treeView.SelectedItem;
            if(selectedItem != null)
            {
                string selectedItemPath = selectedItem.Tag.ToString();
                TreeViewItem parentItem = (TreeViewItem)selectedItem.Parent;

                FileAttributes selectedFileAttributes = File.GetAttributes(selectedItemPath);
                if(selectedFileAttributes.HasFlag(FileAttributes.ReadOnly))
                    File.SetAttributes(selectedItemPath, selectedFileAttributes & ~FileAttributes.ReadOnly);
                parentItem.Items.Remove(selectedItem);
                if(Directory.Exists(selectedItemPath))
                    deleteDirectoryRec(selectedItemPath);
                else
                    File.Delete(selectedItemPath);
            }
            
        }

        private void openOptionClick(object sender, System.EventArgs e)
        {
            TreeViewItem selectedItem = (TreeViewItem)treeView.SelectedItem;
            string selectedItemPath = selectedItem.Tag.ToString();
            StreamReader stream = new StreamReader(selectedItemPath);
            scrollViewer.Content = new TextBlock() { Text = stream.ReadToEnd() };
        }

        private void createOptionClick(object sender, System.EventArgs e)
        {
            TreeViewItem selectedDirectory = (TreeViewItem)treeView.SelectedItem;
            string selectedDirPath = selectedDirectory.Tag.ToString();
            CreateForm form = new CreateForm(selectedDirPath);
            form.ShowDialog();
            TreeViewItem newItem;
            if (form.IsValid())
            {
                newItem = createNewTreeItem(form.getPath());
                selectedDirectory.Items.Add(newItem);
            }
        }

        // atrybuty

        private void setItemAttributes(object sender, System.EventArgs e)
        {
            TreeViewItem selectedItem = (TreeViewItem)treeView.SelectedItem;
            string selectedItemPath = selectedItem.Tag.ToString();
            FileSystemInfo fileSystemInfo = new FileInfo(selectedItemPath);
            string selectedItemAttributes = "";
            selectedItemAttributes += fileSystemInfo.Attributes.HasFlag(FileAttributes.ReadOnly) ? "r" : "-";
            selectedItemAttributes += fileSystemInfo.Attributes.HasFlag(FileAttributes.Archive) ? "a" : "-";
            selectedItemAttributes += fileSystemInfo.Attributes.HasFlag(FileAttributes.Hidden) ? "h" : "-";
            selectedItemAttributes += fileSystemInfo.Attributes.HasFlag(FileAttributes.System) ? "s" : "-";
            status.Text = selectedItemAttributes;
        }

    }
}
