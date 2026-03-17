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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TouristServiceTest {

    @Mock
    private TouristRepository repository;

    @InjectMocks
    private TouristService service;

    @Test
    void findAttractionByName_shouldReturnAttraction_whenNameMatches() {
        // Arrange - set up fake data
        TouristAttraction tivoli = new TouristAttraction(
                "Tivoli", "Forlystelsespark", "København", List.of("forlystelsespark")
        );
        when(repository.getAllAttractions()).thenReturn(List.of(tivoli));

        // Act - call the method we're testing
        TouristAttraction result = service.findAttractionByName("tivoli");

        // Assert - check the result is what we expect
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
    void deleteAttraction_shouldReturnNull_whenAttractionDoesNotExist() {
        // Arrange
        when(repository.getAllAttractions()).thenReturn(List.of());

        // Act
        TouristAttraction result = service.deleteAttraction("ukendt");

        // Assert
        assertNull(result);
    }
}