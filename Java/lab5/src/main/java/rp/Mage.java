package rp;

import java.util.Objects;

public class Mage {
    private String name;

    private int level;

    public Mage(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, level);
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
        return Objects.equals(name, other.name) && level == other.level;
    }

    @Override
    public String toString() {
        return "Mage [name=" + name + ", level=" + level + "]";
    }

}
