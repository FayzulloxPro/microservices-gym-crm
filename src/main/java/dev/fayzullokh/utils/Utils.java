package dev.fayzullokh.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;

public class Utils {

    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    private static Long traineeIdGenerator = 0L;
    private static Long userIdGenerator = 0L;
    private static Long trainerIdGenerator = 0L;
    private static Long trainingIdGenerator = 0L;
    private static Long userNameSuffix = 0L;

    public static Long getUserNameSuffix() {
        userNameSuffix++;
        LOGGER.debug("Generated User Name Suffix: {}", userNameSuffix);
        return userNameSuffix;
    }

    public static Long getTraineeIdGenerator() {
        traineeIdGenerator++;
        LOGGER.debug("Generated Trainee ID: {}", traineeIdGenerator);
        return traineeIdGenerator;
    }

    public static Long getUserIdGenerator() {
        userIdGenerator++;
        LOGGER.debug("Generated User ID: {}", userIdGenerator);
        return userIdGenerator;
    }

    public static Long getTrainerIdGenerator() {
        trainerIdGenerator++;
        LOGGER.debug("Generated Trainer ID: {}", trainerIdGenerator);
        return trainerIdGenerator;
    }

    public static Long getTrainingIdGenerator() {
        trainingIdGenerator++;
        LOGGER.debug("Generated Training ID: {}", trainingIdGenerator);
        return trainingIdGenerator;
    }

    private static final String ALLOWED_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generatePassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(10);

        for (int i = 0; i < 10; i++) {
            int randomIndex = random.nextInt(ALLOWED_CHARACTERS.length());
            char randomChar = ALLOWED_CHARACTERS.charAt(randomIndex);
            password.append(randomChar);
        }

        String generatedPassword = password.toString();
        LOGGER.debug("Generated Password: {}", generatedPassword);
        return generatedPassword;
    }
}
