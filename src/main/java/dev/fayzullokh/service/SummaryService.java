package dev.fayzullokh.service;

import dev.fayzullokh.entity.MonthlySummary;
import dev.fayzullokh.entity.Summary;
import dev.fayzullokh.entity.Trainer;
import dev.fayzullokh.entity.YearlySummary;
import dev.fayzullokh.enums.ActionType;
import dev.fayzullokh.repositories.SummaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class SummaryService {

    private final SummaryRepository summaryRepository;

    public void modifyWorkload(Trainer trainer) {
        log.info("Modifying workload: {}", trainer.getId());
        Summary summary = summaryRepository.findById(trainer.getId()).orElseGet(() -> new Summary(trainer.getId(), trainer, new ArrayList<>()));
        YearlySummary yearlySummary = getYearlySummary(summary, trainer);
        MonthlySummary monthlySummary = getMonthlySummary(yearlySummary, trainer);
        updateMonthlySummary(monthlySummary, trainer);
        summaryRepository.save(summary);
        log.info("Workload modified by summary: {}", summary.getId());
    }


    private YearlySummary getYearlySummary(Summary summary, Trainer trainer) {
        log.info("Getting summary by trainer id: {}", trainer.getId());
        return summary.getYearlySummaries().stream()
                .filter(yearlySummary -> yearlySummary.getYear().equals(Year.from(trainer.getStartDate())))
                .findFirst()
                .orElseGet(() -> createYearlySummary(summary, trainer));
    }

    private YearlySummary createYearlySummary(Summary summary, Trainer trainer) {
        log.info("Creating new yearly salary by trainer: {}", trainer.getId());
        YearlySummary yearlySummary = YearlySummary.builder()
                .year(Year.from(trainer.getStartDate()))
                .monthlySummaries(new ArrayList<>())
                .build();
        yearlySummary.setSummary(summary);
        summary.getYearlySummaries().add(yearlySummary);
        log.info("Created new yearly salary by trainer: {}", trainer.getId());
        return yearlySummary;
    }

    private MonthlySummary getMonthlySummary(YearlySummary yearlySummary, Trainer trainer) {
        log.info("Getting monthly salary by trainer: {}", trainer.getId());
        return yearlySummary.getMonthlySummaries().stream()
                .filter(monthlySummary -> monthlySummary.getMonth().equals(YearMonth.from(trainer.getStartDate()).getMonthValue()))
                .findFirst()
                .orElseGet(() -> createMonthlySummary(yearlySummary, trainer));
    }

    private MonthlySummary createMonthlySummary(YearlySummary yearlySummary, Trainer trainer) {
        log.info("Creating new monthly salary by trainer: {}", trainer.getId());
        MonthlySummary monthlySummary = MonthlySummary.builder()
                .month(YearMonth.from(trainer.getStartDate()).getMonthValue())
                .trainingSummaryDuration(0)
                .build();
        monthlySummary.setYearlySummary(yearlySummary);
        yearlySummary.getMonthlySummaries().add(monthlySummary);
        log.info("Created new monthly salary by trainer: {}", trainer.getId());
        return monthlySummary;
    }

    private void updateMonthlySummary(MonthlySummary monthlySummary, Trainer trainer) {
        log.info("Updating monthly salary by trainer: {}", trainer.getId());
        if (trainer.getActionType().equals(ActionType.ADD)) {
            monthlySummary.setTrainingSummaryDuration(monthlySummary.getTrainingSummaryDuration() + trainer.getDuration());
            log.info("Added new monthly salary by trainer: {}", trainer.getId());
        } else {
            monthlySummary.setTrainingSummaryDuration(monthlySummary.getTrainingSummaryDuration() - trainer.getDuration());
            log.info("Updated monthly salary by trainer: {}", trainer.getId());
        }
    }

    public Summary getSummaryByTrainerId(int trainerId) {
        log.info("Getting summary by trainer id: {}", trainerId);
        return summaryRepository.findByTrainerId(trainerId);
    }

    public Summary getSummaryByTrainerUserName(String trainerId) {
        log.info("Getting summary by trainer id: {}", trainerId);
        return summaryRepository.findByTrainer(trainerId);
    }

}