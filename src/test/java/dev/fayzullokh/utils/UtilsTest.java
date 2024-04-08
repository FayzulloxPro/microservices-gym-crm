package dev.fayzullokh.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UtilsTest {

    @Test
    void getTraineeIdGenerator() {
        Long traineeId = Utils.getTraineeIdGenerator();

        assertNotNull(traineeId);
        assertTrue(traineeId > 0);
    }

    @Test
    void getUserIdGenerator() {
        Long userId = Utils.getUserIdGenerator();
        assertNotNull(userId);
        assertTrue(userId > 0);
    }

    @Test
    void getTrainerIdGenerator() {
        Long trainerId = Utils.getTrainerIdGenerator();

        assertNotNull(trainerId);
        assertTrue(trainerId > 0);
    }

    @Test
    void getTrainingIdGenerator() {
        Long trainingId = Utils.getTrainingIdGenerator();

        assertNotNull(trainingId);
        assertTrue(trainingId > 0);
    }

    @Test
    void getUserNameSuffix() {
        Long userNameSuffix = Utils.getUserNameSuffix();

        assertNotNull(userNameSuffix);
        assertTrue(userNameSuffix > 0);
    }

    @Test
    void generatePassword() {
        String password = Utils.generatePassword();

        assertNotNull(password);
        assertTrue(password.length() == 10);
    }
}

