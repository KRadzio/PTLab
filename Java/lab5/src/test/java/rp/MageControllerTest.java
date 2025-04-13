package rp;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MageControllerTest {
    @Mock
    private MageRepository repository;
    @InjectMocks
    private MageController controller;

    @Test
    public void findExisting() {
        Mockito.when(repository.find(any())).thenReturn(Optional.of(new Mage("huj", 5)));

        var actual = controller.find("huj");

        verify(repository).find("huj");
        Assertions.assertThat(actual).isEqualTo("Mage [name=huj, level=5]");
    }

    @Test
    public void findNotExisting() {
        Mockito.when(repository.find(any())).thenReturn(Optional.empty());

        var actual = controller.find("huj");

        verify(repository).find("huj");
        Assertions.assertThat(actual).isEqualTo("not found");
    }

    @Test
    public void deleteNotExisting() {
        Mockito.doThrow(IllegalArgumentException.class).when(repository).delete(any());

        var actual = controller.delete("huj");

        verify(repository).delete("huj");
        Assertions.assertThat(actual).isEqualTo("not found");
    }

    @Test
    public void deleteExisting() {
        var actual = controller.delete("huj");

        verify(repository).delete("huj");
        Assertions.assertThat(actual).isEqualTo("done");
    }

    @Test
    public void saveNew() {
        var actual = controller.save("huj", "5");

        verify(repository).save(new Mage("huj", 5));
        Assertions.assertThat(actual).isEqualTo("done");
    }

    @Test
    public void saveExisting() {
        Mockito.doThrow(IllegalArgumentException.class).when(repository).save(any());

        var actual = controller.save("huj", "5");
    
        verify(repository).save(new Mage("huj", 5));
        Assertions.assertThat(actual).isEqualTo("bad request");
    }
}
