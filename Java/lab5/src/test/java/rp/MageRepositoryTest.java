package rp;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class MageRepositoryTest {
    private MageRepository repository = new MageRepository();

    @Test
    public void findExisting() {
        repository.save(new Mage("huj", 5));

        var actual = repository.find("huj");

        Assertions.assertThat(actual).isPresent();
    }

    @Test
    public void findNotExisting() {
        var actual = repository.find("huj");

        Assertions.assertThat(actual).isEmpty();
    }

    @Test
    public void saveNew() {
        Assertions.assertThatCode(() -> repository.save(new Mage("huj", 5))).doesNotThrowAnyException();
    }

    @Test
    public void saveExisting() {
        repository.save(new Mage("huj", 5));

        Assertions.assertThatCode(() -> repository.save(new Mage("huj", 5)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void deleteExisting() {
        repository.save(new Mage("huj", 5));
        Assertions.assertThatCode(() -> repository.delete("huj")).doesNotThrowAnyException();
    }

    @Test
    public void deleteNotExisting() {
        Assertions.assertThatCode(() -> repository.delete("huj")).isInstanceOf(IllegalArgumentException.class);
    }
}
