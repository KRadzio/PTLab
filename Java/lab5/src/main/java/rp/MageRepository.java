package rp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class MageRepository {
    private Collection<Mage> collection = new ArrayList<>();

    public Optional<Mage> find(String name) {
        return collection.stream()
                .filter(mage -> mage.getName().equals(name))
                .findFirst();
    }

    public void delete(String name) {
        var existingMageOpt = find(name);
        if(existingMageOpt.isEmpty()) {
            throw new IllegalArgumentException("");
        }
        collection.remove(existingMageOpt.get());
    }

    public void save(Mage mage) {
        var existingMageOpt = find(mage.getName());
        if(existingMageOpt.isPresent()){
            throw new IllegalArgumentException("");
        }
        collection.add(mage);
    }
}
