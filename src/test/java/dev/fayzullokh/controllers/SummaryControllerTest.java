package dev.fayzullokh.controllers;

import dev.fayzullokh.controller.SummaryController;
import dev.fayzullokh.entity.Summary;
import dev.fayzullokh.entity.Trainer;
import dev.fayzullokh.service.SummaryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SummaryControllerTest {

    @Mock
    private SummaryService summaryService;

    @InjectMocks
    private SummaryController summaryController;
    @Test
    public void testModify_validTrainer() {
        Trainer trainer = Trainer.builder()
                .id(1)
                .username("John.Doe").build();

        doNothing().when(summaryService).modifyWorkload(trainer);

        ResponseEntity<Void> response = summaryController.modify(trainer);

        verify(summaryService).modifyWorkload(trainer);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    public void testGetSummaries() {
        int trainerId = 1;
        Summary expectedSummary = new Summary();
        when(summaryService.getSummaryByTrainerId(trainerId)).thenReturn(expectedSummary);

        ResponseEntity<Summary> responseEntity = summaryController.getSummaries(trainerId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedSummary, responseEntity.getBody());
        verify(summaryService, times(1)).getSummaryByTrainerId(trainerId);
    }
}
