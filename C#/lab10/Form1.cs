using System.ComponentModel;

namespace lab10
{
    public partial class Form1 : Form
    {

        private static readonly List<Car> myCars = new List<Car>
        {
                new Car("E250", new Engine(1.8, 204, "CGI"), 2009),
                new Car("E350", new Engine(3.5, 292, "CGI"), 2009),
                new Car("A6", new Engine(2.5, 187, "FSI"), 2012),
                new Car("A6", new Engine(2.8, 220, "FSI"), 2012),
                new Car("A6", new Engine(3.0, 295, "TFSI"), 2012),
                new Car("A6", new Engine(2.0, 175, "TDI"), 2011),
                new Car("A6", new Engine(3.0, 309, "TDI"), 2011),
                new Car("S6", new Engine(4.0, 414, "TFSI"), 2012),
                new Car("S8", new Engine(4.0, 513, "TFSI"), 2012)
         };
        public Form1()
        {
              InitializeComponent();
              CarBindingList carsBindingList = new CarBindingList(myCars);
              BindingSource carBindingSource = new BindingSource();
              carBindingSource.DataSource = carsBindingList;
              dataGridView1.DataSource = carBindingSource;
              
        }
    }
}