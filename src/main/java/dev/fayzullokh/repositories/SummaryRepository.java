package dev.fayzullokh.repositories;

import dev.fayzullokh.entity.Summary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SummaryRepository extends JpaRepository<Summary, Integer> {
    Summary findByTrainerId(int id);

    @Query("select s from Summary s where s.trainer.username=?1")
    Summary findByTrainer(String username);
}