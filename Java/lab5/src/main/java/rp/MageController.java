package rp;

public class MageController {
    private MageRepository repository;

    public MageController(MageRepository repository) {
        this.repository = repository;
    }

    public String find(String name) {
        var mageFoundOpt = repository.find(name);
        return mageFoundOpt.isPresent()
                ? mageFoundOpt.get().toString()
                : "not found";
    }

    public String delete(String name) {
        try {
            repository.delete(name);
            return "done";
        } catch (Exception e) {
            return "not found";
        }
    }

    public String save(String name, String level) {
        try {
            repository.save(new Mage(name, Integer.parseInt(level)));
            return "done";
        } catch (Exception e) {
            return "bad request";
        }
    }
}
