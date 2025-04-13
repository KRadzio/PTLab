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
using System.Text.RegularExpressions;

namespace lab8
{
    /// <summary>
    /// Logika interakcji dla klasy CreateForm.xaml
    /// </summary>
    public partial class CreateForm : Window
    {
        private string newItemPath;
        private bool validForm = false;

        public CreateForm(string dirPath)
        {
            InitializeComponent();
            newItemPath = dirPath;
        }

        public string getPath() { return newItemPath; }

        public bool IsValid() { return validForm; }

        private void confirmClick(object sender, RoutedEventArgs e)
        {
            if ((bool)radioFile.IsChecked && !Regex.IsMatch(textName.Text, "^[a-zA-Z0-9_~-]{1,8}(.txt|.html|.php)$"))
                System.Windows.MessageBox.Show("The name does not meet requirements", "Invalid name", MessageBoxButton.OK, MessageBoxImage.Error);
            else if (string.IsNullOrEmpty(textName.Text))
                System.Windows.MessageBox.Show("The name field is empty", "No name entered", MessageBoxButton.OK, MessageBoxImage.Error);
            else
            {
                string newItemName = textName.Text;
                newItemPath = newItemPath + "\\" + newItemName;
                FileAttributes newItemAttributes = FileAttributes.Normal;
                if ((bool)checkA.IsChecked)
                    newItemAttributes |= FileAttributes.Archive;
                if ((bool)checkR.IsChecked)
                    newItemAttributes |= FileAttributes.ReadOnly;
                if ((bool)checkH.IsChecked)
                    newItemAttributes |= FileAttributes.Hidden;
                if ((bool)checkS.IsChecked)
                    newItemAttributes |= FileAttributes.System;
                if ((bool)radioFile.IsChecked)
                    File.Create(newItemPath);
                else if ((bool)radioDirectory.IsChecked)
                    Directory.CreateDirectory(newItemPath);
                File.SetAttributes(newItemPath, newItemAttributes);
                Close();
                validForm = true;
            }
        }

        private void cancelClick(object sender, RoutedEventArgs e)
        {
            Close();
            System.Windows.MessageBox.Show("The form has been cancelled", "Form Cancelled", MessageBoxButton.OK, MessageBoxImage.Information);        
        }
    }
}
