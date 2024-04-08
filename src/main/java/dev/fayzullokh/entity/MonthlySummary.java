package dev.fayzullokh.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"yearly_summary_id", "month"}))
public class MonthlySummary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "yearly_summary_id", nullable = false)
    private YearlySummary yearlySummary;

    private Integer month;
    private Integer trainingSummaryDuration;
}
