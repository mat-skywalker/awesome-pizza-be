package org.example.awesomepizzabe.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderCodeGeneratorTest {

    @Test
    void generateReturnsCodeWithExpectedPrefixLengthAndAlphabet() {
        String allowedCharacters = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";

        for (int i = 0; i < 100; i++) {
            String code = OrderCodeGenerator.generate();

            assertAll(
                    () -> assertNotNull(code),
                    () -> assertTrue(code.startsWith("AP-")),
                    () -> assertEquals(9, code.length()),
                    () -> assertTrue(code.substring(3).chars()
                            .allMatch(character -> allowedCharacters.indexOf(character) >= 0))
            );
        }
    }
}
