using System;
using System.Collections;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;

namespace lab10
{
    class CarBindingList : BindingList<Car>
    {
        private ArrayList selectedIndices;
        private PropertyDescriptor sortPropertyValue;
        private ListSortDirection sortDirectionValue;
        private bool isSortedValue = false;

        public CarBindingList(List<Car> list)
        {
            if (list != null)
                foreach (var car in list)
                    Add(car);
        }

        protected override bool SupportsSearchingCore
        {
            get { return true; }
        }
        protected override bool SupportsSortingCore
        {
            get { return true; }
        }
        protected override PropertyDescriptor SortPropertyCore
        {
            get { return sortPropertyValue; }
        }
        protected override bool IsSortedCore
        {
            get { return isSortedValue; }
        }

        public void Sort(string property, ListSortDirection direction)
        {
            PropertyDescriptorCollection properties = TypeDescriptor.GetProperties(typeof(Car));
            PropertyDescriptor prop = properties.Find(property, true);
            if (prop != null)
                ApplySortCore(prop, direction);
        }


        protected override void ApplySortCore(PropertyDescriptor prop, ListSortDirection direction)
        {
            var sortedList = new ArrayList();
            var unsortedList = new ArrayList(Count);
            if (prop.PropertyType.GetInterface("IComparable") != null)
            {
                sortPropertyValue = prop;
                sortDirectionValue = direction;

                foreach (Car car in Items)
                    if (!sortedList.Contains(prop.GetValue(car)))
                        sortedList.Add(prop.GetValue(car));
                sortedList.Sort();
                if (direction == ListSortDirection.Descending)
                    sortedList.Reverse();

                for (int i = 0; i < sortedList.Count; i++)
                {
                    var foundIndices = FindIndices(prop.Name, sortedList[i]);
                    if (foundIndices != null)
                        foreach (var index in foundIndices)
                            unsortedList.Add(Items[index]);
                }

                if (unsortedList != null)
                {
                    Clear();
                    foreach (Car elem in unsortedList)
                        Add(elem);
                    isSortedValue = true;
                    OnListChanged(new ListChangedEventArgs(ListChangedType.Reset, -1));
                }
            }
        }
        private int FindCore(PropertyDescriptor prop, object key, bool isEngine)
        {
            PropertyInfo propInfo;
            if (isEngine)
                propInfo = typeof(Engine).GetProperty(prop.Name);
            else
                propInfo = typeof(Car).GetProperty(prop.Name);
            selectedIndices = new ArrayList();
            int found = -1;

            if (key != null)
            {
                for (int i = 0; i < Count; i++)
                {
                    if (isEngine)
                    {
                        double neverused;
                        if (Double.TryParse(key.ToString(), out neverused))
                        {
                            if (propInfo.GetValue(Items[i].motor, null).Equals(Double.Parse(key.ToString())))
                            {
                                found++;
                                selectedIndices.Add(i);
                            }
                        }
                        else
                        {
                            if (propInfo.GetValue(Items[i].motor, null).Equals(key))
                            {
                                found++;
                                selectedIndices.Add(i);
                            }
                        }

                    }
                    else
                    {
                        if (propInfo.GetValue(Items[i], null).Equals(key))
                        {
                            found++;
                            selectedIndices.Add(i);
                        }
                    }
                }
            }
            return found;
        }

        public int[] FindIndices(string property, object key)
        {
            PropertyDescriptorCollection properties;
            bool isEngine = property.Contains("motor.");
            if (isEngine)
            {
                properties = TypeDescriptor.GetProperties(typeof(Engine));
                property = property.Split('.').Last();
            }
            else
                properties = TypeDescriptor.GetProperties(typeof(Car));
            PropertyDescriptor prop = properties.Find(property, true);
            if (prop != null)
                if (FindCore(prop, key, isEngine) >= 0)
                    return (int[])(selectedIndices.ToArray(typeof(int)));
            return null;

        }

    }
}
