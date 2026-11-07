package org.example.awesomepizzabe.utils;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;
import java.util.stream.Collectors;

@UtilityClass
public class OrderCodeGenerator {

    // Alfabeto personalizzato a 32 caratteri (esclusi 0, 1, O, I per evitare difficile letture e confusione)
    private static final String CHARACTERS = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String PREFIX = "AP-";
    private static final int CODE_LENGTH = 6;

    public static String generate() {
        String randomPart = RANDOM.ints(CODE_LENGTH, 0, CHARACTERS.length())
                .mapToObj(CHARACTERS::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());

        return PREFIX + randomPart;
    }

}
