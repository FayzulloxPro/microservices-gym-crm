package dev.fayzullokh.repositories;

import dev.fayzullokh.entity.Summary;
import dev.fayzullokh.entity.Trainer;
import dev.fayzullokh.service.SummaryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SummaryRepositoryTest {

    @Mock
    private SummaryRepository summaryRepository;

    @InjectMocks
    private SummaryService summaryService;

    @Test
    public void testFindByTrainerId() {
        int trainerId = 1;
        Summary expectedSummary = new Summary();
        expectedSummary.setTrainer(Trainer.builder().id(trainerId).build());

        when(summaryRepository.findByTrainerId(trainerId)).thenReturn(expectedSummary);

        Summary actualSummary = summaryService.getSummaryByTrainerId(trainerId);

        Assertions.assertEquals(expectedSummary, actualSummary);

        verify(summaryRepository, times(1)).findByTrainerId(trainerId);
    }
}
