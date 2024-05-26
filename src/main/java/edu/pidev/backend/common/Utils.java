package edu.pidev.backend.common;

import edu.pidev.backend.common.enumuration.TokenType;
import edu.pidev.backend.entity.Token;
import edu.pidev.backend.entity.User;

import java.security.SecureRandom;

public class Utils {
    public static String capitalizeWords(String input) {
        // split the input string into an array of words
        String[] words = input.split("\\s");

        // StringBuilder to store the result
        StringBuilder result = new StringBuilder();

        // iterate through each word
        for (String word : words) {
            // capitalize the first letter, append the rest of the word, and add a space
            result.append(Character.toTitleCase(word.charAt(0)))
                    .append(word.substring(1))
                    .append(" ");
        }

        // convert StringBuilder to String and trim leading/trailing spaces
        return result.toString().trim();
    }

    // Define weights for different attributes
    private static final int FIRST_NAME_WEIGHT = 10;
    private static final int LAST_NAME_WEIGHT = 10;
    private static final int EMAIL_WEIGHT = 10;
    private static final int ADDRESS_WEIGHT = 20;
    private static final int CITY_WEIGHT = 5;
    private static final int MOBILE_WEIGHT = 20;
    private static final int PHONE_WEIGHT = 20;
    private static final int GENDER_WEIGHT = 5;


    public static int calculateProfileCompilation(User user) {
        int compilation = 0;

        // Check and calculate percentage based on attribute presence and quality
        if (isNotEmpty(user.getFirstName())) {
            compilation += FIRST_NAME_WEIGHT;
        }
        if (isNotEmpty(user.getLastName())) {
            compilation += LAST_NAME_WEIGHT;
        }
        if (isNotEmpty(user.getAddress())) {
            compilation += ADDRESS_WEIGHT;
        }
        if (user.getAddressCity() != null) {
            compilation += CITY_WEIGHT;
        }
        if (isNotEmpty(user.getMobile())) {
            compilation += MOBILE_WEIGHT;
        }
        if (isNotEmpty(user.getPhone())) {
            compilation += PHONE_WEIGHT;
        }
        if (user.getGender() != null) {
            compilation += GENDER_WEIGHT;
        }
        if (user.getGender() != null) {
            compilation += EMAIL_WEIGHT;
        }

        return compilation;
    }

    private static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static String generateTokenCode(int length, TokenType type) {
        String characters = "";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();

        if (type.equals(TokenType.ACTIVATE_ACCOUNT)) {
            characters = "0123456789";
        }

        if (type.equals(TokenType.RESET_PASSWORD)) {
            characters = "0123456789qwertyuiopasdfghjklzxcvbnm";
        }

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }

    public static void formatUserName(User user) {
        String name;

        name = user.getFirstName();
        if (name != null) {
            user.setFirstName(capitalizeWords(name));
        }

        name = user.getLastName();
        if (name != null) {
            user.setLastName(name.toUpperCase());
        }

        user.getFullName();
    }
}
