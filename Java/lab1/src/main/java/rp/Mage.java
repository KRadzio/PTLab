package rp;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;



public class Mage implements Comparable<Mage> {
    private final String name;
    private final int level;
    private final double power;
    private final Set<Mage> apprentices;

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public double getPower() {
        return power;
    }

    public Set<Mage> getApprentices() {
        return apprentices;
    }

    public Mage(String name, int level, double power, SortMode mode, Mage... apprentices) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(apprentices);
        Objects.requireNonNull(mode);
        this.name = name;
        this.level = level;
        this.power = power;
        if (mode == SortMode.NATURAL) {
            this.apprentices = new TreeSet<>();
            this.apprentices.addAll(Arrays.asList(apprentices));
        } else if (mode == SortMode.ALTERNATIVE) {
            this.apprentices = new TreeSet<>(new AlternativeComparator());
            this.apprentices.addAll(Arrays.asList(apprentices));
        } else {
            this.apprentices = new HashSet<>();
            this.apprentices.addAll(Arrays.asList(apprentices));
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, level, power, apprentices);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Mage other = (Mage) obj;
        return Objects.equals(name, other.name) && level == other.level
                && Double.doubleToLongBits(power) == Double.doubleToLongBits(other.power)
                && Objects.equals(apprentices, other.apprentices);
    }

    @Override
    public String toString() {
        return "Mage {name=" + name + ", level=" + level + ", power=" + power + "}";
    }

    public String toString(int indent) {
        StringBuilder sbuf = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            sbuf.append("-");
        }
        sbuf.append(toString());
        sbuf.append("\n");
        for (Mage m : apprentices) {
            sbuf.append(m.toString(indent + 1));
        }
        return sbuf.toString();
    }

    public Map<Mage, Integer> generateStatistic(SortMode mode) {
        Map<Mage, Integer> m;
        if (mode == SortMode.NATURAL) {
            m = new TreeMap<>();
        } else if (mode == SortMode.ALTERNATIVE) {
            m = new TreeMap<>(new AlternativeComparator());
        } else {
            m = new HashMap<>();
        }

        m.put(this, this.apprentices.size());
        for(Mage mg : apprentices)
        {
            m.putAll(mg.generateStatistic(mode));
            m.put(this, m.get(this) + m.get(mg));
        }
        return m;
    }

    @Override
    public int compareTo(Mage other) {
        int c = getName().compareTo(other.getName());
        if (c != 0)
            return c;
        c = Integer.compare(getLevel(), other.getLevel());
        if (c != 0)
            return c;
        return Double.compare(getPower(), other.getPower());
    }
}
/*
 * Naturalny porządek: name, level, power
 * 
 * Alternatywne kryterium: level, name, power
 * 
 * Reprezentacja tekstowa: Mage{name='', level=, power=}
 * 
 * Reprezentacja struktury rekurencyjnej: -Mage{name='', level=, power=}
 * --Mage{name='', level=, power=}
 * ---Mage{name='', level=, power=}
 * --Mage{name='', level=, power=}
 */
