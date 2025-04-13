package rp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "MAGE")
@NamedQuery(name = "MageFindAll", query = "SELECT m FROM Mage m")
public class Mage {
    @Id
    @Column(name = "MAGE_NAME", length = 50, nullable = false, unique = true)
    private String name;

    @Column(name = "LEVEL", nullable = false, unique = false)
    private int level;

    @ManyToOne()
    @JoinColumn(name = "TOWER_ID", nullable = true)
    private Tower tower;

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

    public Tower getTower() {
        return tower;
    }

    public void setTower(Tower tower) {
        this.tower = tower;
    }

    @Override
    public String toString() {
        return "Mage [name=" + name + ", level=" + level + ", tower=" + tower + "]";
    }
}
