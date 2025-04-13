using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.ConstrainedExecution;
using System.Xml.Linq;
using System.Xml.XPath;

namespace lab9
{
    class Lab9
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

        static void Main()
        {
            FindElementsAndAverage();
            SerializeAndDeserialize();
            CalculateAverageAndPickWithNoRepeats();
            Serialize();
            CreateXHTML();
            ModifyColection();
        }

        private static void FindElementsAndAverage() // zadanie 1
        {
            Console.WriteLine("Zadnie 1");
            var query = myCars.Where(s => s.getModel().Equals("A6")) // modela A6
                 .Select(car => new
                       {
                         engineType = String.Compare(car.getMotor().getModel(), "TDI") == 0 // TDI
                                        ? "diesel"
                                        : "petrol",
                         hppl = car.getMotor().getHorsePower() / car.getMotor().getDisplacement(),
                       });
            foreach (var elem in query)
                Console.WriteLine(elem.ToString());
            Console.WriteLine();
            var groupedQuery = query
                .GroupBy(elem => elem.engineType)
                .Select(elem => $"{elem.First().engineType}: {elem.Average(s => s.hppl).ToString()}");
            foreach (var elem in groupedQuery)
                Console.WriteLine(elem);
            Console.WriteLine();
        }

        private static void Serialize() // zadanie 4 + używane w 2 
        {
            IEnumerable<XElement> cars = myCars
                .Select(c =>
                new XElement("car",
                    new XElement("model", c.getModel() ),
                    new XElement("engine",
                        new XAttribute("model", c.getMotor().getModel() ),
                        new XElement("displacement", c.getMotor().getDisplacement() ),
                        new XElement("horsePower", c.getMotor().getHorsePower() )),
                    new XElement("year", c.getYear() )));
            XElement rootNode = new XElement("cars", cars);
            rootNode.Save("CarsCollection.xml");
            rootNode.Save("LinqCarsCollection.xml");
        }

        private static void SerializeAndDeserialize() // zadanie 2
        {
            Serialize(); // serializacja

            // deserializacja
            var fileName = "CarsCollection.xml";
            var currentDirectory = Directory.GetCurrentDirectory();
            Console.WriteLine("Zadanie2");
            List<Car> deserializedList = new List<Car>();
            Console.WriteLine("Models of cars from deserialized list: ");
            foreach (var elem in deserializedList)
                Console.Write($"{elem.getModel()} ");            
            Console.WriteLine();
        }
        private static void CalculateAverageAndPickWithNoRepeats() // zadanie 3
        {
            Console.WriteLine("Zadanie 3");
            XElement rootNode = XElement.Load("CarsCollection.xml");
            var countAvarageXPath = "sum(//car/engine[@model!=\"TDI\"]/horsePower) div count(//car/engine[@model!=\"TDI\"]/horsePower)";
            Console.WriteLine($"Average Power: {(double)rootNode.XPathEvaluate(countAvarageXPath)}");

            var removeDuplicatesXPath = "//car[following-sibling::car/model = model]";
            IEnumerable<XElement> models = rootNode.XPathSelectElements(removeDuplicatesXPath);

            var newFileName = "CarsCollectionNoRepeats.xml";
            var currentDirectory = Directory.GetCurrentDirectory();
            var newFilePath = Path.Combine(currentDirectory, newFileName);
            using (var writer = new StreamWriter(newFilePath))
                foreach (var model in models)
                    writer.WriteLine(model);

        }
        private static void CreateXHTML() // zadanie 5
        {
            IEnumerable<XElement> cars = myCars
                .Select(car =>
                new XElement("tr", new XAttribute("style", "border: 3px solid black"),
                    new XElement("td", new XAttribute("style", "border: 3px double black"), car.getModel()),
                    new XElement("td", new XAttribute("style", "border: 3px double black"), car.getMotor().getModel()),
                    new XElement("td", new XAttribute("style", "border: 3px double black"), car.getMotor().getDisplacement()),
                    new XElement("td", new XAttribute("style", "border: 3px double black"), car.getMotor().getHorsePower()),
                    new XElement("td", new XAttribute("style", "border: 3px double black"), car.getYear())));
            XElement table = new XElement("table", new XAttribute("style", "border: 3px double black"), cars);
            XElement template = XElement.Load("template.html"); // pusty dokument
            if(template != null)
            {
                XElement body = template.Element("{http://www.w3.org/1999/xhtml}body");
                if (body != null)
                    body.Add(table);
                template.Save("templateWithTable.html");
            }          
        }

        private static void ModifyColection() // zadanie 6
        {
            XElement modifiedCollection = XElement.Load("CarsCollection.xml");
            foreach (var car in modifiedCollection.Elements())
            {
                foreach (var element in car.Elements())
                {
                    if (element.Name == "engine")
                    {
                        foreach (var engineElement in element.Elements())
                            if (engineElement.Name == "horsePower")
                                engineElement.Name = "hp";
                    }
                    else if (element.Name == "model")
                    {
                        var year = car.Element("year");
                        if(year != null)
                        {
                            XAttribute attribute = new XAttribute("year", year.Value);
                            element.Add(attribute);
                            year.Remove();
                        }    
                    }
                }
            }
            modifiedCollection.Save("CarsCollectionModified.xml");
        }
    }
}
