using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml.Serialization;

namespace lab9
{
    public class Car
    {
        private string model;
        private int year;

        private Engine motor;

        public Car()
        {
            this.model = "";
            motor = new Engine();
        }

        public Car(string model, Engine motor, int year)
        {
            this.model = model;
            this.motor = motor;
            this.year = year;
        }

        public string getModel() {  return model; }
        public int getYear() { return year; }
        public Engine getMotor() { return motor; }

        public override string ToString()
        {
            return $"Model: {model}, Year: {year}, Engine: {motor}";
        }
    }
}
