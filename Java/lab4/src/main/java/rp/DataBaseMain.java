package rp;

import java.util.Scanner;

import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import rp.entity.Mage;
import rp.entity.Tower;

public class DataBaseMain {

    private static Scanner sc;

    public static void main(String[] args) {
        sc = new Scanner(System.in);
        var factory = Persistence.createEntityManagerFactory("myPersistenceUnit");
        var manager = factory.createEntityManager();
        insertExampleData(manager);
        var zn = "";
        var runApp = true;
        while(runApp){
            zn = sc.nextLine();
            switch (zn) {
                case "q":
                    runApp = false;
                    break;
                case "m":
                    insertNewMage(manager);
                    break;
                case "t":
                    insertNewTower(manager);
                    break;
                case "f":
                    query(manager);
                    break;
                case "p":
                    printDatabaseContent(manager);
                    break;
                case "d":
                    deleteMage(manager);
                    break;
                case "r":
                    deleteTower(manager);
                    break;
                default:
                    break;
            }

        }
        sc.close();
    }

    private static void insertExampleData(EntityManager manager) {
        try {
            manager.getTransaction().begin();

            var towerHigh = new Tower();
            towerHigh.setName("H");
            towerHigh.setHeight(100);
            manager.persist(towerHigh);

            var towerMedium = new Tower();
            towerMedium.setName("M");
            towerMedium.setHeight(70);
            manager.persist(towerMedium);

            var mageWarol = new Mage();
            mageWarol.setName("Gotard");
            mageWarol.setLevel(50);
            mageWarol.setTower(towerHigh);
            manager.persist(mageWarol);

            var mageDebil = new Mage();
            mageDebil.setName("Merlin");
            mageDebil.setLevel(10);
            mageDebil.setTower(towerHigh);
            manager.persist(mageDebil);

            var mageSzmata = new Mage();
            mageSzmata.setName("Gandalf");
            mageSzmata.setLevel(20);
            mageSzmata.setTower(towerMedium);
            manager.persist(mageSzmata);

            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
        }
    }

    private static void query(EntityManager manager) {
        try {
            manager.getTransaction().begin();
            System.out.println("Insert query");
            var query = sc.nextLine();

            Query q = manager.createQuery(query, Object.class);
            var results = q.getResultList();
            for (var r : results) {
                System.out.println(r.toString());
            }
            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
        }
    }

    private static void deleteMage(EntityManager manager) {
        try {
            manager.getTransaction().begin();
            System.out.println("Insert mage name");
            var name = sc.nextLine();
            Mage dltMage = manager.find(Mage.class, name);
            if (dltMage != null) {
                manager.remove(dltMage);
                manager.getTransaction().commit();
            } else
                manager.getTransaction().rollback();
        } catch (Exception e) {
            manager.getTransaction().rollback();
        }
    }

    private static void deleteTower(EntityManager manager) {
        try {
            manager.getTransaction().begin();
            System.out.println("Insert tower name");
            var name = sc.nextLine();
            Tower dltTower = manager.find(Tower.class, name);
            if (dltTower != null) {
                manager.remove(dltTower);
                manager.getTransaction().commit();
            } else
                manager.getTransaction().rollback();
        } catch (Exception e) {
            manager.getTransaction().rollback();
        }
    }

    private static void insertNewMage(EntityManager manager) {
        try {
            manager.getTransaction().begin();
            var newMage = new Mage();
            System.out.println("Insert mage name");
            var name = sc.nextLine();
            newMage.setName(name);
            System.out.println("Insert mage level");
            var level = sc.nextInt();
            newMage.setLevel(level);
            sc.nextLine();
            System.out.println("Insert tower name");
            String towerName = sc.nextLine();
            Tower dstTower = manager.find(Tower.class, towerName);
            if (dstTower != null) {
                newMage.setTower(dstTower);
                manager.persist(newMage);
                manager.getTransaction().commit();
            } else
                manager.getTransaction().rollback();

        } catch (Exception e) {
            manager.getTransaction().rollback();
        }
    }

    private static void insertNewTower(EntityManager manager) {
        try {
            manager.getTransaction().begin();
            var newTower = new Tower();
            System.out.println("Insert tower name");
            String name = sc.nextLine();
            newTower.setName(name);
            System.out.println("Insert tower height");
            var height = sc.nextInt();
            newTower.setHeight(height);
            manager.persist(newTower);
            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
        }
    }

    private static void printDatabaseContent(EntityManager manager) {

        var towers = manager.createNamedQuery("TowerFindAll", Tower.class).getResultList();
        for (var tower : towers) {
            System.out.println(String.format("tower name %s, height %d", tower.getName(), tower.getHeight()));
        }
        var mages = manager.createNamedQuery("MageFindAll", Mage.class).getResultList();
        for (var mage : mages) {
            if (mage.getTower() != null)
                System.out.println(String.format("mag name %s, level %d, belongs to tower %s", mage.getName(),
                        mage.getLevel(), mage.getTower().getName()));
            else
                System.out.println(String.format("mag name %s, level %d, belongs to no tower", mage.getName(),
                        mage.getLevel()));
        }
    }
}
