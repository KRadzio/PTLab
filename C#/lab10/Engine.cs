using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml.Serialization;

namespace lab10
{
    public class Engine : IComparable<Engine>
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

        public int CompareTo(Engine other)
        {
            return this.horsePower.CompareTo(other.horsePower);
        }

        public override string ToString()
        {
            return $"Model: {model}, Horse Power: {horsePower}, Displacement: {displacement}";
        }
    }
}
