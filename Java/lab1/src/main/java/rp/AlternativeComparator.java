package rp;

import java.util.Comparator;

public class AlternativeComparator implements Comparator<Mage> {
    @Override
    public int compare(Mage m1, Mage m2) { 
        int c = Integer.compare(m1.getLevel(), m2.getLevel());
        if (c != 0)
            return c;
        c = m1.getName().compareTo(m2.getName());
        if (c != 0)
            return c;
        return Double.compare(m1.getPower(), m2.getPower());
    }
}
