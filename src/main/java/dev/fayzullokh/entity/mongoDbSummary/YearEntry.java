package dev.fayzullokh.entity.mongoDbSummary;

import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YearEntry {
    private int year;
    private List<MonthEntry> monthsList;
}

