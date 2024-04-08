package dev.fayzullokh.services;

import dev.fayzullokh.entity.Trainer;
import dev.fayzullokh.entity.mongoDbSummary.MonthEntry;
import dev.fayzullokh.entity.mongoDbSummary.TrainerDocument;
import dev.fayzullokh.entity.mongoDbSummary.YearEntry;
import dev.fayzullokh.repositories.TrainerDocumentRepository;
import dev.fayzullokh.service.TrainerDocumentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TrainerDocumentServiceTest {

    @Mock
    private TrainerDocumentRepository trainerDocumentRepository;

    @InjectMocks
    private TrainerDocumentService trainerDocumentService;

    @Test
    public void testFindByUsername_found() throws Exception {
        String username = "test_user";
        TrainerDocument expectedTrainerDocument = new TrainerDocument();
        expectedTrainerDocument.setUsername(username);

        Mockito.when(trainerDocumentRepository.findByUsername(username)).thenReturn(expectedTrainerDocument);

        TrainerDocument actualTrainerDocument = trainerDocumentService.findByUsername(username);

        assertEquals(expectedTrainerDocument, actualTrainerDocument);
        verify(trainerDocumentRepository, times(1)).findByUsername(username);
    }


    @Test
    public void testUpdateTrainingsSummaryDuration() {
        String username = "test_user";
        TrainerDocument trainerDocument = new TrainerDocument();
        trainerDocument.setUsername(username);

        Mockito.when(trainerDocumentRepository.findByUsername(username)).thenReturn(trainerDocument);

        trainerDocumentService.updateTrainingsSummaryDuration(trainerDocument);

        verify(trainerDocumentRepository, times(1)).findByUsername(username);
        verify(trainerDocumentRepository, times(1)).insert(trainerDocument);
    }
    @Test
    public void testProcessingNewEvent_newTrainer() throws Exception {
        String username = "test_user";
        LocalDateTime localDateTime =LocalDateTime.now();
        int duration = 60;
        Trainer trainer = Trainer.builder()
                .startDate(localDateTime)
                .username(username)
                .duration(duration)
                .build();
        Mockito.when(trainerDocumentRepository.findByUsername(username)).thenReturn(null);

        trainerDocumentService.processingNewEvent(trainer);

        verify(trainerDocumentRepository, times(1)).findByUsername(username);
        verify(trainerDocumentRepository, times(1)).insert(Mockito.any(TrainerDocument.class));
    }
    @Test
    public void testProcessingNewEvent_existingTrainerExistingYearMonth() throws Exception {
        String username = "test_user";
        LocalDateTime localDateTime =LocalDateTime.now();
        int duration = 60;
        Trainer trainer = Trainer.builder()
                .startDate(localDateTime)
                .username(username)
                .duration(duration)
                .build();
        TrainerDocument existingTrainerDoc = new TrainerDocument();
        existingTrainerDoc.setUsername(username);
        existingTrainerDoc.setYearsList(List.of(
                YearEntry.builder().year(localDateTime.getYear()).monthsList(List.of(
                        MonthEntry.builder().month(localDateTime.getMonth().toString()).trainingsSummaryDuration(0).build()
                )).build()
        ));

        Mockito.when(trainerDocumentRepository.findByUsername(username)).thenReturn(existingTrainerDoc);

        trainerDocumentService.processingNewEvent(trainer);

        verify(trainerDocumentRepository, times(1)).findByUsername(username);
    }



}
