package rp;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        SortMode mode = SortMode.NATURAL;
        Mage m = new Mage("Gotard", 40, 20.5, mode,
                new Mage("Merlin", 22, 5.3, mode,
                        new Mage("Jan", 10, 1.1, mode)),
                new Mage("Karol", 21, 3.7, mode,
                        new Mage("Jakub", 15, 4.2, mode),
                        new Mage("Bernard", 13, 5.25, mode)),
                new Mage("Gandalf", 25, 7.0, mode,
                        new Mage("Barnaba", 10, 2.75, mode), new Mage("Eustachy", 17, 4.11, mode,
                                new Mage("Tadeusz", 5, 1.0, mode))));
        System.out.println(m.toString(1));
        System.out.println(m.generateStatistic(mode));
    }
}
