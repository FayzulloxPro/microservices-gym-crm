package dev.fayzullokh.services;

import dev.fayzullokh.entity.Summary;
import dev.fayzullokh.entity.Trainer;
import dev.fayzullokh.enums.ActionType;
import dev.fayzullokh.repositories.SummaryRepository;
import dev.fayzullokh.service.SummaryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

@ExtendWith(MockitoExtension.class)
class SummaryServiceTest {

    @Mock
    private SummaryRepository summaryRepository;

    @InjectMocks
    private SummaryService summaryService;

    @Test
    void testModifyWorkloadDeleteActionType() {
        Trainer trainer = new Trainer();
        trainer.setId(1);
        trainer.setStartDate(LocalDateTime.now());
        trainer.setActionType(ActionType.DELETE);

        Summary summary = new Summary(1, trainer, new ArrayList<>());

        Mockito.when(summaryRepository.findById(1)).thenReturn(Optional.of(summary));

        Mockito.when(summaryRepository.save(Mockito.any(Summary.class))).thenReturn(summary);

        summaryService.modifyWorkload(trainer);

        assertNotNull(summary.getYearlySummaries());
        Assertions.assertEquals(1, summary.getYearlySummaries().size());
        Assertions.assertEquals(Year.now(), summary.getYearlySummaries().get(0).getYear());
    }

    @Test
    void testModifyAddActionType() {
        Trainer trainer = new Trainer();
        trainer.setId(1);
        trainer.setStartDate(LocalDateTime.now());
        trainer.setActionType(ActionType.DELETE);

        Summary summary = new Summary(1, trainer, new ArrayList<>());

        Mockito.when(summaryRepository.findById(1)).thenReturn(Optional.of(summary));

        Mockito.when(summaryRepository.save(Mockito.any(Summary.class))).thenReturn(summary);

        summaryService.modifyWorkload(trainer);

        assertNotNull(summary.getYearlySummaries());
        Assertions.assertEquals(1, summary.getYearlySummaries().size());
        Assertions.assertEquals(Year.now(), summary.getYearlySummaries().get(0).getYear());
    }

    @Test
    void testGetSummaryByTrainerId() {
        int id = 1;
        Trainer trainer= Trainer.builder().id(id).build();

        Mockito.when(summaryRepository.findByTrainerId(id)).thenReturn(new Summary(id, trainer, new ArrayList<>()));

        Summary summary = summaryService.getSummaryByTrainerId(id);

        assertNotNull(summary);}

}
