package com.example.turistguide;

import com.example.turistguide.model.TouristAttraction;
import com.example.turistguide.repository.TouristRepository;
import com.example.turistguide.service.TouristService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TouristServiceTest {

    @Mock
    private TouristRepository repository;

    @InjectMocks
    private TouristService service;

    @Test
    void findAttractionByName_shouldReturnAttraction_whenNameMatches() {
        // Arrange
        TouristAttraction tivoli = new TouristAttraction(
                1, "Tivoli", "Forlystelsespark i centrum af København", "København", List.of("forlystelsespark")
        );
        when(repository.getAllAttractions()).thenReturn(List.of(tivoli));

        // Act
        TouristAttraction result = service.findAttractionByName("tivoli");

        // Assert
        assertNotNull(result);
        assertEquals("Tivoli", result.getName());
    }

    @Test
    void findAttractionByName_shouldReturnNull_whenAttractionDoesNotExist() {
        // Arrange
        when(repository.getAllAttractions()).thenReturn(List.of());

        // Act
        TouristAttraction result = service.findAttractionByName("ukendt");

        // Assert
        assertNull(result);
    }

    @Test
    void findAttractionByName_shouldBeCaseInsensitive() {
        // Arrange
        TouristAttraction tivoli = new TouristAttraction(
                1, "Tivoli", "Forlystelsespark", "København", List.of()
        );
        when(repository.getAllAttractions()).thenReturn(List.of(tivoli));

        // Act
        TouristAttraction result = service.findAttractionByName("TIVOLI");

        // Assert
        assertNotNull(result);
        assertEquals("Tivoli", result.getName());
    }

    @Test
    void deleteAttraction_shouldReturnNull_whenAttractionDoesNotExist() {
        // Arrange
        when(repository.getAllAttractions()).thenReturn(List.of());

        // Act
        TouristAttraction result = service.deleteAttraction("ukendt");

        // Assert
        assertNull(result);
    }

    @Test
    void deleteAttraction_shouldReturnDeletedAttraction_whenAttractionExists() {
        // Arrange
        TouristAttraction tivoli = new TouristAttraction(
                1, "Tivoli", "Forlystelsespark", "København", List.of("forlystelsespark")
        );
        when(repository.getAllAttractions()).thenReturn(List.of(tivoli));
        when(repository.deleteAttraction("Tivoli")).thenReturn(true);

        // Act
        TouristAttraction result = service.deleteAttraction("Tivoli");

        // Assert
        assertNotNull(result);
        assertEquals("Tivoli", result.getName());
    }

    @Test
    void updateAttraction_shouldReturnNull_whenAttractionDoesNotExist() {
        // Arrange
        when(repository.getAllAttractions()).thenReturn(List.of());

        TouristAttraction newValues = new TouristAttraction(
                0, "Tivoli", "Opdateret beskrivelse", "København", List.of()
        );

        // Act
        TouristAttraction result = service.updateAttraction("ukendt", newValues);

        // Assert
        assertNull(result);
    }

    @Test
    void updateAttraction_shouldReturnUpdatedAttraction_whenAttractionExists() {
        // Arrange
        TouristAttraction existing = new TouristAttraction(
                1, "Tivoli", "Gammel beskrivelse", "København", List.of()
        );
        TouristAttraction newValues = new TouristAttraction(
                1, "Tivoli", "Ny beskrivelse", "Aarhus", List.of("forlystelsespark")
        );

        when(repository.getAllAttractions()).thenReturn(List.of(existing));
        when(repository.updateAttraction(eq("Tivoli"), any(TouristAttraction.class))).thenReturn(true);

        // Act
        TouristAttraction result = service.updateAttraction("Tivoli", newValues);

        // Assert
        assertNotNull(result);
        assertEquals("Ny beskrivelse", result.getDescription());
        assertEquals("Aarhus", result.getCity());
    }
}