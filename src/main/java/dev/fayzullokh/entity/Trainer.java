package dev.fayzullokh.entity;

import dev.fayzullokh.enums.ActionType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Trainer {

    @Id
    private Integer id;
    private Long trainerId;
    private String firstName;
    private String lastName;
    private String username;
    private boolean isActive;
    @Transient
    private int duration;
    @Transient
    private LocalDateTime startDate;

    @Transient
    private ActionType actionType;
}
