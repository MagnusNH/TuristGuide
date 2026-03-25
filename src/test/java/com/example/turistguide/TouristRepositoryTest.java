package com.example.turistguide;

import com.example.turistguide.model.TouristAttraction;
import com.example.turistguide.repository.TouristRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:h2init.sql", executionPhase = BEFORE_TEST_METHOD)
class TouristRepositoryTest {

    @Autowired
    private TouristRepository repo;

    @Test
    void readAll() {
        List<TouristAttraction> all = repo.getAllAttractions();

        assertThat(all).isNotNull();
        assertThat(all.size()).isEqualTo(2);

        TouristAttraction tivoli = all.get(0);
        TouristAttraction kronborg = all.get(1);

        assertThat(tivoli.getName()).isEqualTo("Tivoli");
        assertThat(kronborg.getName()).isEqualTo("Kronborg slot");

        assertThat(tivoli.getCity()).isEqualTo("København");
        assertThat(kronborg.getCity()).isEqualTo("Helsingør");

        assertThat(tivoli.getTags()).contains("Forlystelsespark");
        assertThat(kronborg.getTags()).contains("Slot");
    }

    @Test
    void insertAndReadBack() {
        TouristAttraction attraction = new TouristAttraction();
        attraction.setName("Den Lille Havfrue");
        attraction.setDescription("Et berømt monument i København");
        attraction.setCity("København");
        attraction.setTags(List.of("Monument"));

        // Add the tag manually for the test setup (H2 doesn’t auto-fill tags)
        repo.addAttraction(attraction);

        List<TouristAttraction> all = repo.getAllAttractions();
        assertThat(all.stream().anyMatch(a -> a.getName().equals("Den Lille Havfrue"))).isTrue();
    }

    @Test
    void updateAttraction() {
        TouristAttraction updated = new TouristAttraction();
        updated.setName("Tivoli");
        updated.setDescription("Opdateret beskrivelse");
        updated.setCity("København");
        updated.setTags(List.of("Forlystelsespark"));

        boolean updatedOk = repo.updateAttraction("Tivoli", updated);
        assertThat(updatedOk).isTrue();

        List<TouristAttraction> all = repo.getAllAttractions();
        assertThat(all.stream()
                .filter(a -> a.getName().equals("Tivoli"))
                .findFirst()
                .orElseThrow()
                .getDescription()).isEqualTo("Opdateret beskrivelse");
    }

    @Test
    void deleteAttraction() {
        boolean deleted = repo.deleteAttraction("Kronborg slot");
        assertThat(deleted).isTrue();

        List<TouristAttraction> all = repo.getAllAttractions();
        assertThat(all.stream().anyMatch(a -> a.getName().equals("Kronborg slot"))).isFalse();
    }
}
