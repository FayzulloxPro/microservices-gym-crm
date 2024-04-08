package dev.fayzullokh.controller;


import dev.fayzullokh.entity.Summary;
import dev.fayzullokh.entity.Trainer;
import dev.fayzullokh.service.SummaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SummaryController {

    private final SummaryService summaryService;


    @PostMapping("/modify")
    public ResponseEntity<Void> modify(@RequestBody @NonNull Trainer trainer) {
        log.info("Modifying workload with trainerId: {}", trainer.getId());
        summaryService.modifyWorkload(trainer);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/summary")
    public ResponseEntity<Summary> getSummaries(@RequestBody int id) {
        log.info("Getting workload of trainer by trainerId: {}", id);
        return ResponseEntity.ok(summaryService.getSummaryByTrainerId(id));
    }

}
