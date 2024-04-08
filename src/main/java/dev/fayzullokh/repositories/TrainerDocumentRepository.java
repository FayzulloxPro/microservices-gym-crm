package dev.fayzullokh.repositories;

import dev.fayzullokh.entity.mongoDbSummary.TrainerDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerDocumentRepository extends MongoRepository<TrainerDocument, Integer> {
     TrainerDocument findByUsername(String username);

}