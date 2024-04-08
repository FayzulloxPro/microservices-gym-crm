package dev.fayzullokh.entity.mongoDbSummary;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Document(value = "trainers")
@CompoundIndexes({
        @CompoundIndex(name = "first_last_name_idx", def = "{'firstName': 1, 'lastName': 1}")
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class TrainerDocument {
    @Id
    private Integer id;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "First name is required")
    private String firstName;

    private String lastName;

    private Boolean status;

    private List<YearEntry> yearsList;
}

