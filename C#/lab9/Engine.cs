using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml.Serialization;

namespace lab9
{
    public class Engine
    {
        private double displacement;
        private double horsePower;
        private string model;

        public Engine()
        {
            this.model = "";
        }
        public Engine(double displacement, double horsePower, string model)
        {
            this.displacement = displacement;
            this.horsePower = horsePower;
            this.model = model;
        }

        public double getDisplacement() { return displacement; }
        public double getHorsePower() { return horsePower; }
        public string getModel() { return model; }

        public override string ToString()
        {
            return $"Model: {model}, Horse Power: {horsePower}, Displacement: {displacement}";
        }
    }
}
