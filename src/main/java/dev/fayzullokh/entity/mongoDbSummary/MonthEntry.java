package dev.fayzullokh.entity.mongoDbSummary;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthEntry {
    private String month;
    private int trainingsSummaryDuration;
}