package rp.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "TOWER")
@NamedQuery(name = "TowerFindAll", query = "SELECT t FROM Tower t")
public class Tower {
    @Id
    @Column(name = "TOWER_NAME", length = 50, nullable = false, unique = true)
    private String name;

    @Column(name = "HEIGHT", nullable = false, unique = false)
    private int height;

    @OneToMany(mappedBy = "tower", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Mage> mages = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<Mage> getMages() {
        return mages;
    }

    public void setMages(List<Mage> mages) {
        this.mages = mages;
    }

    @Override
    public String toString() {
        return "Tower [name=" + name + ", height=" + height + "]";
    }
}
