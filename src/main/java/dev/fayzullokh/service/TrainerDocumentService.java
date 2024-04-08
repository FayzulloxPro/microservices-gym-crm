package dev.fayzullokh.service;

import dev.fayzullokh.entity.Trainer;
import dev.fayzullokh.entity.mongoDbSummary.MonthEntry;
import dev.fayzullokh.entity.mongoDbSummary.TrainerDocument;
import dev.fayzullokh.entity.mongoDbSummary.YearEntry;
import dev.fayzullokh.repositories.TrainerDocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainerDocumentService {

    private final TrainerDocumentRepository trainerDocumentRepository;

    @Transactional
    public TrainerDocument findByUsername(String username) {
        MDC.put("transactionId", UUID.randomUUID().toString());
        log.info("Transaction started for findByUsername. Username: {}", username);
        try {
            TrainerDocument result = trainerDocumentRepository.findByUsername(username);
            log.info("findByUsername completed successfully. Result: {}", result);
            return result;
        } catch (Exception e) {
            log.error("Error in findByUsername", e);
            throw e;
        } finally {
            MDC.clear();
            log.info("Transaction ended for findByUsername.");
        }
    }

    @Transactional
    public void updateTrainingsSummaryDuration(TrainerDocument trainerDocument) {
        MDC.put("transactionId", UUID.randomUUID().toString());
        String username = trainerDocument.getUsername();
        log.info("Transaction started for updateTrainingsSummaryDuration. Username: {}", username);
        try {
            TrainerDocument byUsername = trainerDocumentRepository.findByUsername(username);
            byUsername.setFirstName(trainerDocument.getFirstName());
            byUsername.setLastName(trainerDocument.getLastName());
            byUsername.setStatus(trainerDocument.getStatus());
            byUsername.setYearsList(trainerDocument.getYearsList());
            trainerDocumentRepository.insert(trainerDocument);
            log.info("updateTrainingsSummaryDuration completed successfully for Username: {}", username);
        } catch (Exception e) {
            log.error("Error in updateTrainingsSummaryDuration", e);
            throw e;
        } finally {
            MDC.clear();
            log.info("Transaction ended for updateTrainingsSummaryDuration.");
        }
    }

    @Transactional
    public void processingNewEvent(Trainer trainer) {
        MDC.put("transactionId", UUID.randomUUID().toString());
        String username = trainer.getUsername();
        log.info("Transaction started for processingNewEvent. Username: {}", username);
        try {
            TrainerDocument trainerDocument = trainerDocumentRepository.findByUsername(username);
            int year = trainer.getStartDate().getYear();

            if (trainerDocument == null) {
                String month = trainer.getStartDate().format(DateTimeFormatter.ofPattern("MMMM"));

                TrainerDocument newTrainer = TrainerDocument.builder()
                        .username(trainer.getUsername())
                        .yearsList(List.of(
                                YearEntry.builder()
                                        .year(year)
                                        .monthsList(List.of(
                                                MonthEntry.builder()
                                                        .month(month)
                                                        .trainingsSummaryDuration(trainer.getDuration())
                                                        .build()
                                        ))
                                        .build()
                        ))
                        .build();

                trainerDocumentRepository.insert(newTrainer);
                log.info("Saved new TrainerDocument for Username: {}", username);
            } else {
                String month = trainer.getStartDate().format(DateTimeFormatter.ofPattern("MMMM"));

                YearEntry foundYear = trainerDocument.getYearsList().stream()
                        .filter(y -> y.getYear() == year)
                        .findFirst()
                        .orElse(null);

                if (foundYear != null) {
                    MonthEntry foundMonth = foundYear.getMonthsList().stream()
                            .filter(m -> m.getMonth().equals(month))
                            .findFirst()
                            .orElse(null);

                    if (foundMonth != null) {
                        int updatedDuration = foundMonth.getTrainingsSummaryDuration() + trainer.getDuration();
                        foundMonth.setTrainingsSummaryDuration(updatedDuration);

                        trainerDocumentRepository.insert(trainerDocument);
                        log.info("Updated TrainerDocument after processing new event for Username: {}", username);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error in processingNewEvent", e);
            throw e;
        } finally {
            MDC.clear();
            log.info("Transaction ended for processingNewEvent.");
        }
    }
}
