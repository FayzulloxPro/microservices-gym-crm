package dev.fayzullokh.services;
import dev.fayzullokh.entity.Trainer;
import dev.fayzullokh.entity.mongoDbSummary.MonthEntry;
import dev.fayzullokh.entity.mongoDbSummary.TrainerDocument;
import dev.fayzullokh.entity.mongoDbSummary.YearEntry;
import dev.fayzullokh.repositories.TrainerDocumentRepository;
import dev.fayzullokh.service.TrainerDocumentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class TrainerServiceTest {

    @Mock
    private TrainerDocumentRepository trainerRepository;

    @InjectMocks
    private TrainerDocumentService trainerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        trainerService = new TrainerDocumentService(trainerRepository);
    }

    @Test
    void findByUsername_Success() {
        when(trainerRepository.findByUsername(anyString())).thenReturn(createMockTrainerDocument());
         trainerService.findByUsername("john.doe");
        verify(trainerRepository, times(1)).findByUsername("john.doe");
    }

    @Test
    void updateTrainingsSummaryDuration_Success() {
        TrainerDocument existingTrainer = createMockTrainerDocument();
        when(trainerRepository.findByUsername(anyString())).thenReturn(existingTrainer);
        TrainerDocument updatedTrainer = createMockTrainerDocument();
        updatedTrainer.setFirstName("UpdatedFirstName");
        trainerService.updateTrainingsSummaryDuration(updatedTrainer);
        verify(trainerRepository, times(1)).findByUsername(anyString());
    }

    @Test
    void processingNewEvent_NewTrainer_Success() {
        when(trainerRepository.findByUsername(anyString())).thenReturn(null);
        Trainer newTrainer = createMockTrainer();
        trainerService.processingNewEvent(newTrainer);
        verify(trainerRepository, times(1)).findByUsername("john.doe");

    }

    @Test
    void processingNewEvent_ExistingTrainer_Success() {
        Trainer trainer = createMockTrainer();
        when(trainerRepository.findByUsername(anyString())).thenReturn(createMockTrainerDocument());
        trainerService.processingNewEvent(trainer);
        verify(trainerRepository, times(1)).findByUsername("john.doe");
    }

    private TrainerDocument createMockTrainerDocument() {
        return TrainerDocument.builder()
                .username("john.doe")
                .firstName("John")
                .lastName("Doe")
                .status(true)
                .yearsList(Collections.singletonList(YearEntry
                                .builder()
                                .year(2024)
                                .monthsList(Collections.singletonList(MonthEntry
                                        .builder()
                                                .month("January")
                                                .trainingsSummaryDuration(30)
                                        .build()))
                        .build()))
                .build();
    }

    private Trainer createMockTrainer() {
        return Trainer.builder()
                .username("john.doe")
                .startDate(LocalDateTime.now())
                .duration(3)
                .build();
    }
}
